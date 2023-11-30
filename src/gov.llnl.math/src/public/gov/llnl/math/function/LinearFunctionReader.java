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
@Reader.Declaration(pkg = MathPackage.class, name = "linear",
        cls = LinearFunction.class,
        order = Reader.Order.ALL,
        referenceable = true)
@Reader.Attribute(name = "offset", type = double.class, required = true)
@Reader.Attribute(name = "slope", type = double.class, required = true)
@Reader.AnyAttribute(processContents = Reader.ProcessContents.Skip)
public class LinearFunctionReader extends ObjectReader<LinearFunction>
{

  @Override
  public LinearFunction start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    double offset = Double.parseDouble(attributes.getValue("offset"));
    double slope = Double.parseDouble(attributes.getValue("slope"));
    return new LinearFunction(offset, slope);
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    return null;
  }
}
