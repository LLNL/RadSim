/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.llnl.rtk.physics;

/**
 *
 * @author nelson85
 */
public class EmissionScaled
{
  public static Emission scaled(Emission e, double f)
  {
    if (e instanceof Alpha)
      return new ScaledAlpha((Alpha) e, f);
    if (e instanceof Beta)
      return new ScaledBeta((Beta) e, f);
    if (e instanceof Gamma)
      return new ScaledGamma((Gamma) e, f);
    if (e instanceof ElectronCapture)
      return new ScaledCapture((ElectronCapture) e, f);
    if (e instanceof Positron)
      return new ScaledPositron((Positron) e, f);
    if (e instanceof Xray)
      return new ScaledXray((Xray) e, f);
    throw new UnsupportedOperationException("Unable to scale particle of type " + e.getClass());
  }

  public static class ScaledAlpha implements Alpha
  {
    Alpha parent;
    double scalar;

    private ScaledAlpha(Alpha e, double f)
    {
      this.parent = e;
      this.scalar = f;
    }

    @Override
    public Quantity getEnergy()
    {
      return parent.getEnergy();
    }

    @Override
    public Quantity getIntensity()
    {
      return parent.getIntensity().scaled(scalar);
    }

    @Override
    public Quantity getHindrance()
    {
      return parent.getHindrance();
    }

    @Override
    public Transition getOrigin()
    {
      return parent.getOrigin();
    }

  }

  public static class ScaledBeta implements Beta
  {
    Beta parent;
    double scalar;

    private ScaledBeta(Beta e, double f)
    {
      this.parent = e;
      this.scalar = f;
    }

    @Override
    public Quantity getEnergy()
    {
      return parent.getEnergy();
    }

    @Override
    public Quantity getIntensity()
    {
      return parent.getIntensity().scaled(scalar);
    }

    @Override
    public Quantity getLogFT()
    {
      return parent.getLogFT();
    }

    @Override
    public String getForbiddenness()
    {
      return parent.getForbiddenness();
    }

    @Override
    public Transition getOrigin()
    {
      return parent.getOrigin();
    }

  }

  private static class ScaledPositron implements Positron
  {
    Positron parent;
    double scalar;

    public ScaledPositron(Positron e, double f)
    {
      this.parent = e;
      this.scalar = f;
    }

    @Override
    public Quantity getEnergy()
    {
      return parent.getEnergy();
    }

    @Override
    public Quantity getIntensity()
    {
      return parent.getIntensity().scaled(scalar);
    }

    @Override
    public Quantity getLogFT()
    {
      return parent.getLogFT();
    }

    @Override
    public Transition getOrigin()
    {
      return parent.getOrigin();
    }

    @Override
    public String getForbiddenness()
    {
      return parent.getForbiddenness();
    }
  }

  private static class ScaledCapture implements ElectronCapture
  {
    private final ElectronCapture parent;
    private final double scalar;

    public ScaledCapture(ElectronCapture e, double f)
    {
      this.parent = e;
      this.scalar = f;
    }

    @Override
    public Quantity getIntensity()
    {
      return parent.getIntensity().scaled(scalar);
    }

    @Override
    public Quantity getLogFT()
    {
      return parent.getLogFT();
    }

    @Override
    public Transition getOrigin()
    {
      return parent.getOrigin();
    }

    @Override
    public String getForbiddenness()
    {
      return parent.getForbiddenness();
    }

  }

  private static class ScaledGamma implements Gamma
  {
    private final Gamma parent;
    private final double scalar;

    public ScaledGamma(Gamma e, double f)
    {
      this.parent = e;
      this.scalar = f;
    }

    @Override
    public Quantity getEnergy()
    {
      return parent.getEnergy();
    }

    @Override
    public Quantity getIntensity()
    {
      return parent.getIntensity().scaled(scalar);
    }

    @Override
    public Transition getOrigin()
    {
      return parent.getOrigin();
    }
  }

  private static class ScaledXray implements Xray
  {
    private final Xray parent;
    private final double scalar;

    public ScaledXray(Xray e, double f)
    {
      this.parent = e;
      this.scalar = f;
    }

    @Override
    public String getName()
    {
      return parent.getName();
    }
    @Override
    public Quantity getEnergy()
    {
      return parent.getEnergy();
    }

    @Override
    public Quantity getIntensity()
    {
      return parent.getIntensity().scaled(scalar);
    }

    @Override
    public Transition getOrigin()
    {
      return parent.getOrigin();
    }
  }
}
