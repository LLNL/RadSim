/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.nist.physics.n42.data.DoseRate;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.RadDetectorInformation;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.ComplexObject;
import gov.nist.physics.n42.data.Quantity;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "DoseRate",
        order = Reader.Order.SEQUENCE,
        cls = DoseRate.class,
        typeName = "DoseRateType")
@Reader.Attribute(name = "id", type = String.class, required = true)
@Reader.Attribute(name = "radDetectorInformationReference", type = String.class, required = true)
@Reader.Attribute(name = "radRawDoseRateReferences", type = String.class, required = false)
public class DoseRateReader extends ObjectReader<DoseRate>
{
  @Override
  public DoseRate start(ReaderContext context, Attributes attr) throws ReaderException
  {
    DoseRate out = new DoseRate();
    ReaderUtilities.register(context, out, attr);
    context.addDeferred(out, DoseRate::setDetector,
            attr.getValue("radDetectorInformationReference"), RadDetectorInformation.class);
// Used in derived data blocks.  Need example.
//    ReaderUtilities.addReferences(getContext(), out, 
//            AnalysisResults::addRawDoseRate, Object.class, attr.getValue("radRawDoseRateReferences"));
    return out;
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    ReaderBuilder<DoseRate> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("DoseRateValue").call(DoseRate::setValue, Quantity.class);
    builder.element("DoseRateLevelDescription").callString(DoseRate::setLevelDescription).optional();
    return builder.getHandlers();
  }

}
