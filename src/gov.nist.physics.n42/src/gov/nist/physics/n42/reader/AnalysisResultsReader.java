/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.nist.physics.n42.data.NuclideAnalysisResults;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.AnalysisAlgorithmSetting;
import gov.nist.physics.n42.data.AnalysisAlgorithmVersion;
import gov.nist.physics.n42.data.AnalysisResultStatusCode;
import gov.nist.physics.n42.data.AnalysisResults;
import gov.nist.physics.n42.data.ComplexObject;
import gov.nist.physics.n42.data.DerivedData;
import gov.nist.physics.n42.data.DoseAnalysisResults;
import gov.nist.physics.n42.data.ExposureAnalysisResults;
import gov.nist.physics.n42.data.Fault;
import gov.nist.physics.n42.data.GrossCountAnalysisResults;
import gov.nist.physics.n42.data.RadAlarm;
import gov.nist.physics.n42.data.RadMeasurement;
import gov.nist.physics.n42.data.RadMeasurementGroup;
import gov.nist.physics.n42.data.Spectrum;
import gov.nist.physics.n42.data.SpectrumPeakAnalysisResults;
import java.time.Instant;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "AnalysisResults",
        order = Reader.Order.SEQUENCE,
        cls = AnalysisResults.class,
        typeName = "AnalysisResultsType")
@Reader.Attribute(name = "id", type = String.class, required = false)

@Reader.Attribute(name = "radMeasurementReferences", type = String.class, required = false)


@Reader.Attribute(name = "radMeasurementGroupReferences", type = String.class, required = false)


@Reader.Attribute(name = "derivedDataReferences", type = String.class, required = false)
public class AnalysisResultsReader extends ObjectReader<AnalysisResults>
{
  @Override
  public AnalysisResults start(ReaderContext context, Attributes attr) throws ReaderException
  {
    AnalysisResults out = new AnalysisResults();
    ReaderUtilities.register(context, out, attr);
    ReaderUtilities.addReferences(context, out, AnalysisResults::addToMeasurement, RadMeasurement.class, attr.getValue("radMeasurementReferences"));
    ReaderUtilities.addReferences(context, out, AnalysisResults::addToGroup, RadMeasurementGroup.class, attr.getValue("radMeasurementGroupReferences"));
    ReaderUtilities.addReferences(context, out, AnalysisResults::addToReferences, DerivedData.class, attr.getValue("derivedDataReferences"));
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<AnalysisResults> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    //<xsd:element ref="n42:AnalysisStartDateTime" minOccurs="0" maxOccurs="1"/>
    builder.element("AnalysisStartDateTime")
            .call(AnalysisResults::setStartDateTime, Instant.class).optional();
    //  <xsd:element ref="n42:AnalysisComputationDuration" minOccurs="0" maxOccurs="1"/>
    //  <xsd:element ref="n42:AnalysisAlgorithmName" minOccurs="0" maxOccurs="1"/>
    builder.element("AnalysisAlgorithmName")
            .callString(AnalysisResults::setAlgorithmName).optional();
    //  <xsd:element ref="n42:AnalysisAlgorithmCreatorName" minOccurs="0" maxOccurs="1"/>
    builder.element("AnalysisAlgorithmCreatorName")
            .callString(AnalysisResults::setAlgorithmCreatorName).optional();
    //  <xsd:element ref="n42:AnalysisAlgorithmDescription" minOccurs="0" maxOccurs="1"/>
    //  <xsd:element ref="n42:AnalysisAlgorithmVersion" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("AnalysisAlgorithmVersion")
            .call(AnalysisResults::addAlgorithmVersion, AnalysisAlgorithmVersion.class)
            .optional()
            .unbounded();
    //  <xsd:element ref="n42:AnalysisAlgorithmSetting" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("AnalysisAlgorithmSetting")
            .call(AnalysisResults::addAlgorithmSetting, AnalysisAlgorithmSetting.class)
            .optional()
            .unbounded();
    //  <xsd:element ref="n42:AnalysisResultStatusCode" minOccurs="0" maxOccurs="1"/>
    builder.element("AnalysisResultStatusCode").call(AnalysisResults::setStatusCode, AnalysisResultStatusCode.class).optional();
    //  <xsd:element ref="n42:AnalysisConfidenceValue" minOccurs="0" maxOccurs="1"/>
    builder.element("AnalysisConfidenceValue").callDouble(AnalysisResults::setConfidence).optional();
    //  <xsd:element ref="n42:AnalysisResultDescription" minOccurs="0" maxOccurs="1"/>
    builder.element("AnalysisResultDescription").callString(AnalysisResults::setDescription).optional();
    //  <xsd:element ref="n42:RadAlarm" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("RadAlarm")
            .call(AnalysisResults::addRadAlarm, RadAlarm.class)
            .optional().unbounded();
    //  <xsd:element ref="n42:NuclideAnalysisResults" minOccurs="0" maxOccurs="1"/>
    builder.element("NuclideAnalysisResults")
            .call(AnalysisResults::setNuclideAnalysisResults, NuclideAnalysisResults.class)
            .optional();
    //  <xsd:element ref="n42:SpectrumPeakAnalysisResults" minOccurs="0" maxOccurs="1"/>
    builder.element("SpectrumPeakAnalysisResults")
            .call(AnalysisResults::setSpectrumPeakAnalysisResults, SpectrumPeakAnalysisResults.class)
            .optional();
    //  <xsd:element ref="n42:GrossCountAnalysisResults" minOccurs="0" maxOccurs="1"/>
    builder.element("GrossCountAnalysisResults")
            .call(AnalysisResults::setGrossCountAnalysisResults, GrossCountAnalysisResults.class)
            .optional();
    //  <xsd:element ref="n42:DoseAnalysisResults" minOccurs="0" maxOccurs="1"/>
    builder.element("DoseAnalysisResults")
            .call(AnalysisResults::setDoseAnalysisResults, DoseAnalysisResults.class)
            .optional();
    //  <xsd:element ref="n42:ExposureAnalysisResults" minOccurs="0" maxOccurs="1"/>
    builder.element("ExposureAnalysisResults")
            .call(AnalysisResults::setExposureAnalysisResults, ExposureAnalysisResults.class)
            .optional();
    //  <xsd:element ref="n42:Fault" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("Fault")
            .call(AnalysisResults::addFault, Fault.class)
            .optional().unbounded();
    //  <xsd:element ref="n42:AnalysisResultsExtension" minOccurs="0" maxOccurs="unbounded"/>
    builder.reader(new ExtensionReader("AnalysisResultsExtension"))
            .call((p, v) -> p.addExtension(v))
            .optional()
            .unbounded();
    return builder.getHandlers();
  }

}

//  <AnalysisResults radMeasurementReferences="M113">
//    <NuclideAnalysisResults>
//      <Nuclide>
//        <NuclideIdentifiedIndicator>true</NuclideIdentifiedIndicator>
//        <NuclideName>K-40</NuclideName>
//        <NuclideIDConfidenceValue>0.99</NuclideIDConfidenceValue>
//      </Nuclide>
//    </NuclideAnalysisResults>
//  </AnalysisResults>

