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
import gov.nist.physics.n42.data.RadInstrumentQualityControl;
import java.time.Instant;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "RadInstrumentQualityControl",
        order = Reader.Order.SEQUENCE,
        cls = RadInstrumentQualityControl.class,
        typeName = "RadInstrumentQualityControlType")
@Reader.Attribute(name = "id", type = String.class, required = false)
public class RadInstrumentQualityControlReader extends ObjectReader<RadInstrumentQualityControl>
{

  @Override
  public RadInstrumentQualityControl start(ReaderContext context, Attributes attr) throws ReaderException
  {
    RadInstrumentQualityControl out = new RadInstrumentQualityControl();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<RadInstrumentQualityControl> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("InspectionDateTime")
            .call(RadInstrumentQualityControl::setInspectionDateTime, Instant.class)
            .required();
    builder.element("InCalibrationIndicator")
            .callBoolean(RadInstrumentQualityControl::setInCalibrationIndicator)
            .required();
    return builder.getHandlers();
  }

//    <xsd:sequence>
//      <xsd:element ref="n42:InspectionDateTime" minOccurs="1" maxOccurs="1"/>
//      <xsd:element ref="n42:InCalibrationIndicator" minOccurs="1" maxOccurs="1"/>
//    </xsd:sequence>
}
