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
import gov.nist.physics.n42.data.FWHMCalibration;
import java.time.Instant;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "FWHMCalibration",
        order = Reader.Order.SEQUENCE,
        cls = FWHMCalibration.class,
        typeName = "FWHMCalibrationType")
@Reader.Attribute(name = "id", type = String.class, required = true)
public class FWHMCalibrationReader extends ObjectReader<FWHMCalibration>
{
  @Override
  public FWHMCalibration start(ReaderContext context, Attributes attr) throws ReaderException
  {
    FWHMCalibration out = new FWHMCalibration();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    ReaderBuilder<FWHMCalibration> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("EnergyValues").call(FWHMCalibration::setEnergyValues, double[].class).required();
    builder.element("FWHMValues").call(FWHMCalibration::setFwhmValues, double[].class).required();
    builder.element("FWHMUncertaintyValues").call(FWHMCalibration::setFwhmUncertaintyValues, double[].class).optional();
    builder.element("CalibrationDateTime").call(FWHMCalibration::setCalibrationDateTime, Instant.class).optional();
    return builder.getHandlers();
  }

}
