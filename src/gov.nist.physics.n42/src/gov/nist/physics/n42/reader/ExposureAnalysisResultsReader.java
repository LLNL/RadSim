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
import gov.nist.physics.n42.data.ExposureAnalysisResults;
import gov.nist.physics.n42.data.Quantity;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "ExposureAnalysisResults",
        order = Reader.Order.SEQUENCE,
        cls = ExposureAnalysisResults.class,
        typeName = "ExposureAnalysisResultsType")
@Reader.Attribute(name="id", type=String.class, required=false)
public class ExposureAnalysisResultsReader extends ObjectReader<ExposureAnalysisResults>
{
  @Override
  public ExposureAnalysisResults start(ReaderContext context, Attributes attr) throws ReaderException
  {
    ExposureAnalysisResults out = new ExposureAnalysisResults();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<ExposureAnalysisResults> builder = this.newBuilder();
        builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("AverageExposureRateValue")
            .call(ExposureAnalysisResults::setAverageExposureRate, Quantity.class).optional();
    builder.element("AverageExposureRateUncertaintyValue")
            .call(ExposureAnalysisResults::setAverageExposureRateUncertainty, Quantity.class).optional();
    builder.element("MaximumExposureRateValue")
            .call(ExposureAnalysisResults::setMaximumExposureRate, Quantity.class).optional();
    builder.element("MinimumExposureRateValue")
            .call(ExposureAnalysisResults::setMinimumExposureRate, Quantity.class).optional();
    builder.element("BackgroundExposureRateValue")
            .call(ExposureAnalysisResults::setBackgroundExposureRate, Quantity.class).optional();
    builder.element("BackgroundExposureRateUncertaintyValue")
            .call(ExposureAnalysisResults::setBackgroundExposureRateUncertainty, Quantity.class).optional();
    builder.element("TotalExposureValue")
            .call(ExposureAnalysisResults::setTotalExposure, Quantity.class).optional();
//builder.element("SourcePosition" minOccurs="0" maxOccurs="1"/>
//builder.element("ExposureAnalysisResultsExtension" minOccurs="0" maxOccurs="unbounded"/>
    builder.reader(new ExtensionReader("ExposureAnalysisResultsExtension"))
            .call((p, v) -> p.addExtension(v))
            .optional()
            .unbounded();
    return builder.getHandlers();
  }

}

//      <xsd:element ref="n42:AverageExposureRateValue" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:AverageExposureRateUncertaintyValue" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:MaximumExposureRateValue" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:MinimumExposureRateValue" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:BackgroundExposureRateValue" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:BackgroundExposureRateUncertaintyValue" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:TotalExposureValue" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:SourcePosition" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:ExposureAnalysisResultsExtension" minOccurs="0" maxOccurs="unbounded"/>
