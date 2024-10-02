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
import gov.nist.physics.n42.data.DoseAnalysisResults;
import gov.nist.physics.n42.data.Quantity;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "DoseAnalysisResults",
        order = Reader.Order.SEQUENCE,
        cls = DoseAnalysisResults.class,
        typeName = "DoseAnalysisResultsType")
@Reader.Attribute(name = "id", type = String.class, required = false)
public class DoseAnalysisResultsReader extends ObjectReader<DoseAnalysisResults>
{

  @Override
  public DoseAnalysisResults start(ReaderContext context, Attributes attr) throws ReaderException
  {
    DoseAnalysisResults out = new DoseAnalysisResults();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<DoseAnalysisResults> builder = this.newBuilder();
        builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("AverageDoseRateValue")
            .call(DoseAnalysisResults::setAverageDoseRate, Quantity.class)
            .optional();
    builder.element("AverageDoseRateUncertaintyValue")
            .call(DoseAnalysisResults::setAverageDoseRateUncertainty, Quantity.class)
            .optional();
    builder.element("MaximumDoseRateValue")
            .call(DoseAnalysisResults::setMaximumDoseRate, Quantity.class)
            .optional();
    builder.element("MinimumDoseRateValue")
            .call(DoseAnalysisResults::setMinimumDoseRate, Quantity.class)
            .optional();
    builder.element("BackgroundDoseRateValue")
            .call(DoseAnalysisResults::setBackgroundDoseRate, Quantity.class)
            .optional();
    builder.element("BackgroundDoseRateUncertaintyValue")
            .call(DoseAnalysisResults::setBackgroundDoseRateUncertainty, Quantity.class)
            .optional();
    builder.element("TotalDoseValue")
            .call(DoseAnalysisResults::setTotalDose, Quantity.class)
            .optional();
    //builder.element("SourcePosition").optional();
    builder.reader(new ExtensionReader("DoseAnalysisResultsExtension"))
            .call((p, v) -> p.addExtension(v))
            .optional()
            .unbounded();
    return builder.getHandlers();
  }

}

//    <xsd:sequence>
//      <xsd:element ref="n42:AverageDoseRateValue" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:AverageDoseRateUncertaintyValue" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:MaximumDoseRateValue" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:MinimumDoseRateValue" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:BackgroundDoseRateValue" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:BackgroundDoseRateUncertaintyValue" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:TotalDoseValue" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:SourcePosition" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:DoseAnalysisResultsExtension" minOccurs="0" maxOccurs="unbounded"/>
//    </xsd:sequence>
