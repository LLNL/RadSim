/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.distribution;

import gov.llnl.math.MathPackage;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.PolymorphicReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = MathPackage.class, name = "distribution",
        cls = Distribution.class,
        referenceable = true)
public class DistributionReader extends PolymorphicReader<Distribution>
{

  @Override
  @SuppressWarnings("unchecked")
  public ObjectReader<? extends Distribution>[] getReaders() throws ReaderException
  {
    return group(
            new GumbelDistributionReader(),
            new ChiSquaredDistributionReader(),
            new GammaDistributionReader(),
            new PoissonDistributionReader(),
            new NormalDistributionReader());
  }

//<editor-fold desc="individual readers">
  static abstract class DistributionReaderBase<Obj> extends ObjectReader<Obj>
  {
//    public DistributionReaderBase(String name)
//    {
//      super(Order.ALL, Options.Attr.ANY, name, MathPackage.getInstance());
//    }

    @Override
    public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
    {
      return null;
    }
  }

  @Reader.Declaration(pkg = MathPackage.class, name = "gumbel", referenceable = true, cls = GumbelDistribution.class)
  @Reader.Attribute(name = "mu", type = double.class, required = true)
  @Reader.Attribute(name = "beta", type = double.class, required = true)
  public static class GumbelDistributionReader extends DistributionReaderBase<GumbelDistribution>
  {

    @Override
    public GumbelDistribution start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      try
      {
        double mu = Double.parseDouble(attributes.getValue("mu").trim());
        double beta = Double.parseDouble(attributes.getValue("beta").trim());
        return new GumbelDistribution(mu, beta);
      }
      catch (NumberFormatException ex)
      {
        throw new ReaderException(ex);
      }
    }

  }

  @Reader.Declaration(pkg = MathPackage.class, name = "chiSquared",
          cls = ChiSquaredDistribution.class,
          referenceable = true)
  @Reader.Attribute(name = "df", type = double.class, required = true)
  public static class ChiSquaredDistributionReader extends DistributionReaderBase<ChiSquaredDistribution>
  {

    @Override
    public ChiSquaredDistribution start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      try
      {
        double df = Double.parseDouble(attributes.getValue("df").trim());
        return new ChiSquaredDistribution(df);
      }
      catch (NumberFormatException ex)
      {
        throw new ReaderException(ex);
      }
    }
  }

  @Reader.Declaration(pkg = MathPackage.class, name = "gamma",
          cls = GammaDistribution.class,
          referenceable = true)
  @Reader.Attribute(name = "shape", type = double.class, required = true)
  @Reader.Attribute(name = "scale", type = double.class, required = true)
  public static class GammaDistributionReader extends DistributionReaderBase<GammaDistribution>
  {
    @Override
    public GammaDistribution start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      try
      {
        double shape = Double.parseDouble(attributes.getValue("shape").trim());
        double scale = Double.parseDouble(attributes.getValue("scale").trim());
        return new GammaDistribution(shape, scale);
      }
      catch (NumberFormatException ex)
      {
        throw new ReaderException(ex);
      }
    }

  }

  @Reader.Declaration(pkg = MathPackage.class, name = "normal",
          cls = NormalDistribution.class,
          referenceable = true)
  @Reader.Attribute(name = "mean", type = double.class, required = true)
  @Reader.Attribute(name = "var", type = double.class, required = true)
  public static class NormalDistributionReader extends DistributionReaderBase<NormalDistribution>
  {

    @Override
    public NormalDistribution start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      try
      {
        double mean = Double.parseDouble(attributes.getValue("mean").trim());
        double var = Double.parseDouble(attributes.getValue("var").trim());
        return new NormalDistribution(mean, var);
      }
      catch (NumberFormatException ex)
      {
        throw new ReaderException(ex);
      }
    }

  }

  @Reader.Declaration(pkg = MathPackage.class, name = "poisson",
          cls = PoissonDistribution.class,
          referenceable = true)
  @Reader.Attribute(name = "lambda", type = double.class, required = true)
  public static class PoissonDistributionReader extends DistributionReaderBase<PoissonDistribution>
  {

    @Override
    public PoissonDistribution start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      try
      {
        double lambda = Double.parseDouble(attributes.getValue("lambda").trim());
        return new PoissonDistribution(lambda);
      }
      catch (NumberFormatException ex)
      {
        throw new ReaderException(ex);
      }
    }
  }
//</editor-fold>
}
