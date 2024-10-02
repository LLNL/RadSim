/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.RadInstrumentInformation;
import gov.nist.physics.n42.data.RadInstrumentVersion;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.Characteristics;
import gov.nist.physics.n42.data.ComplexObject;
import gov.nist.physics.n42.data.RadInstrumentQualityControl;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "RadInstrumentInformation",
        order = Reader.Order.SEQUENCE,
        cls = RadInstrumentInformation.class,
        typeName = "RadInstrumentInformationType")
@Reader.Attribute(name = "id", type = String.class, required = true)
public class RadInstrumentInformationReader extends ObjectReader<RadInstrumentInformation>
{
  @Override
  public RadInstrumentInformation start(ReaderContext context, Attributes attr) throws ReaderException
  {
    RadInstrumentInformation out = new RadInstrumentInformation();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    ReaderBuilder<RadInstrumentInformation> builder = this.newBuilder();
        builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    //<xsd:element ref="n42:RadInstrumentManufacturerName" minOccurs="1" maxOccurs="1"/>
    builder.element("RadInstrumentManufacturerName").callString(RadInstrumentInformation::setManufacturerName).required();
    //<xsd:element ref="n42:RadInstrumentIdentifier" minOccurs="0" maxOccurs="1"/>
    builder.element("RadInstrumentIdentifier").callString(RadInstrumentInformation::setIdentifier).optional();
    //<xsd:element ref="n42:RadInstrumentModelName" minOccurs="1" maxOccurs="1"/>
    builder.element("RadInstrumentModelName").callString(RadInstrumentInformation::setModelName).required();
    //<xsd:element ref="n42:RadInstrumentDescription" minOccurs="0" maxOccurs="1"/>
    builder.element("RadInstrumentDescription").callString(RadInstrumentInformation::setDescription).optional();
    //<xsd:element ref="n42:RadInstrumentClassCode" minOccurs="1" maxOccurs="1"/>
    builder.element("RadInstrumentClassCode").callString(RadInstrumentInformation::setClassCode).required();
    //<xsd:element ref="n42:RadInstrumentVersion" minOccurs="1" maxOccurs="unbounded"/>
    builder.element("RadInstrumentVersion").call(RadInstrumentInformation::addVersion, RadInstrumentVersion.class).unbounded();
    //<xsd:element ref="n42:RadInstrumentQualityControl" minOccurs="0" maxOccurs="1"/>
    builder.element("RadInstrumentQualityControl").call(RadInstrumentInformation::setQualityControl, RadInstrumentQualityControl.class).optional();
    //<xsd:element ref="n42:RadInstrumentCharacteristics" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("RadInstrumentCharacteristics").call(RadInstrumentInformation::addCharacteristics, Characteristics.class).optional().unbounded();
    
    //<xsd:element ref="n42:RadInstrumentInformationExtension" minOccurs="0" maxOccurs="unbounded"/>
    builder.reader(new ExtensionReader("RadInstrumentInformationExtension"))
            .call((p, v) -> p.addExtension(v))
            .optional()
            .unbounded();
    return builder.getHandlers();
  }

}
