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
import gov.nist.physics.n42.data.AnalysisAlgorithmVersion;
import gov.nist.physics.n42.data.ComplexObject;
import org.xml.sax.Attributes;

/**
 *
 * @author monterial1
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "AnalysisAlgorithmVersion",
        order = Reader.Order.SEQUENCE,
        cls = AnalysisAlgorithmVersion.class,
        typeName = "AnalysisAlgorithmVersionType")
@Reader.Attribute(name = "id", type = String.class, required = false)
public class AnalysisAlgorithmVersionReader extends ObjectReader<AnalysisAlgorithmVersion>
{

  @Override
  public AnalysisAlgorithmVersion start(ReaderContext context, Attributes attr) throws ReaderException
  {
    AnalysisAlgorithmVersion out = new AnalysisAlgorithmVersion();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    //<xsd:sequence>
    //  <xsd:element ref="n42:AnalysisAlgorithmComponentName" minOccurs="1" maxOccurs="1"/>
    //  <xsd:element ref="n42:AnalysisAlgorithmComponentVersion" minOccurs="1" maxOccurs="1"/>
    //</xsd:sequence>

    Reader.ReaderBuilder<AnalysisAlgorithmVersion> builder = this.newBuilder();
        builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("AnalysisAlgorithmComponentName").callString(AnalysisAlgorithmVersion::setComponentName).required();
    builder.element("AnalysisAlgorithmComponentVersion").callString(AnalysisAlgorithmVersion::setComponentVersion).required();
    return builder.getHandlers();
  }

}
