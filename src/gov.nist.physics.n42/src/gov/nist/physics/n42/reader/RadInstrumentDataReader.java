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
import gov.nist.physics.n42.data.AnalysisResults;
import gov.nist.physics.n42.data.ComplexObject;
import gov.nist.physics.n42.data.DerivedData;
import gov.nist.physics.n42.data.EfficiencyCalibration;
import gov.nist.physics.n42.data.EnergyCalibration;
import gov.nist.physics.n42.data.EnergyWindows;
import gov.nist.physics.n42.data.FWHMCalibration;
import gov.nist.physics.n42.data.RadDetectorInformation;
import gov.nist.physics.n42.data.RadInstrumentData;
import gov.nist.physics.n42.data.RadInstrumentInformation;
import gov.nist.physics.n42.data.RadItemInformation;
import gov.nist.physics.n42.data.RadMeasurement;
import gov.nist.physics.n42.data.RadMeasurementGroup;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "RadInstrumentData",
        order = Reader.Order.SEQUENCE,
        cls = RadInstrumentData.class,
        typeName = "RadInstrumentDataType")
@Reader.Attribute(name = "id", type = String.class, required = false)
@Reader.Attribute(name = "n42DocUUID", type = String.class)
@Reader.Attribute(name = "n42DocDateTime", type = String.class)
public class RadInstrumentDataReader extends ObjectReader<RadInstrumentData>
{
  @Override
  public RadInstrumentData start(ReaderContext context, Attributes attr) throws ReaderException
  {
    RadInstrumentData out = new RadInstrumentData();
    ReaderUtilities.register(context, out, attr);
    
    out.setDocUUID(attr.getValue("n42DocUUID"));
    out.setDocDateTime(attr.getValue("n42DocDateTime"));
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    // Schema taken from
    //   https://www.nist.gov/sites/default/files/documents/pml/div682/grp04/n42.xsd

    // Header
    Reader.ReaderBuilder<RadInstrumentData> builder = this.newBuilder();
        builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("RadInstrumentDataCreatorName").callString(RadInstrumentData::setDataCreatorName).optional();
    builder.element("RadInstrumentInformation")
            .call(RadInstrumentData::setInstrument, RadInstrumentInformation.class)
            .required();
    builder.element("RadDetectorInformation")
            .call(RadInstrumentData::addDetector, RadDetectorInformation.class).unbounded();
    builder.element("RadItemInformation")
            .call(RadInstrumentData::addItem, RadItemInformation.class).optional().unbounded();
    // Contents
    //<xsd:choice minOccurs="0" maxOccurs="unbounded">
    //</xsd:choice>
    ReaderBuilder<RadInstrumentData> choice = builder.group(Order.CHOICE,Option.OPTIONAL, Option.UNBOUNDED);
    //  <xsd:element ref="n42:RadMeasurement" minOccurs="0" maxOccurs="unbounded"/>
    choice.element("RadMeasurement").call(RadInstrumentData::addMeasurement, RadMeasurement.class);
    //  <xsd:element ref="n42:RadMeasurementGroup" minOccurs="0" maxOccurs="unbounded"/>
    choice.element("RadMeasurementGroup").call(RadInstrumentData::addMeasurementGroup, RadMeasurementGroup.class);
    //  <xsd:element ref="n42:EnergyCalibration" minOccurs="0" maxOccurs="unbounded"/>
    choice.element("EnergyCalibration").call(RadInstrumentData::addEnergyCalibration, EnergyCalibration.class);
    //  <xsd:element ref="n42:FWHMCalibration" minOccurs="0" maxOccurs="unbounded"/>
    choice.element("FWHMCalibration").call(RadInstrumentData::addFwhmCalibration, FWHMCalibration.class);
    //  <xsd:element ref="n42:TotalEfficiencyCalibration" minOccurs="0" maxOccurs="unbounded"/>
    choice.element("TotalEfficiencyCalibration").call(RadInstrumentData::addTotalEfficiencyCalibration, EfficiencyCalibration.class);
    //  <xsd:element ref="n42:FullEnergyPeakEfficiencyCalibration" minOccurs="0" maxOccurs="unbounded"/>
    choice.element("FullEnergyPeakEfficiencyCalibration").call(RadInstrumentData::addFullEnergyPeakEfficiencyCalibration, EfficiencyCalibration.class);
    //  <xsd:element ref="n42:IntrinsicFullEnergyPeakEfficiencyCalibration" minOccurs="0" maxOccurs="unbounded"/>
    choice.element("IntrinsicFullEnergyPeakEfficiencyCalibration").call(RadInstrumentData::addIntrinsicFullEnergyPeakEfficiencyCalibration, EfficiencyCalibration.class);
    //  <xsd:element ref="n42:IntrinsicSingleEscapePeakEfficiencyCalibration" minOccurs="0" maxOccurs="unbounded"/>
    choice.element("IntrinsicSingleEscapePeakEfficiencyCalibration").call(RadInstrumentData::addIntrinsicSingleEscapePeakEfficiencyCalibration, EfficiencyCalibration.class);
    //  <xsd:element ref="n42:IntrinsicDoubleEscapePeakEfficiencyCalibration" minOccurs="0" maxOccurs="unbounded"/>
    choice.element("IntrinsicDoubleEscapePeakEfficiencyCalibration").call(RadInstrumentData::addIntrinsicDoubleEscapePeakEfficiencyCalibration, EfficiencyCalibration.class);
    //  <xsd:element ref="n42:EnergyWindows" minOccurs="0" maxOccurs="unbounded"/>
    choice.element("EnergyWindows").call(RadInstrumentData::addEnergyWindows, EnergyWindows.class);
    // <xsd:element ref="n42:DerivedData" minOccurs="0" maxOccurs="unbounded"/>
    choice.element("DerivedData").call(RadInstrumentData::addDerivedData, DerivedData.class);
    //  <xsd:element ref="n42:AnalysisResults" minOccurs="0" maxOccurs="unbounded"/>
    choice.element("AnalysisResults").call(RadInstrumentData::addAnalysisResults, AnalysisResults.class);
    //  <xsd:element ref="n42:MultimediaData" minOccurs="0" maxOccurs="unbounded"/>
    
    // <xsd:element ref="n42:RadInstrumentDataExtension" minOccurs="0" maxOccurs="unbounded"/>
    builder.reader(new ExtensionReader("RadInstrumentDataExtension")).nop().optional().unbounded();
    return builder.getHandlers();
  }

}
