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
import gov.nist.physics.n42.data.Nuclide;
import gov.nist.physics.n42.data.NuclideAnalysisResults;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "NuclideAnalysisResults",
        order = Reader.Order.SEQUENCE,
        cls = NuclideAnalysisResults.class,
        typeName = "NuclideAnalysisResultsType")
@Reader.Attribute(name = "id", type = String.class, required = false)
public class NuclideAnalysisResultsReader extends ObjectReader<NuclideAnalysisResults>
{
  @Override
  public NuclideAnalysisResults start(ReaderContext context, Attributes attr) throws ReaderException
  {
    NuclideAnalysisResults out = new NuclideAnalysisResults();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<NuclideAnalysisResults> builder = this.newBuilder();
        builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("Nuclide")
            .call(NuclideAnalysisResults::addNuclide, Nuclide.class).optional()
            .unbounded();
    //   <xsd:element ref="n42:NuclideAnalysisReducedChiSquareValue" minOccurs="0" maxOccurs="1"/>
    //   <xsd:element ref="n42:NuclideAnalysisResultsExtension" minOccurs="0" maxOccurs="unbounded"/>
    builder.reader(new ExtensionReader("NuclideAnalysisResultsExtension")).nop().optional().unbounded();
    return builder.getHandlers();
  }

}
