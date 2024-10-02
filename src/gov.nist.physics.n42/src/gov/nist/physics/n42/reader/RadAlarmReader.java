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
import gov.nist.physics.n42.data.ComplexObject;
import gov.nist.physics.n42.data.RadAlarm;
import gov.nist.physics.n42.data.RadAlarmCategoryCode;
import java.time.Instant;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "RadAlarm",
        order = Reader.Order.SEQUENCE,
        cls = RadAlarm.class,
        typeName = "RadAlarmType")
@Reader.Attribute(name="id", type=String.class, required=false)
@Reader.Attribute(name="radDetectorInformationReferences", type=String.class, required=true)
public class RadAlarmReader extends ObjectReader<RadAlarm>
{
  @Override
  public RadAlarm start(ReaderContext context, Attributes attr) throws ReaderException
  {
    RadAlarm out = new RadAlarm();
    ReaderUtilities.register(context, out, attr);
    ReaderUtilities.addReferences(context, out, RadAlarm::addDetector, RadDetectorInformation.class, attr.getValue("radDetectorInformationReferences"));
    return out;
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
//    <xsd:sequence>
//      <xsd:element ref="n42:RadAlarmDateTime" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:RadAlarmCategoryCode" minOccurs="1" maxOccurs="1"/>
//      <xsd:element ref="n42:RadAlarmDescription" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:AlarmAudibleIndicator" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:RadAlarmLightColor" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:RadAlarmEnergyWindowIndices" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:RadAlarmExtension" minOccurs="0" maxOccurs="unbounded"/>
//    </xsd:sequence>
//    <xsd:attribute name="radDetectorInformationReferences" type="xsd:IDREFS" use="required">
    ReaderBuilder<RadAlarm> builder = this.newBuilder();
        builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("RadAlarmDateTime").call(RadAlarm::setDateTime, Instant.class).optional();
    builder.element("RadAlarmCategoryCode").call(RadAlarm::setCategoryCode, RadAlarmCategoryCode.class);
    builder.element("RadAlarmDescription").callString(RadAlarm::setDescription).optional();
    builder.element("AlarmAudibleIndicator").callBoolean(RadAlarm::setAudibleIndicator).optional();
    builder.element("RadAlarmLightColor").callString(RadAlarm::setLightColor).optional();
    builder.element("RadAlarmEnergyWindowIndices").call(RadAlarm::setEnergyWindowIndices, int[].class).optional();
    builder.reader(new ExtensionReader("RadAlarmExtension"))
            .call((p, v) -> p.addExtension(v))
            .optional()
            .unbounded();
    return builder.getHandlers();
  }

}
