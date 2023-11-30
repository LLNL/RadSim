/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.function;

import gov.llnl.math.MathPackage;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = MathPackage.class, name = "quadratic",
        order = Reader.Order.ALL,
        cls = QuadraticFunction.class,
        referenceable = true)
@Reader.Attribute(name="offset", type=double.class, required=true)
@Reader.Attribute(name="slope", type=double.class, required=true)
@Reader.Attribute(name="accel", type=double.class, required=true)
@Reader.AnyAttribute(processContents = Reader.ProcessContents.Skip)
public class QuadraticFunctionReader extends ObjectReader<QuadraticFunction>
{

  @Override
  public QuadraticFunction start(ReaderContext context, Attributes attributes) throws ReaderException
  {
        double offset = Double.parseDouble(attributes.getValue("offset"));
    double slope = Double.parseDouble(attributes.getValue("slope"));
    double accel = Double.parseDouble(attributes.getValue("accel"));
    return new QuadraticFunction(offset, slope, accel);
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    return null;
  }
}
