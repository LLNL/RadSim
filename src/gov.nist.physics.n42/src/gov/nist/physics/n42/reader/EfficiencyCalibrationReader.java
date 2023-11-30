/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.llnl.utility.io.ReaderException;
import gov.nist.physics.n42.data.EfficiencyCalibration;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.ComplexObject;
import java.time.Instant;
import org.xml.sax.Attributes;

/**
 *  
 * @author monterial1
 */

@Reader.Declaration(pkg = N42Package.class,
        name = "EfficiencyCalibration",
        order = Reader.Order.SEQUENCE,
        cls = EfficiencyCalibration.class,
        typeName = "EfficiencyCalibrationType")
@Reader.Attribute(name="id", type=String.class, required=true)
public class EfficiencyCalibrationReader extends ObjectReader<EfficiencyCalibration>
{
  
  @Override
  public EfficiencyCalibration start(ReaderContext context, Attributes attr) throws ReaderException
  {
    EfficiencyCalibration out = new EfficiencyCalibration();
    ReaderUtilities.register(context, out, attr);
    return out;
  }
  
    @Override
  public ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    ReaderBuilder<EfficiencyCalibration> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("EnergyValues").call(EfficiencyCalibration::setEnergyValues, double[].class).required();
    builder.element("EfficiencyValues").call(EfficiencyCalibration::setEfficiencyValues, double[].class).required();
    builder.element("EfficiencyUncertaintyValues").call(EfficiencyCalibration::setEfficiencyUncertaintyValues, double[].class).optional();
    builder.element("CalibrationDateTime").call(EfficiencyCalibration::setCalibrationDateTime, Instant.class).optional();    
    return builder.getHandlers();
  }

}
