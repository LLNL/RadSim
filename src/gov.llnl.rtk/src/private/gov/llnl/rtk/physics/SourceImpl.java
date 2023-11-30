package gov.llnl.rtk.physics;

/**
 *
 * @author nelson85
 */
public class SourceImpl implements Source
{
  public Nuclide nuclide;
  public double activity;

  
  public SourceImpl(Source s)
  {
    this.nuclide = s.getNuclide();
    this.activity = s.getActivity();
  }
  
  public static SourceImpl of(Nuclide nuclide)
  {
    SourceImpl source = new SourceImpl();
    source.nuclide = nuclide;
    return source;
  }

  public static SourceImpl fromActivity(Nuclide nuclide, double activity, ActivityUnit units)
  {
    SourceImpl source = new SourceImpl();
    source.nuclide = nuclide;
    source.activity = activity * units.getFactor();
    return source;
  }

  public static SourceImpl fromAtoms(Nuclide nuclide, double atoms)
  {
    SourceImpl source = new SourceImpl();
    source.nuclide = nuclide;
    source.activity = nuclide.getDecayConstant() * atoms;
    return source;
  }

  private SourceImpl()
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
  public double getActivity()
  {
    return activity;
  }

}
