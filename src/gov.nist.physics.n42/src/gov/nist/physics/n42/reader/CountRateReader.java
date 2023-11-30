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
import gov.nist.physics.n42.data.CountRate;
import gov.nist.physics.n42.data.Quantity;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "CountRate",
        order = Reader.Order.SEQUENCE,
        cls = DoseRate.class,
        typeName = "CountRateType")
@Reader.Attribute(name = "id", type = String.class, required = true)
@Reader.Attribute(name = "radDetectorInformationReference", type = String.class, required = true)
@Reader.Attribute(name = "radRawDoseRateReferences", type = String.class, required = false)
public class CountRateReader extends ObjectReader<CountRate>
{
  @Override
  public CountRate start(ReaderContext context, Attributes attr) throws ReaderException
  {
    CountRate out = new CountRate();
    ReaderUtilities.register(context, out, attr);
    context.addDeferred(out, CountRate::setDetector,
            attr.getValue("radDetectorInformationReference"), RadDetectorInformation.class);
    return out;
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    ReaderBuilder<CountRate> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("CountRateValue").call(CountRate::setValue, Quantity.class);
    return builder.getHandlers();
  }

}
