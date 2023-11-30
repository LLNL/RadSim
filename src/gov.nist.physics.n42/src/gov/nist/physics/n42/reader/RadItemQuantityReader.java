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
import gov.nist.physics.n42.data.ComplexObject;
import gov.nist.physics.n42.data.RadItemQuantity;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "RadItemQuantity",
        order = Reader.Order.SEQUENCE,
        cls = RadItemQuantity.class,
        typeName = "RadItemQuantityType")
@Reader.Attribute(name = "id", type = String.class, required = true)
public class RadItemQuantityReader extends ObjectReader<RadItemQuantity>
{

  @Override
  public RadItemQuantity start(ReaderContext context, Attributes attr) throws ReaderException
  {
    RadItemQuantity out = new RadItemQuantity();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    ReaderBuilder<RadItemQuantity> builder = this.newBuilder();
        builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    // <xsd:element ref="n42:RadItemQuantityValue" minOccurs="1" maxOccurs="1"/>
    builder.element("RadItemQuantityValue").callDouble(RadItemQuantity::setValue).required();
    // <xsd:element ref="n42:RadItemQuantityUncertaintyValue" minOccurs="0" maxOccurs="1"/>
    builder.element("RadItemQuantityUncertaintyValue").callDouble(RadItemQuantity::setUncertainty).optional();
    // <xsd:element ref="n42:RadItemQuantityUnits" minOccurs="1" maxOccurs="1"/>
    builder.element("RadItemQuantityUnits").callString(RadItemQuantity::setUnits).required();
    return builder.getHandlers();
  }

}
