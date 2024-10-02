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
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "SpectrumPeak",
        order = Reader.Order.SEQUENCE,
        cls = SpectrumPeak.class,
        typeName = "SpectrumPeakType")
public class SpectrumPeakReader extends ObjectReader<SpectrumPeak>
{

  @Override
  public SpectrumPeak start(ReaderContext context, Attributes attr) throws ReaderException
  {
    SpectrumPeak out = new SpectrumPeak();
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<SpectrumPeak> builder = this.newBuilder();
        builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    // <xsd:element ref="n42:SpectrumPeakEnergyValue" minOccurs="1" maxOccurs="1"/>
    builder.element("SpectrumPeakEnergyValue")
            .callDouble(SpectrumPeak::setEnergy)
            .optional();
    // <xsd:element ref="n42:SpectrumPeakExpectedEnergyValue" minOccurs="0" maxOccurs="1"/>
    // <xsd:element ref="n42:SpectrumPeakFWHMValue" minOccurs="0" maxOccurs="1"/>
    // <xsd:element ref="n42:SpectrumPeakNetAreaValue" minOccurs="0" maxOccurs="1"/>
    builder.element("SpectrumPeakNetAreaValue")
            .callDouble(SpectrumPeak::setNetArea)
            .optional();
    // <xsd:element ref="n42:SpectrumPeakNetAreaUncertaintyValue" minOccurs="0" maxOccurs="1"/>
    builder.element("SpectrumPeakNetAreaUncertaintyValue")
            .callDouble(SpectrumPeak::setNetAreaUncertainty)
            .unbounded();
    // <xsd:element ref="n42:SpectrumPeakExtension" minOccurs="0" maxOccurs="1"/>
    builder.reader(new ExtensionReader("SpectrumPeakExtension"))
            .call((p, v) -> p.addExtension(v))
            .optional()
            .unbounded();
    return builder.getHandlers();
  }

}

//      <SpectrumPeak>
//        <SpectrumPeakEnergyValue>1460.9</SpectrumPeakEnergyValue>
//        <SpectrumPeakNetAreaValue>234.5</SpectrumPeakNetAreaValue>
//        <>23.4</SpectrumPeakNetAreaUncertaintyValue>
//      </SpectrumPeak>

