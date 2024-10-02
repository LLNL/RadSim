/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.nist.physics.n42.N42Package;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.ComplexObject;
import gov.nist.physics.n42.data.SpectrumPeak;
import gov.nist.physics.n42.data.SpectrumPeakAnalysisResults;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "SpectrumPeakAnalysisResults",
        order = Reader.Order.SEQUENCE,
        cls = SpectrumPeakAnalysisResults.class,
        typeName = "SpectrumPeakAnalysisResultsType")
public class SpectrumPeakAnalysisResultsReader extends ObjectReader<SpectrumPeakAnalysisResults>
{

  @Override
  public SpectrumPeakAnalysisResults start(ReaderContext context, Attributes attr) throws ReaderException
  {
    SpectrumPeakAnalysisResults out = new SpectrumPeakAnalysisResults();
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<SpectrumPeakAnalysisResults> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    // <xsd:element ref="n42:SpectrumPeak" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("SpectrumPeak").call(SpectrumPeakAnalysisResults::addPeak, SpectrumPeak.class).unbounded();
    // <xsd:element ref="n42:SpectrumPeakAnalysisResultsExtension" minOccurs="0" maxOccurs="unbounded"/>
    builder.reader(new ExtensionReader("SpectrumPeakAnalysisResultsExtension"))
            .call((p, v) -> p.addExtension(v))
            .optional()
            .unbounded();
    return builder.getHandlers();
  }

}

//    <SpectrumPeakAnalysisResults>
//      <SpectrumPeak>
//        <SpectrumPeakEnergyValue>661.7</SpectrumPeakEnergyValue>
//        <SpectrumPeakNetAreaValue>1234.5</SpectrumPeakNetAreaValue>
//        <SpectrumPeakNetAreaUncertaintyValue>123.4</SpectrumPeakNetAreaUncertaintyValue>
//      </SpectrumPeak>
//      <SpectrumPeak>
//        <SpectrumPeakEnergyValue>1460.9</SpectrumPeakEnergyValue>
//        <SpectrumPeakNetAreaValue>234.5</SpectrumPeakNetAreaValue>
//        <SpectrumPeakNetAreaUncertaintyValue>23.4</SpectrumPeakNetAreaUncertaintyValue>
//      </SpectrumPeak>
//    </SpectrumPeakAnalysisResults>

