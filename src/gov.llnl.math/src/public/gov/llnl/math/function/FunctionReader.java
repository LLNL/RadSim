/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.function;

import gov.llnl.math.MathPackage;
import gov.llnl.math.spline.CubicHermiteSplineReader;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.PolymorphicReader;
import gov.llnl.utility.xml.bind.Reader;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = MathPackage.class, name = "function",
        cls = Function.class,
        referenceable = true)
public class FunctionReader extends PolymorphicReader<Function>
{

  @Override
  @SuppressWarnings("unchecked")
  public ObjectReader<? extends Function>[] getReaders() throws ReaderException
  {
    return group(
            new LinearFunctionReader(),
            new QuadraticFunctionReader(),
            new SaturationFunctionReader(),
            new CubicHermiteSplineReader());
  }
}
