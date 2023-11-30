/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.spline;

import gov.llnl.math.DoubleArray;
import gov.llnl.math.IntegerArray;
import gov.llnl.math.MathExceptions;
import static gov.llnl.math.spline.CubicAreaHermiteSpline.*;

/**
 *
 * @author nelson85
 */
public class AreaMatchedSplineCalculator
{

  private final static double MICRO = 1.e-6;
  private final static double HALF = 0.5;
  static final int MAX_NUM_ITER = 36;

  static public CubicAreaHermiteSpline create(double[] boundary, double[] area)
          throws MathExceptions.SizeException, MathExceptions.MathException
  {
    if (boundary.length != area.length + 1)
    {
      throw new MathExceptions.SizeException("input size mismatch");
    }

    // input data points
    int numDataPoints = boundary.length;
    int numIntervals = numDataPoints - 1; // for an open curve

    ////////////////////////////////////////////////////////////////////
    //////////  SET THE ESSENTIAL ARRAYS FOR COMPUTATION ///////////////
    ////////////////////////////////////////////////////////////////////
    // Compute the integral of each interval
    double[] integral = new double[numDataPoints];
    integral[0] = 0.0;
    for (int i = 1; i < numDataPoints; ++i)
    {
      integral[i] = integral[i - 1] + area[i - 1];
    }

    // counts in each bin
    double[] ds = new double[numIntervals];
    for (int i = 0; i < numIntervals; i++)
    {
      ds[i] = integral[i + 1] - integral[i];
    }

    // then the bin width and the average count per unit
    double[] width = new double[numIntervals]; // width
    double[] density = new double[numIntervals];  // density
    for (int i = 0; i < numIntervals; i++)
    {
      width[i] = boundary[i + 1] - boundary[i];
      density[i] = ds[i] / width[i];
    }

    ////////////////////////////////////////////////////////////////////
    //////////////////   PROBLEM INITIALIZATION    /////////////////////
    ////////////////////////////////////////////////////////////////////
    // slope of "g" (g') of the monotone interpolation at knots
    double[] sp = new double[numDataPoints];
    // slope of "f" (f = g', so spp is f' = g") at knots
    double[] spp = new double[numDataPoints];

    // now compute the first and second derivatives from the data
    estimateDerivatives(width, density, sp, spp);

    ///////////////////////////////////////////////////////////////////////
    ///////////// MAIN OPERATION: THE HERMIT SPLINE THAT //////////////////
    ///////////// MATCHES AREAS EXACTLY WITH NO OSCILLATION ///////////////
    ///////////////////////////////////////////////////////////////////////
    // the solution array
    double[] mid = new double[numDataPoints];

    // re-estimate slopes to be consistent to the first Hermite spline that
    // intersects the bin walls for control points with integer subscripts
    updateSlopeSpace(boundary, mid, width, density, spp, numIntervals);

    ////////////////////////////////////////////////////////////////////
    ////////// I MATCH LOCAL AREAS EXACTLY WITH ALL CONTROL ////////////
    ////////// POINTS FOR THE FINAL HERMITE SPLINE SOLUTION ////////////
    ////////////////////////////////////////////////////////////////////
    // new number of control points
    int numControlPoints = 2 * numIntervals + 1;
    int numSubIntervals = numControlPoints - 1;

    // new knots for cubic spline
    CubicAreaHermiteSpline hs = hermitSplineAreaMatch(boundary, width, density, mid, numControlPoints);

    ///////////////////////////////////////////////////////////////
    ////////// DETECT AND FIX POSSIBLE NEGATIVENESS ///////////////
    ///////////////////////////////////////////////////////////////
    // to tag the bins with possible negativeness
    int[] binTag = new int[numIntervals];
    IntegerArray.fill(binTag, 1);

    // compute minimum in each sub bin and if negativeness is observed
    // (assumed isolated), do a local fix and make the minimums zeros.
    detectNegativeness(hs, binTag, numSubIntervals);

    // treat negativeness;
    for (int i = 0; i < numIntervals; i++)
    {
      if (binTag[i] == 0)
      {
        System.out.printf(" bin %d possible negativeness \n", i);
        treatNegativeness(2 * i, hs, density);
      }
    }

    return hs;
  }

  /**
   * Estimate derivatives at knots.
   *
   * @param width (input)
   * @param density (input)
   * @param sp (output) estimated first derivative
   * @param spp (output) estimated second derivative
   */
  private static void estimateDerivatives(double[] width, double[] density, double[] sp, double[] spp)
  {
    // i fit a quadratic to compute "sp" and "spp" for internal nodes.
    // s(x) = alpa (x - x[i])^2 + beta (x - x[i]) + gama, only the
    // interior knots are treated, boundary condition to be added later
    for (int i = 1; i < width.length; i++)
    {
      double var = (width[i] + width[i - 1]);
      double alfa = (density[i] - density[i - 1]) / var;
      double beta = (density[i] * width[i - 1] + density[i - 1] * width[i]) / var;
      sp[i] = beta;
      spp[i] = alfa + alfa;
    }
  }

  /**
   * Re-estimate slopes in slope space to be consistent to the first Hermite
   * spline.
   *
   * @param x (input)
   * @param y (output)
   * @param dx (input)
   * @param h (input)
   * @param X (output)
   * @param Y (output)
   * @param spp (input)
   * @param numIntervals
   */
  private static void updateSlopeSpace(double[] x, double[] y, double[] dx, double[] h, double[] spp, int numIntervals)
  {
    int numDataPoints = x.length;

    // create a set of data points
    double[] X = new double[numDataPoints];
    double[] Y = new double[numDataPoints];

    // ghost y-coordinate preset for applying boundary conditions
    double yLL;
    double yRR;

    // differential at the two ends
    double sL, sR;

    // left boundary conditions
    double dh;

    // the left end
    X[0] = HALF * dx[0];
    Y[0] = h[0];

    // the right end
    X[numDataPoints - 1] = HALF * (x[numDataPoints - 2] + x[numDataPoints - 1]);
    Y[numDataPoints - 1] = h[numIntervals - 1];

    int iter = 0;

    //////////////////////////////////////////////////////////////////
    /////////////////  FIX THE SLOPE AT KNOTS   //////////////////////
    //////////////////////////////////////////////////////////////////
    while (iter < 16)
    {
      // quadratic area matched fitting for the interior points
      // the iteration here is in order to make slope consistent
      // with the current Hermit spline (on mid bin control points)
      for (int i = 1; i < numIntervals; i++)
      {
        double lift;

        sL = spp[i];
        sR = spp[i + 1];

        // apply lift(by fitting a quadratic)
        lift = dx[i] * (sL - sR) / 24.0;

        X[i] = x[i] + HALF * dx[i];
        Y[i] = h[i] + lift;
      }

      // update the slope "spp"
      for (int i = 0; i < numIntervals; i++)
      {
        double xL = X[i];
        double yL = Y[i];

        double xR = X[i + 1];
        double yR = Y[i + 1];

        yLL = h[0] - HALF * (h[1] - h[0]);

        if (i > 0)
        {
          yLL = Y[i - 1];
        }

        dh = h[numIntervals - 2] - h[numIntervals - 1];
        yRR = h[numIntervals - 1] - HALF * dh;

        if (i < numIntervals - 1)
        {
          yRR = Y[i + 2];
        }

        double syL = HALF * (yR - yLL);
        double syR = HALF * (yRR - yL);

        double xOld = xL;
        double yOld = yL;

        for (int j = 0; j < 20; j++)
        {

          double u = 0.05 * j;
          double xC = xL + u * (xR - xL);
          double yC = yL * H00(u) + syL * H10(u) + yR * H01(u) + syR * H11(u);

          // estimate intersection of curve "i" and the cell wall xW = x[i],
          // for finding a good initial guess of height of curve on a wall.
          double xW = x[i + 1];
          if (xOld <= xW && xC > xW)
          {
            // the intersection is found
            double ratio = (xW - xOld) / (xC - xOld);
            y[i + 1] = (1.0 - ratio) * yOld + ratio * yC;

            // update slope
            spp[i + 1] = (yC - yOld) / (xC - xOld);
          }

          xOld = xC;
          yOld = yC;
        }
      }

      iter++;
    }
  }

  /**
   * Match area in each bin exactly with Hermite spline on the new control
   * points.
   *
   * @param hs (output)
   * @param boundary
   * @param width
   * @param density
   * @param mid
   * @param numDataPoints
   * @param numIntervals
   */
  static private CubicAreaHermiteSpline hermitSplineAreaMatch(
          double[] boundary, double[] width, double[] density, double[] mid, int numControlPoints)
  {

    int numDataPoints = boundary.length;
    int numIntervals = boundary.length - 1;

    // what it really does is to define (xnew, ynew) as the new bin system with
    // number of bins doubled
    // I use a tridiagonal liner solver to do the area matching.
    // note the system has a strong diagonal line, a good feature
    // define the diagonals
    double[] c = new double[numIntervals - 1];
    double[] a = new double[numIntervals - 1];
    double[] b = new double[numIntervals];

    DoubleArray.fill(c, -1.0);
    DoubleArray.fill(a, -1.0);
    DoubleArray.fill(b, 26.0);

    // *the solution vector
    double[] z = new double[numIntervals];

    // fill "z" with the right hand side initially
    // ghost knot over left boundary (counter symmetric)
    double gyL = 2 * density[0] - density[1];

    // ghost knot over the right boundary (symmetric)
    double gyR = density[numIntervals - 1];

    // left boundary
    mid[0] = density[0] - HALF * (density[1] - density[0]);

    // right boundary
    mid[numIntervals] = density[numIntervals - 1]
            - HALF * (density[numIntervals - 2] - density[numIntervals - 1]);

    double[] RHS = new double[numIntervals];

    for (int i = 0; i < numIntervals; i++)
    {

      double ys = mid[i] + mid[i + 1];
      double delta = 0.0;

      if (i == 0)
      {
        delta = gyL;
      }

      if (i == numIntervals - 1)
      {
        delta = gyR;
      }

      RHS[i] = 48 * density[i] - 12 * ys + delta;
    }

    System.arraycopy(RHS, 0, z, 0, numIntervals);

    ////////////////////////////////////////////////////////////////////
    //////////////////  SOLVE THE TRI-DIAGONAL SYSTEM  /////////////////
    ////////////////////////////////////////////////////////////////////
    triDiagonalSolver(z, a, b, c, numIntervals);

    // check area match
//    for (int i = 0; i < numIntervals; i++)
//    {
//      double term2 = 0;
//
//      if (i == 0)
//      {
//        term2 = (2 * z[i] - gyL - z[i + 1]) / 24.0;
//      }
//      else if (i == numIntervals - 1)
//      {
//        term2 = (2 * z[i] - z[i - 1] - gyR) / 24.0;
//      }
//      if (i > 0 && i < numIntervals - 1)
//      {
//        term2 = (2 * z[i] - z[i - 1] - z[i + 1]) / 24.0;
//      }
//    }
    // define (xnew, ynew) next
    // xnew is fixed
    CubicAreaHermiteSpline hs = CubicAreaHermiteSpline.allocate(numControlPoints);
    hs.setEndBehavior(EndBehavior.CLAMP);

    int count = 0;
    for (int i = 0; i < numIntervals; i++)
    {
      System.out.println(count + " " + hs.control[count]);
      double xknot = boundary[i];
      hs.control[count] = new CubicAreaHermiteSpline.ControlPoint();
      hs.control[count].x = xknot;
      hs.control[count].y = Math.max(0.0, mid[i]); // these are above the knots, fixed
      count++;
      hs.control[count] = new CubicAreaHermiteSpline.ControlPoint();
      hs.control[count].x = xknot + width[i] / 2;
      hs.control[count].y = z[i];
      count++;
    }

    hs.control[count] = new CubicAreaHermiteSpline.ControlPoint();

    // x-coordinate of the control point on the right boundary
    hs.control[count].x = boundary[numDataPoints - 1];

    // y-component of the control points on the right boundary (symmetric)
    hs.control[count].y = mid[numIntervals];

    // evaluate slope at knots
    for (int i = 0; i < 2 * numIntervals + 1; i++)
    {
      if (i == 0)
      {
        hs.control[i].dy = 0.5 * (hs.control[i + 1].y - gyL);
      }
      else if (i == 2 * numIntervals)
      {
        hs.control[i].dy = 0.5 * (gyR - hs.control[i - 1].y);
      }
      else
      {
        hs.control[i].dy = 0.5 * (hs.control[i + 1].y - hs.control[i - 1].y);
      }
    }
    return hs;
  }

// detect possible negativeness in isolated bins
  private static void detectNegativeness(CubicAreaHermiteSpline hs,
          int[] binTag, int numSubIntervals) throws MathExceptions.MathException
  {
//    double[] ynew = hs.controlY;
//    double[] dy = hs.controlDY;

    for (int i = 0; i < numSubIntervals; i++)
    {
      // for each sub (half) interval, compute the minumum

      double yL = hs.control[i].y;
      double yR = hs.control[i + 1].y;

      double syL = hs.control[i].dy;
      double syR = hs.control[i + 1].dy;

      // finds "u" that makes y'(u) = 0, then compare it with
      // the two end knots to see which one is the lowest point.
      // y'(u) = 0 is a quadratic so can be solved analytically
      // so now define the coefficients of the quadratic f'(u) = 0.
      // the coefficient for A u^2
      double A = 6 * yL + 3 * syL - 6 * yR + 3 * syR;

      // the coefficient for 2B u^1
      double B = - 3 * yL - 2 * syL + 3 * yR - syR;

      // the coefficient for C u^0
      double C = syL;

      double root[] = new double[2];

      int numRoots = 0; // assume no roots

      if (Math.abs(A) > MICRO)
      {
        // scale again
        B /= A;
        C /= A;
        A = 1.0;

        double delta = Math.pow(B, 2) - C;

        if (delta < 0.0)
        {
          // no real critical point, slope will not change sign between (0, 1)
          // also by choice all knots are on or above y = 0, so no negativeness.
          numRoots = 0;
        }
        else
        {
          // two critical points exist
          // here real roots exist, the two roots are
          delta = Math.sqrt(delta);

          root[0] = -B - delta;
          root[1] = -B + delta;
          numRoots = 2;

          if (root[0] < 0.0 || root[0] > 1.0)
          {
            root[0] = root[1];
            numRoots--;
          }

          if (root[1] < 0.0 || root[1] > 1.0)
          {
            numRoots--;
          }
        }
      }
      else
      {
        // probably not quadratic but linear
        root[0] = -C / (B + B);

        if (0.0 <= root[0] && root[0] <= 1.0)
        {
          numRoots = 1;
        }
      }

      int numNegatives = 0;

      if (numRoots > 0)
      {
        // check if the root is a minimum
        for (int count = 0; count < numRoots; count++)
        {
          double det = A * root[count] + B;
          if (det >= 0)
          {
            // possibly a minimum, check if negative
            double u = root[count];
            double value
                    = yL * H00(u) + syL * H10(u) + yR * H01(u) + syR * H11(u);
            if (value < 0)
            {
              root[numNegatives] = root[count];
              numNegatives++;
            }
          }
        }
      } // end checking negatives

      if (numNegatives == 1)
      {
        // one negative, perform the treatment.
        // i am ok to do this for the negative is assumed isolated
        // then the treatment is definitely local.
        System.out.printf(" bin %d possible negativeness\n", i);

        int originalBinID = (int) (i / 2);
        binTag[originalBinID] = 0; // negativeness treated in this bin
      }
      else if (numNegatives == 2)
      {
        throw new MathExceptions.MathException("strange case");
      }
    }
  }

// finding new knot with matching area in the solution bin
  static private double knotLocation(CubicAreaHermiteSpline hs, double s0, int i)
  {
    //    left knot of the sub-bin "i" and slope
    double xL = hs.getX(i);
    double yL = hs.getY(i);
    double dyL = hs.getDY(i);

//     right knot of the sub-bin "i" and slope
    double xR = hs.getX(i + 1);
    double yR = hs.getY(i + 1);
    double dyR = hs.getDY(i + 1);

//     width of bin
    double dx = (xR - xL);

//     area to match
    double areaToMatch = s0 - hs.getS(i);

    double ds = hs.getS(i + 1) - hs.getS(i);
//     initial guess
    double u = areaToMatch / ds;

//     to integrate the Hermite interpolant to "u" to match area
    //    the function: yL H00(u) + dyL H10(u) + yR H10(u) + dyR H11(u)
    //    since the function is monotone, it is safe with any method
    //    i try my (??) accelerated Newton's scheme
    double f;
    double diff = 1.0e6;
    int iter = 0;

    while (iter < 36 && diff > 1.0e-12)
    {
      f = dx * (yL * K00(u) + dyL * K10(u) + yR * K01(u) + dyR * K11(u)) - areaToMatch;
      double df = dx * (yL * H00(u) + dyL * H10(u) + yR * H01(u) + dyR * H11(u));
      double up = u - f / df;

      f += dx * (yL * K00(up) + dyL * K10(up) + yR * K01(up) + dyR * K11(up)) - areaToMatch;
      double du = -f / df;
      u += du;

      diff = Math.min(Math.abs(du), Math.abs(f));
      iter++;
    }

    return (xL + dx * u);
  }

///////////////////////////////////////////////////////////////////////////
// TreatNegativeness()
// treat isolated interior negativeness
// algorithm: delete the control point at the middle, then there are
//            three area match conditions and a nonnegative condition
//            to solve positions of the four neighbor control points.
// author:    JIN YAO
///////////////////////////////////////////////////////////////////////////
  private static void treatNegativeness(int subBinID, CubicAreaHermiteSpline hs, double h[])
  {
    //   double[] ynew = hs.controlY;
    //   double[] dy = hs.controlDY;
    // first figure out the original bin ID
    int i = subBinID / 2;
    int shift = subBinID - 2 * i;

    // read in current control points that violates positiveness
    int iL = subBinID - shift;
    int iLL = iL - 1;
    int iLLL = iLL - 1;
    int iM = iL + 1;
    int iR = iL + 2;
    int iRR = iR + 1;
    int iRRR = iRR + 1;

    // position of Y at the local control points
    double yLLL = hs.control[iLLL].y;
    double yRRR = hs.control[iRRR].y;

    // level in each bin involved
    double hM = h[i];
    double hL = h[i - 1];
    double hR = h[i + 1];

    // fix control points in the middle bin
    double yM = hM;
    double yL = hM;
    double yR = hM;

    // modify slope for negativeness treatment
    hs.control[iL].dy = 0.0;
    hs.control[iM].dy = 0.0;
    hs.control[iR].dy = 0.0;

    double yLL = 2 * hL - HALF * (yLLL + yL) - hs.control[iLLL].dy / 12.0;
    double yRR = 2 * hR - HALF * (yRRR + yR) + hs.control[iRRR].dy / 12.0;

    // update the positions of control points regarding keeping positivity.
    hs.control[iLL].y = yLL;
    hs.control[iL].y = yL;
    hs.control[iM].y = yM;
    hs.control[iR].y = yR;
    hs.control[iRR].y = yRR;

    // update the slope
    hs.control[iLL].dy = HALF * (yL - yLLL);
    hs.control[iL].dy = 0.0;
    hs.control[iM].dy = 0.0;
    hs.control[iR].dy = 0.0;
    hs.control[iRR].dy = HALF * (yRRR - yR);
  }

  private static void triDiagonalSolver(double x[], double a[], double b[], double c[], int N)
  {
    // Allocate scratch space.
    double[] cp = new double[N];

    cp[0] = c[0] / b[0];
    x[0] = x[0] / b[0];

    /* loop from 1 to N - 1 inclusive */
    for (int in = 1; in < N; in++)
    {
      double t = 1.0 / (b[in] - a[in - 1] * cp[in - 1]);
      cp[in] = c[in - 1] * t;
      x[in] = (x[in] - a[in - 1] * x[in - 1]) * t;
    }

    /* loop from N - 2 to 0 inclusive, safely testing loop end condition */
    for (int in = N - 1; in-- > 0;)
    {
      x[in] = x[in] - cp[in] * x[in + 1];
    }
  }

  //  double[] equalCountBinEdges;
//  double[] equalCountBinDensity;
//
//  public double[] EqualCountBinEdges()
//  {
//    return equalCountBinEdges;
//  }
//
//  public double[] EqualCountBinDensity()
//  {
//    return equalCountBinDensity;
//  }
  //// back solve for even bin count in each new bin
//  public void backSolveNewBins(CubicAreaHermiteSpline hs, int M)
//  {
////  M is desired number of bins
//
//    //  N the number of new knots
//    int N = M + 1;
//
//    // the desired counts each bin (equal counts each bin, say)
//    double totalArea = hs.getS(hs.getNumControlPoints() - 1);
//    double ds0 = totalArea / ((double) M);
//
//    //   the aaccumulated counts
//    double s0 = 0.0;
//
//    // compute the new bin edges
//    double[] equalCountBinEdges = new double[N];
//
//    //   the left end alignment
//    equalCountBinEdges[0] = hs.getX(0);
//
//    int knotID = 1;
//    int binID = 0;
//
//    double uOld = 0.0;
//
//    int startBin = 0;
//
////     search for even area increasing points
//    for (int i = 1; i < M; i++)
//    {
//      //      the desired accumulated count
//      s0 += ds0;
//
////       search for the solution bin of "s0"
//      for (int j = startBin; j < hs.getNumControlPoints() - 1; j++)
//      {
//        if (hs.getS(j) < s0 && s0 <= hs.getS(j + 1))
//        {
//          equalCountBinEdges[i] = knotLocation(hs, s0, j);
//          startBin = j;
//        }
//      }
//    }
//
//    // right end of the bin-structure
//    equalCountBinEdges[M] = hs.getX(hs.getNumControlPoints() - 1);
//
//    // allocate space for density each bin
//    double[] equalCountBinDensity = new double[M];
//    // print out the density in each bin
//    for (int i = 0; i < M; i++)
//    {
//      equalCountBinDensity[i] = ds0 / (equalCountBinEdges[i + 1] - equalCountBinEdges[i]);
//    }
//  }
}
