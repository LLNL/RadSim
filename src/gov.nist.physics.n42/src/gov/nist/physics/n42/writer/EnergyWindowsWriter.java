/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.writer;

import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.ObjectWriter;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.EnergyWindows;

/**
 *
 * @author her1
 */
public class EnergyWindowsWriter extends ObjectWriter<EnergyWindows>
{
  public EnergyWindowsWriter()
  {
    super(Options.NONE, "EnergyWindows", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, EnergyWindows object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(EnergyWindows object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    
    builder.element("WindowStartEnergyValues")
      .writer(new DoubleListWriter())
      .put(object.getStartEnergyValues());
    
    builder.element("WindowEndEnergyValues")
      .writer(new DoubleListWriter())
      .put(object.getEndEnergyValues());
  }
  
}
