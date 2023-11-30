/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.nist.physics.n42.data.RadDetectorState;
import gov.nist.physics.n42.data.DoseRate;
import gov.nist.physics.n42.data.GrossCounts;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.RadMeasurement;
import gov.nist.physics.n42.data.Spectrum;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.CountRate;
import gov.nist.physics.n42.data.MeasurementClassCode;
import gov.nist.physics.n42.data.RadInstrumentState;
import gov.nist.physics.n42.data.RadItemState;
import gov.nist.physics.n42.data.RadMeasurementGroup;
import java.time.Duration;
import java.time.Instant;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "RadMeasurement",
        order = Reader.Order.SEQUENCE,
        cls = RadMeasurement.class,
        typeName = "RadMeasurementType")
@Reader.Attribute(name = "id", type = String.class, required = true)

@Reader.Attribute(name = "radMeasurementGroupReferences", type = String.class, required = false)
public class RadMeasurementReader extends ObjectReader< RadMeasurement>
{
  @Override
  public RadMeasurement start(ReaderContext context, Attributes attr) throws ReaderException
  {
    RadMeasurement out = new RadMeasurement();
    ReaderUtilities.register(context, out, attr);
    String groupReference = attr.getValue("radMeasurementGroupReferences");
    if (groupReference != null)
    {
      for (String group : groupReference.split(" "))
      {
        context.addDeferred(out, RadMeasurement::addToGroup,
                group, RadMeasurementGroup.class);
      }
    }
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {

    Reader.ReaderBuilder<RadMeasurement> builder = this.newBuilder();
    builder.element("Remark").callString(RadMeasurement::addRemark).optional().unbounded();
    //  <xsd:element ref="n42:MeasurementClassCode" minOccurs="1" maxOccurs="1"/>
    builder.element("MeasurementClassCode").call(RadMeasurement::setClassCode, MeasurementClassCode.class).required();
    //  <xsd:element ref="n42:StartDateTime" minOccurs="1" maxOccurs="1"/>
    builder.element("StartDateTime").call(RadMeasurement::setStartDateTime, Instant.class).required();
    //  <xsd:element ref="n42:RealTimeDuration" minOccurs="1" maxOccurs="1"/>
    builder.element("RealTimeDuration").call(RadMeasurement::setRealTimeDuration, Duration.class).required();
    //  <xsd:element ref="n42:Spectrum" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("Spectrum")
            .call(RadMeasurement::addSpectrum, Spectrum.class)
            .optional().unbounded();
    //  <xsd:element ref="n42:GrossCounts" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("GrossCounts")
            .call(RadMeasurement::addGrossCounts, GrossCounts.class)
            .unbounded().optional();
    //  <xsd:element ref="n42:DoseRate" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("DoseRate")
            .call(RadMeasurement::addDoseRate, DoseRate.class)
            .unbounded().optional();
    builder.element("CountRate")
            .call(RadMeasurement::addCountRate, CountRate.class)
            .unbounded().optional();
    // FIXME
    //  <xsd:element ref="n42:TotalDose" minOccurs="0" maxOccurs="unbounded"/>
    //  <xsd:element ref="n42:ExposureRate" minOccurs="0" maxOccurs="unbounded"/>
    //  <xsd:element ref="n42:TotalExposure" minOccurs="0" maxOccurs="unbounded"/>
//  <xsd:element ref="n42:RadInstrumentState" minOccurs="0" maxOccurs="1"/>
    builder.element("RadInstrumentState")
            .call(RadMeasurement::setInstrumentState, RadInstrumentState.class).optional();

    //  <xsd:element ref="n42:RadDetectorState" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("RadDetectorState")
            .call(RadMeasurement::addDetectorState, RadDetectorState.class)
            .unbounded().optional();
    //  <xsd:element ref="n42:RadItemState" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("RadItemState")
            .call(RadMeasurement::addItemState, RadItemState.class)
            .unbounded().optional();
    //  <xsd:element ref="n42:OccupancyIndicator" minOccurs="0" maxOccurs="1"/>
    builder.element("OccupancyIndicator").callBoolean(RadMeasurement::setOccupancy)
            .optional();
    //  <xsd:element ref="n42:RadMeasurementExtension" minOccurs="0" maxOccurs="unbounded"/>
    builder.reader(new ExtensionReader("RadMeasurementExtension")).nop().optional().unbounded();
    return builder.getHandlers();
  }

}
