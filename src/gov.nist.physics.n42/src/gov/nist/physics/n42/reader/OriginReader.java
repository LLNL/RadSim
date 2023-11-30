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
import gov.nist.physics.n42.data.GeographicPoint;
import gov.nist.physics.n42.data.Origin;
import gov.nist.physics.n42.data.OriginReference;
import org.xml.sax.Attributes;

/**
 *
 * @author pham21
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "Origin",
        order = Reader.Order.SEQUENCE,
        cls = Origin.class,
        typeName = "OriginType")
@Reader.Attribute(name = "originReference", type = String.class)
public class OriginReader extends ObjectReader<Origin>
{
  @Override
  public Origin start(ReaderContext context, Attributes attr) throws ReaderException
  {
    Origin out = new Origin();
    String originReference = attr.getValue("originReference");
    if (originReference != null)
    {
      out.setReference(context.get(originReference, OriginReference.class));
    }
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<Origin> builder = this.newBuilder();
    // <xsd:element ref="n42:GeographicPoint" minOccurs="0" maxOccurs="1"/>
    builder.element("GeographicPoint").call(Origin::setGeographicPoint, GeographicPoint.class).optional();
    // <xsd:element ref="n42:OriginDescription" minOccurs="0" maxOccurs="1"/>
    builder.element("OriginDescription").callString(Origin::setDescription).optional();
    return builder.getHandlers();
  }

}
