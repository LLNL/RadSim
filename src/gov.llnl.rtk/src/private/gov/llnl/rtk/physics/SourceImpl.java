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

  public SourceImpl()
  {
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    sb.append(this.getNuclide()).append(" ").append(getActivity());
    return sb.toString();
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
