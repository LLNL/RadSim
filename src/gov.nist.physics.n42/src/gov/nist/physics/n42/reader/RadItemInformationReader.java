/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.RadItemInformation;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.Characteristics;
import gov.nist.physics.n42.data.ComplexObject;
import gov.nist.physics.n42.data.Quantity;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "RadItemInformation",
        order = Reader.Order.SEQUENCE,
        cls = RadItemInformation.class,
        typeName = "RadItemInformationType")
@Reader.Attribute(name = "id", type = String.class, required = true)
public class RadItemInformationReader extends ObjectReader<RadItemInformation>
{

  @Override
  public RadItemInformation start(ReaderContext context, Attributes attr) throws ReaderException
  {
    RadItemInformation out = new RadItemInformation();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    ReaderBuilder<RadItemInformation> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    // <xsd:element ref="n42:RadItemDescription" minOccurs="0" maxOccurs="1"/>
    builder.element("RadItemDescription").callString(RadItemInformation::setDescription).optional();
    // <xsd:element ref="n42:RadItemQuantity" minOccurs="0" maxOccurs="1"/> 
    builder.element("RadItemQuantity").call(RadItemInformation::setQuantity, Quantity.class).optional();
    // <xsd:element ref="n42:RadItemMeasurementGeometryDescription" minOccurs="0" maxOccurs="1"/>
    builder.element("RadItemMeasurementGeometryDescription").callString(RadItemInformation::setMeasurementGeometryDescription).optional();
    // <xsd:element ref="n42:RadItemCharacteristics" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("RadItemCharacteristics").call(RadItemInformation::addCharacteristics, Characteristics.class).optional().unbounded();
    // <xsd:element ref="n42:RadItemInformationExtension" minOccurs="0" maxOccurs="unbounded"/>
    // builder.element("RadItemInformationExtension").call(RadItemInformation::setInformationExtension, ).optional().unbounded();
    builder.reader(new ExtensionReader("RadItemInformationExtension"))
            .call((p, v) -> p.addExtension(v))
            .optional()
            .unbounded();
    return builder.getHandlers();
  }

}
