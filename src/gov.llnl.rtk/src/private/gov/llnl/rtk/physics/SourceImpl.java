package gov.llnl.rtk.physics;

/**
 *
 * @author nelson85
 */
public class SourceImpl implements Source
{
  public Nuclide nuclide;
  public double activity;
  public double atoms;

  public SourceImpl(Source s)
  {
    this.nuclide = s.getNuclide();
    this.activity = s.getActivity();
    this.atoms = s.getAtoms();
  }

  public SourceImpl(Nuclide nuc)
  {
    this.nuclide = nuc;
  }

  @Override
  public String toString()
  {
    return String.format("Source(%s,%.3e)", this.getNuclide().getName(), getActivity());
  }

  @Override
  public Nuclide getNuclide()
  {
    return nuclide;
  }

  @Override
  public double getAtoms()
  {
    return atoms;
  }

  @Override
  public double getActivity()
  {
    return activity;
  }

}
