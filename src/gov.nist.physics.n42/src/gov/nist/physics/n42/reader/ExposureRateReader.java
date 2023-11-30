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
import gov.nist.physics.n42.data.ExposureRate;
import gov.nist.physics.n42.data.Quantity;
import gov.nist.physics.n42.data.RadDetectorInformation;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "ExposureRate",
        order = Reader.Order.SEQUENCE,
        cls = ExposureRate.class,
        typeName = "ExposureRateType")
@Reader.Attribute(name = "id", type = String.class, required = true)

@Reader.Attribute(name = "radDetectorInformationReference", type = String.class, required = true)

@Reader.Attribute(name = "radRawExposureRateReferences", type = String.class, required = false)
public class ExposureRateReader extends ObjectReader<ExposureRate>
{
  @Override
  public ExposureRate start(ReaderContext context, Attributes attr) throws ReaderException
  {
    ExposureRate out = new ExposureRate();
    ReaderUtilities.register(context, out, attr);
    context.addDeferred(out, ExposureRate::setDetector,
            attr.getValue("radDetectorInformationReference"),
            RadDetectorInformation.class);
// Used in derived data blocks.  Need example.
//    ReaderUtilities.addReferences(getContext(), out, 
//            AnalysisResults::addRaw, Object.class, attr.getValue("radRawExposureRateReferences"));
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<ExposureRate> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("ExposureRateValue").call(ExposureRate::setValue, Quantity.class);
    builder.element("ExposureRateLevelDescription").callString(ExposureRate::setLevelDescription).optional();
    return builder.getHandlers();
  }

}
