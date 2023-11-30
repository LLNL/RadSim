/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.Orientation;
import gov.nist.physics.n42.data.Quantity;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "Orientation",
        order = Reader.Order.SEQUENCE,
        cls = Orientation.class,
        typeName = "OrientationType")
public class OrientationReader extends ObjectReader<Orientation>
{

  @Override
  public Orientation start(ReaderContext context, Attributes attr) throws ReaderException
  {
    Orientation out = new Orientation();
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<Orientation> builder = this.newBuilder();
    builder.element("AzimuthValue").call(Orientation::setAzimuth, Quantity.class).required();
    builder.element("InclinationValue").call(Orientation::setInclination, Quantity.class).optional();
    builder.element("RollValue").call(Orientation::setRoll, Quantity.class).optional();
    return builder.getHandlers();
  }

}
