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
import gov.nist.physics.n42.data.GrossCountAnalysisResults;
import gov.nist.physics.n42.data.Quantity;
import gov.nist.physics.n42.data.SourcePosition;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "GrossCountAnalysisResults",
        order = Reader.Order.SEQUENCE,
        cls = GrossCountAnalysisResults.class,
        typeName = "GrossCountAnalysisResultsType")
@Reader.Attribute(name = "id", type = String.class, required = false)
public class GrossCountAnalysisResultsReader extends ObjectReader<GrossCountAnalysisResults>
{
  @Override
  public GrossCountAnalysisResults start(ReaderContext context, Attributes attr) throws ReaderException
  {
    GrossCountAnalysisResults out = new GrossCountAnalysisResults();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<GrossCountAnalysisResults> builder = this.newBuilder();
        builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    // <xsd:element ref="n42:AverageCountRateValue" minOccurs="0" maxOccurs="1"/>
    builder.element("AverageCountRateValue")
            .call(GrossCountAnalysisResults::setAverageCountRate, Quantity.class).optional();
    // <xsd:element ref="n42:AverageCountRateUncertaintyValue" minOccurs="0" maxOccurs="1"/>
    builder.element("AverageCountRateUncertaintyValue")
            .call(GrossCountAnalysisResults::setAverageCountRateUncertainty, Quantity.class).optional();
    // <xsd:element ref="n42:MaximumCountRateValue" minOccurs="0" maxOccurs="1"/>
    builder.element("MaximumCountRateValue")
            .call(GrossCountAnalysisResults::setMaximumCountRate, Quantity.class).optional();
    // <xsd:element ref="n42:MinimumCountRateValue" minOccurs="0" maxOccurs="1"/>
    builder.element("MinimumCountRateValue")
            .call(GrossCountAnalysisResults::setMinimumCountRate, Quantity.class).optional();
    // <xsd:element ref="n42:TotalCountsValue" minOccurs="0" maxOccurs="1"/>
    builder.element("TotalCountsValue")
            .call(GrossCountAnalysisResults::setTotalCounts, Quantity.class).optional();
    // <xsd:element ref="n42:BackgroundCountRateValue" minOccurs="0" maxOccurs="1"/>
    builder.element("BackgroundCountRateValue")
            .call(GrossCountAnalysisResults::setBackgroundCountRate, Quantity.class).optional();
    // <xsd:element ref="n42:BackgroundCountRateUncertaintyValue" minOccurs="0" maxOccurs="1"/>
    builder.element("BackgroundCountRateUncertaintyValue")
            .call(GrossCountAnalysisResults::setBackgroundCountRateUncertainty, Quantity.class).optional();
    // <xsd:element ref="n42:SourcePosition" minOccurs="0" maxOccurs="1"/>
    builder.element("SourcePosition").call(GrossCountAnalysisResults::setSourcePosition, SourcePosition.class).optional();  
    // <xsd:element ref="n42:GrossCountAnalysisResultsExtension" minOccurs="0" maxOccurs="unbounded"/>
    builder.reader(new ExtensionReader("GrossCountAnalysisResultsExtension"))
            .call((p, v) -> p.addExtension(v))
            .optional()
            .unbounded();
    return builder.getHandlers();
  }

}
