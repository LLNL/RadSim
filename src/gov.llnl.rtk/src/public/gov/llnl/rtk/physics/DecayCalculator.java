package gov.llnl.rtk.physics;

import gov.llnl.math.matrix.Matrix;
import gov.llnl.math.matrix.MatrixFactory;
import gov.llnl.math.matrix.MatrixOps;
import gov.llnl.math.matrix.MatrixRowOperations;
import gov.llnl.math.matrix.MatrixViews;
import gov.llnl.utility.TemporalUtilities;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * Decay calculator.
 *
 * This should have several modes of operation.
 *
 * <ul>
 * <li> equilibrium
 * <li> age
 * <li> grow (special case in which we keep the initial state so we can work
 * backwards.)
 * </ul>
 *
 * @author nelson85
 */
public class DecayCalculator
{
  // FIXME
  //  This should be able to take a number of different libraries.
  //    DecayLibrary
  //    SpontaneousFissionLibrary
  //    ReactorLibrary?
  //    perhaps induced transitions
  DecayLibrary decayLibrary;
  HashMap<Source, Integer> indices = new HashMap<>();

  public transient Matrix initial = null;
  public transient Matrix transitions = null;
  public transient Matrix u = null;

  /**
   * Set the physics library to use for this process.
   *
   * @param library
   */
  public void setDecayLibrary(DecayLibrary library)
  {
    this.decayLibrary = library;
  }

  /**
   * Compute the result of aging a source.
   *
   * @param source
   * @param time
   * @return
   */
  public List<Source> age(Source source, Duration time)
  {
    return age(Arrays.asList(source), TemporalUtilities.toSeconds(time));
  }

  /**
   * Compute the result of aging a source.
   *
   * @param source is the nuclide and activity.
   * @param time is the time to age in seconds.
   * @return
   */
  public List<Source> age(Source source, double time)
  {
    return age(Arrays.asList(source), time);
  }

  public List<Source> age(List<Source> sourceInput, Duration time)
  {
    return age(sourceInput, TemporalUtilities.toSeconds(time));
  }

  public List<Source> age(List<Source> sources, double time)
  {
    List<Source> working = convertSources(sources);

    // Convert to a matrix problem
    int n = working.size();
    Matrix transitions = computeTransitionMatrix(working);
    Matrix initial = MatrixFactory.newMatrix(n, 1);
    this.transitions = transitions;
    this.initial = initial;

    for (Source s : working)
    {
      int index = indices.get(s);
      initial.set(index, 0, s.getAtoms());
    }

    // Compute the exponential
    Matrix u = expm(transitions, time);
    this.u = u;

    // Convert to atoms in each nuclide
    Matrix a = MatrixOps.multiply(u, initial);

    // Propogate the result back to the source list.
    List<Source> out = new ArrayList<>();
    for (Source source : working)
    {
      int index = indices.get(source);
      out.add(Source.fromAtoms(source.getNuclide(), a.get(index,0)));
    }
    return out;
  }

  /**
   * Compute transient equilibrium state.
   *
   * @param source
   * @return
   */
  public List<Source> transientEquilibrium(Source source)
  {
    // Convert to a modifiable source
    SourceImpl input = new SourceImpl(source);
    double lambda = source.getNuclide().getDecayConstant();
    double N = source.getActivity() / lambda;

    // Add teh transiend equibrium from each path
    List<Source> output = new ArrayList<>();
    output.add(input);
    for (DecayTransition ts : decayLibrary.getTransitionsFrom(source.getNuclide()))
    {
      if (ts.getChild() == null)
        continue;
      updateTE(output, ts, N, lambda, input);
    }

    // Report back with the public types
    return Collections.unmodifiableList(compact(output));
  }

//<editor-fold desc="internal" defaultstate="collapsed">
  void updateTE(List<Source> output, DecayTransition transition, double NA, double lambdaA, Source source)
  {
    Nuclide nuclide = transition.getChild();
    double br = transition.getBranchingRatio();

    // Break chains where the child has a greater half life than the parent, there
    // is no equilibium between the parent and child.
    if (source.getNuclide().getHalfLife() < nuclide.getHalfLife())
      return;

    double lambdaB = nuclide.getDecayConstant();
    if (lambdaB == 0)
      return;

    double NB = br * lambdaA * NA / lambdaB;
    output.add(Source.fromAtoms(transition.getChild(), NB));
    List<DecayTransition> tl = decayLibrary.getTransitionsFrom(nuclide);
    if (tl != null)
    {
      for (DecayTransition ts : tl)
      {
        if (ts.getChild() != null)
          updateTE(output, ts, NB, lambdaB, source);
      }
    }
  }

  /**
   * Recursively find all daughters.
   *
   * @param out
   * @param source
   * @return
   */
  List<Source> traceChildren(List<Source> out, Source source)
  {
    if (!containsNuclide(out, source.getNuclide()))
    {
      out.add(source);
    }
    else
    {
      SourceImpl s2 = (SourceImpl) findNuclide(out, source.getNuclide());
      s2.atoms += source.getAtoms();
      s2.activity += source.getActivity();
    }

    for (DecayTransition ts : decayLibrary.getTransitionsFrom(source.getNuclide()))
    {
      // Skip fission for now.
      if (ts.getChild() == null)
        continue;
      traceChildren(out, Source.of(ts.getChild()));
    }
    return out;
  }

  /**
   * This is a special matrix exponential for decay calculations.
   *
   * Each column must be preserving atoms without losses. We will need to add
   * spontaneous fission, fission produces and the like here later with the
   * restriction that the total of the columns needs to account for splits as a
   * negative row so that the mass total remains constant (rather than the atom
   * total.)
   *
   * @param m is the transition matrix built using decay constants.
   * @param t is the time in seconds.
   * @return
   */
  public Matrix expm(Matrix m, double t)
  {
    // This is the minimum number of squares that we expect to perform in post correction
    //  The post divisions the smaller the matrix elements the less values are
    //  required in the series.
    int k = 5;

    // This is the order of the Pade approximate.
    //   It should be between 4-8 for accuracy.
    //   Smaller the 4 caused accuracy issues we lack enough terms in the series.
    //   Larger than 8 tensts to cause inaccuracies due to rounding in the inverse.
    int l = 5;

    // Compute the scale
    Matrix m2 = MatrixOps.multiply(m, m);
    double maxSqr = MatrixOps.maximumOfAll(m2);
    double max = Math.sqrt(maxSqr);

    // This is dynamic to the time scale and the decay constants we are computing.
    //   The larger the time scale the more squares; the larger the decay constant
    //   the more equares we will require.
    int v = Math.max((int) (Math.log(t * max) / Math.log(2)) + k, k);
    if (v > 0)
      t = t / Math.pow(2.0, v);

    // Pade Approximate for exponential
    MatrixOps.multiplyAssign(m, t);
    Matrix u = MatrixFactory.newMatrix(m.rows(), m.columns());
    MatrixOps.fill(MatrixViews.diagonal(u), 1.0);
    Matrix p1 = MatrixFactory.newMatrix(m.rows(), m.columns());
    Matrix p2 = MatrixFactory.newMatrix(m.rows(), m.columns());

    // Dynamically compute the coefficents of the series using 1F1(-m; -m-n; z)
    double kn = 1;
    double kd = 1;
    for (int i = 0; i < l + 1; ++i)
    {
      if (i % 2 == 0)
      {
        MatrixOps.addAssignScaled(p1, u, kn / kd);
      }
      else
      {
        MatrixOps.addAssignScaled(p2, u, kn / kd);
      }
      if (i < l)
      {
        kn *= (l - i);
        kd *= (2 * l - i) * (i + 1);
        u = MatrixOps.multiply(u, m);
      }
    }
    Matrix mn = MatrixOps.add(p1, p2);
    Matrix md = MatrixOps.subtract(p1, p2);
    Matrix r = solve(md, mn);
//    Matrix r = MatrixOps.divideLeft(md, mn);

    // Post correction
    if (v > 0)
    {
      // Square the product multiple times
      for (int i = 0; i < v; ++i)
      {
        Matrix r2 = r.copyOf();

        // This is a special trick for dealing with rounding in each column.
        // The sum of each column should be 1 because every atom must neither
        // be created nor destroyed, but because of rounding we may end up
        // rounded up or down.  So one half of the squaring matrix is corrected
        // by 1/sum if the sum is not equal to zero.  Thus if we are rounded up
        // the multiplyed matrix will be rounded down, or the reverse.  This
        // cancels out the rounding errors as we progress through time.
        //
        // Note that if we have fission we will need a special row to represent
        // that added products which will always have a negative value.  That
        // way ever time we get a fission we get 2 products and -1 bookkeeping
        // so that sum is always equal to 1.
        for (int j = 0; j < r2.columns(); ++j)
        {
          Matrix r3 = MatrixViews.selectColumn(r2, j);
          double s = MatrixOps.sumOfElements(r3);
          if (s != 1.0)
          {
            MatrixOps.divideAssign(r3, s);
          }
        }

        // Square the matrix.
        r = MatrixOps.multiply(r2, r);
      }
    }
    return r;
  }

  /**
   * Solver that works the upper triangular matrix first.
   *
   * Not sure if this helps anything but decay matrices have a lot of structure.
   *
   * @param a0
   * @param b0
   * @return
   */
  Matrix solve(Matrix a0, Matrix b0)
  {
    int n = a0.rows();
    Matrix.RowAccess a = MatrixFactory.newRowMatrix(a0);
    Matrix.RowAccess b = MatrixFactory.newRowMatrix(b0);
    MatrixRowOperations roa = MatrixFactory.createRowOperations(a);
    MatrixRowOperations rob = MatrixFactory.createRowOperations(b);
    for (int i = n - 1; i >= 0; i--)
    {
      double d = a.get(i, i);
      for (int j = i - 1; j >= 0; j--)
      {
        double v = a.get(j, i);
        if (v == 0)
          continue;
        roa.addScaledRows(j, i, -v/d);
        rob.addScaledRows(j, i, -v/d);
      }
      if (d != 1.0)
      {
        roa.divideAssignRow(i, d);
        roa.apply();
        rob.divideAssignRow(i, d);
      }
    }
    for (int i = 0; i < n; i++)
    {
      for (int j = i + 1; j < n; j++)
      {
        double v = a.get(j, i);
        if (v == 0)
          continue;
        roa.addScaledRows(j, i, -v);
        rob.addScaledRows(j, i, -v);
      }
    }
    return b;
  }

  /**
   * Take a source list and fill in the required daughter products.
   *
   * This is required before computing a transition matrix.
   *
   * @param sourceInput
   * @return
   */
  List<Source> convertSources(List<Source> sourceInput)
  {
    List<Source> sources = new ArrayList<>();

    // Find all the children of the source
    for (Source source : sourceInput)
    {
      traceChildren(sources, new SourceImpl(source));
    }

    // Assign all of the sources an index
    this.indices.clear();
    int i = 0;
    for (Source s : sources)
    {
      this.indices.put(s, i++);
    }
    return sources;
  }

  Matrix computeTransitionMatrix(List<? extends Source> sources)
  {
    // Convert to a matrix problem
    int n = sources.size();
    Matrix transitions = MatrixFactory.newMatrix(n, n);
    for (Source s : sources)
    {
      int index = indices.get(s);
      if (s.getNuclide().isStable() || decayLibrary.getTransitionsFrom(s.getNuclide()) == null && s.getNuclide().getHalfLife() > 1E20)
        continue;
      double lambda = s.getNuclide().getDecayConstant();
      double bt = 0;
      for (DecayTransition ts : decayLibrary.getTransitionsFrom(s.getNuclide()))
      {
        // Skip fission for now
        if (ts.getChild() == null)
          continue;
        bt += ts.getBranchingRatio();
      }

      // We don't let atoms get lost currently, so the sum of the branching
      // ratio must be 1.  We can losen this restriction by adding an
      // unaccounted for nuclide.
      if (bt != 1.0)
      {
        System.out.println("Warning " + s.getNuclide() + " BR=" + bt + " " + s.getNuclide().getHalfLife());
        for (DecayTransition ts : decayLibrary.getTransitionsFrom(s.getNuclide()))
          System.out.println("  " + ts.getChild().toString());
      }

      transitions.set(index, index, -lambda);
      for (DecayTransition ts : decayLibrary.getTransitionsFrom(s.getNuclide()))
      {
        if (ts.getChild() == null)
          continue;
        Source s2 = findNuclide(sources, ts.getChild());
        transitions.set(indices.get(s2), index, lambda * ts.getBranchingRatio() / bt);
      }
    }
    return transitions;
  }

  static List<? extends Source> compact(List<? extends Source> source)
  {
    // Copy the list
    ArrayList<SourceImpl> tmp = source.stream().map(p -> new SourceImpl(p)).collect(Collectors.toCollection(ArrayList::new));
    // Sort by atomic mass
    tmp.sort((p1, p2) -> Double.compare(p1.getNuclide().getAtomicMass(), p2.getNuclide().getAtomicMass()));
    // Combine by atomic mass.
    LinkedList<SourceImpl> out = new LinkedList<>();
    SourceImpl last = null;
    for (SourceImpl e : tmp)
    {
      if (last == null)
      {
        out.add(e);
        last = e;
        continue;
      }
      if (e.getNuclide().getAtomicMass() == last.getNuclide().getAtomicMass())
      {
        last.atoms += e.getAtoms();
        last.activity += e.getActivity();
        continue;
      }
      out.add(e);
      last = e;
    }
    return out;
  }

  static boolean containsNuclide(List<Source> sources, Nuclide n)
  {
    return sources.stream().anyMatch(p -> p.getNuclide().getName().equals(n.getName()));
  }

  static Source findNuclide(List<? extends Source> sources, Nuclide n)
  {
    Optional<? extends Source> nuclide = sources.stream().filter(p -> p.getNuclide().getName().equals(n.getName())).findFirst();
    if (nuclide.isEmpty())
    {
      throw new NoSuchElementException("Unable to find nuclide " + n);
    }
    return nuclide.get();
  }
//</editor-fold>
}
