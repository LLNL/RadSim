/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.nist.physics.n42.data.GrossCounts;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.RadDetectorInformation;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.ComplexObject;
import gov.nist.physics.n42.data.EnergyWindows;
import java.time.Duration;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "GrossCounts",
        order = Reader.Order.SEQUENCE,
        cls = GrossCounts.class,
        typeName = "GrossCountsType")
@Reader.Attribute(name = "id", type = String.class, required = true)

@Reader.Attribute(name = "radDetectorInformationReference", type = String.class, required = true)

@Reader.Attribute(name = "energyWindowsReference", type = String.class, required = false)
public class GrossCountsReader extends ObjectReader<GrossCounts>
{
  @Override
  public GrossCounts start(ReaderContext context, Attributes attr) throws ReaderException
  {
    GrossCounts out = new GrossCounts();
    ReaderUtilities.register(context, out, attr);
    context.addDeferred(out, GrossCounts::setDetector, attr.getValue("radDetectorInformationReference"), RadDetectorInformation.class);
    context.addDeferred(out, GrossCounts::setEnergyWindows, attr.getValue("energyWindowsReference"), EnergyWindows.class);
    return out;
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    ReaderBuilder<GrossCounts> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("LiveTimeDuration").call(GrossCounts::setLiveTimeDuration, Duration.class);
    builder.element("CountData").reader(new DoubleListReader()).call(GrossCounts::setCountData).optional();
    return builder.getHandlers();
  }

}
