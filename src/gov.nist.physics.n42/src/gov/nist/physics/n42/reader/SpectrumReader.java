/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.EnergyCalibration;
import gov.nist.physics.n42.data.RadDetectorInformation;
import gov.nist.physics.n42.data.Spectrum;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.ComplexObject;
import gov.nist.physics.n42.data.EfficiencyCalibration;
import gov.nist.physics.n42.data.FWHMCalibration;
import java.time.Duration;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "Spectrum",
        order = Reader.Order.SEQUENCE,
        cls = Spectrum.class,
        typeName = "SpectrumType")
@Reader.Attribute(name = "id", type = String.class, required = true)
@Reader.Attribute(name = "radDetectorInformationReference", type = String.class, required = false)
@Reader.Attribute(name = "energyCalibrationReference", type = String.class, required = true)
@Reader.Attribute(name = "FWHMCalibrationReference", type = String.class, required = false)
@Reader.Attribute(name = "fullEnergyPeakEfficiencyCalibrationReference", type = String.class, required = false)
@Reader.Attribute(name = "intrinsicSingleEscapePeakEfficiencyCalibrationReference", type = String.class, required = false)
@Reader.Attribute(name = "intrinsicDoubleEscapePeakEfficiencyCalibrationReference", type = String.class, required = false)
@Reader.Attribute(name = "intrinsicFullEnergyPeakEfficiencyCalibrationReference", type = String.class, required = false)
@Reader.Attribute(name = "radRawSpectrumReferences", type = String.class, required = false)
public class SpectrumReader extends ObjectReader<Spectrum>
{

  @Override
  public Spectrum start(ReaderContext context, Attributes attr) throws ReaderException
  {
    Spectrum out = new Spectrum();
    ReaderUtilities.register(context, out, attr);
    // References may be declared after the first usage.
    context.addDeferred(out, Spectrum::setDetector,
            attr.getValue("radDetectorInformationReference"), RadDetectorInformation.class);
    context.addDeferred(out, Spectrum::setEnergyCalibration,
            attr.getValue("energyCalibrationReference"), EnergyCalibration.class);
    context.addDeferred(out, Spectrum::setFWHMEfficiencyCalibration,
            attr.getValue("FWHMCalibrationReference"), FWHMCalibration.class);
    context.addDeferred(out, Spectrum::setFullEnergyPeakEfficiencyCalibration,
            attr.getValue("fullEnergyPeakEfficiencyCalibrationReference"), EfficiencyCalibration.class);
    context.addDeferred(out, Spectrum::setIntrinsicSingleEscapePeakEfficiencyCalibration,
            attr.getValue("intrinsicSingleEscapePeakEfficiencyCalibrationReference"), EfficiencyCalibration.class);
    context.addDeferred(out, Spectrum::setIntrinsicDoubleEscapePeakEfficiencyCalibration,
            attr.getValue("intrinsicDoubleEscapePeakEfficiencyCalibrationReference"), EfficiencyCalibration.class);
    context.addDeferred(out, Spectrum::setIntrinsicFullEnergyPeakEfficiencyCalibration,
            attr.getValue("intrinsicFullEnergyPeakEfficiencyCalibrationReference"), EfficiencyCalibration.class);
//@Reader.Attribute(name="radRawSpectrumReferences", type=String.class, required=false)
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<Spectrum> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    // <xsd:element ref="n42:LiveTimeDuration" minOccurs="1" maxOccurs="1"/>
    builder.element("LiveTimeDuration").call(Spectrum::setLiveTimeDuration, Duration.class);
    // <xsd:element ref="n42:ChannelData" minOccurs="1" maxOccurs="1"/>
    builder.reader(new ChannelDataReader()).call(Spectrum::setCountData);
    // <xsd:element ref="n42:SpectrumExtension" minOccurs="0" maxOccurs="unbounded"/>
    builder.reader(new ExtensionReader("SpectrumExtension"))
            .call((p, v) -> p.addExtension(v))
            .optional()
            .unbounded();
    return builder.getHandlers();
  }

}

//    <xsd:attribute name="energyCalibrationReference" type="xsd:IDREF" use="required">
//      <xsd:annotation>
//        <xsd:documentation>Identifies the EnergyCalibration element within the N42 XML document that applies to a particular spectrum.</xsd:documentation>
//      </xsd:annotation>
//    </xsd:attribute>
//    <xsd:attribute name="intrinsicSingleEscapePeakEfficiencyCalibrationReference" type="xsd:IDREF" use="optional">
//      <xsd:annotation>
//        <xsd:documentation>Identifies the IntrinsicSingleEscapePeakEfficiencyCalibration element within the N42 XML document that applies to this spectrum.</xsd:documentation>
//      </xsd:annotation>
//    </xsd:attribute>
//    <xsd:attribute name="FWHMCalibrationReference" type="xsd:IDREF" use="optional">
//      <xsd:annotation>
//        <xsd:documentation>Identifies the FWHMCalibration element within the N42 XML document that applies to a particular spectrum.</xsd:documentation>
//      </xsd:annotation>
//    </xsd:attribute>
//    <xsd:attribute name="intrinsicDoubleEscapePeakEfficiencyCalibrationReference" type="xsd:IDREF" use="optional">
//      <xsd:annotation>
//        <xsd:documentation>Identifies the IntrinsicDoubleEscapePeakEfficiencyCalibration element within the N42 XML document that applies to a particular spectrum.</xsd:documentation>
//      </xsd:annotation>
//    </xsd:attribute>
//    <xsd:attribute name="intrinsicFullEnergyPeakEfficiencyCalibrationReference" type="xsd:IDREF" use="optional">
//      <xsd:annotation>
//        <xsd:documentation>Identifies the IntrinsicFullEnergyPeakEfficiencyCalibration element within the N42 XML document that applies to a particular spectrum.</xsd:documentation>
//      </xsd:annotation>
//    </xsd:attribute>
//    <xsd:attribute name="fullEnergyPeakEfficiencyCalibrationReference" type="xsd:IDREF" use="optional">
//      <xsd:annotation>
//        <xsd:documentation>Identifies the FullEnergyPeakEfficiencyCalibration element within the N42 XML document that applies to a particular spectrum.</xsd:documentation>
//      </xsd:annotation>
//    </xsd:attribute>
//    <xsd:attribute name="radDetectorInformationReference" type="xsd:IDREF" use="optional">
//      <xsd:annotation>
//        <xsd:documentation>Reference to the radiation detector that was used to collect these data.</xsd:documentation>
//      </xsd:annotation>
//    </xsd:attribute>
//    <xsd:attribute name="radRawSpectrumReferences" type="xsd:IDREFS" use="optional">
//      <xsd:annotation>
//        <xsd:documentation>Identifies the SpectrumMeasurement data element(s) used to produce derived data. There shall be no duplicate IDREF values in the list. This attribute is required whenever the element is used within a DerivedData block, and is prohibited otherwise.</xsd:documentation>
//      </xsd:annotation>
//    </xsd:attribute>
