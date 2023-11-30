/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.RadDetectorInformation;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.Quantity;
import gov.nist.physics.n42.data.Characteristics;
import gov.nist.physics.n42.data.ComplexObject;
import java.time.Instant;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "RadDetectorInformation",
        order = Reader.Order.SEQUENCE,
        cls = RadDetectorInformation.class,
        typeName = "RadDetectorInformationType")
@Reader.Attribute(name = "id", type = String.class, required = true)
public class RadDetectorInformationReader extends ObjectReader<RadDetectorInformation>
{
  @Override
  public RadDetectorInformation start(ReaderContext context, Attributes attr) throws ReaderException
  {
    RadDetectorInformation out = new RadDetectorInformation();
    ReaderUtilities.register(context, out, attr);
    String n42DocDateTime = attr.getValue("n42DocDateTime");
    if (n42DocDateTime != null)
    {
      Instant time = Instant.parse(n42DocDateTime);
      out.setN42DocDateTime(time);
    }
    return out;
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    ReaderBuilder<RadDetectorInformation> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    //  <xsd:element ref="n42:RadDetectorName" minOccurs="0" maxOccurs="1"/>
    builder.element("RadDetectorName").callString(RadDetectorInformation::setName).optional();
    //  <xsd:element ref="n42:RadDetectorCategoryCode" minOccurs="1" maxOccurs="1"/>
    builder.element("RadDetectorCategoryCode").callString(RadDetectorInformation::setCategoryCode).required();
    //  <xsd:element ref="n42:RadDetectorKindCode" minOccurs="1" maxOccurs="1"/>
    builder.element("RadDetectorKindCode").callString(RadDetectorInformation::setKindCode).required();
    //  <xsd:element ref="n42:RadDetectorDescription" minOccurs="0" maxOccurs="1"/>
    builder.element("RadDetectorDescription").callString(RadDetectorInformation::setDescription).optional();

    //  <xsd:element ref="n42:RadDetectorLengthValue" minOccurs="0" maxOccurs="1"/>
    builder.element("RadDetectorLengthValue")
            .call(RadDetectorInformation::setLength, Quantity.class).optional();
    //  <xsd:element ref="n42:RadDetectorWidthValue" minOccurs="0" maxOccurs="1"/>
    builder.element("RadDetectorWidthValue")
            .call(RadDetectorInformation::setWidth, Quantity.class).optional();
    //  <xsd:element ref="n42:RadDetectorDepthValue" minOccurs="0" maxOccurs="1"/>
    builder.element("RadDetectorDepthValue")
            .call(RadDetectorInformation::setDepth, Quantity.class).optional();
    //  <xsd:element ref="n42:RadDetectorDiameterValue" minOccurs="0" maxOccurs="1"/>
    builder.element("RadDetectorDiameterValue")
            .call(RadDetectorInformation::setDiameter, Quantity.class).optional();
    //  <xsd:element ref="n42:RadDetectorVolumeValue" minOccurs="0" maxOccurs="1"/>
    builder.element("RadDetectorVolumeValue")
            .call(RadDetectorInformation::setVolume, Quantity.class).optional();
    //  <xsd:element ref="n42:RadDetectorCharacteristics" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("RadDetectorCharacteristics")
            .call(RadDetectorInformation::addCharacteristics, Characteristics.class).optional().unbounded();
    //  <xsd:element ref="n42:RadDetectorInformationExtension" minOccurs="0" maxOccurs="unbounded"/>
    builder.reader(new ExtensionReader("RadDetectorInformationExtension")).nop().optional().unbounded();
    return builder.getHandlers();
  }

}
