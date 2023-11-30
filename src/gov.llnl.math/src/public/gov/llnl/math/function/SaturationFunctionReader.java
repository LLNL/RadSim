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
@Reader.Declaration(pkg = MathPackage.class, name = "saturation",
        cls = SaturationFunction.class,
        order = Reader.Order.ALL, referenceable = true)
@Reader.AnyAttribute(processContents = Reader.ProcessContents.Skip)
@Reader.Attribute(name="offset", type=double.class, required=true)
@Reader.Attribute(name="gain", type=double.class, required=true)
@Reader.Attribute(name="saturation", type=double.class, required=true)
public class SaturationFunctionReader extends ObjectReader<SaturationFunction>
{

  @Override
  public SaturationFunction start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    double offset = Double.parseDouble(attributes.getValue("offset"));
    double gain = Double.parseDouble(attributes.getValue("gain"));
    double saturation = Double.parseDouble(attributes.getValue("saturation"));
    return new SaturationFunction(offset, gain, saturation);
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    return null;
  }
}
