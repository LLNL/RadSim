/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.EnergyWindowsReader;
import gov.nist.physics.n42.writer.EnergyWindowsWriter;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * A data type for identifying a set of energy window boundaries for gross counting 
 * radiation detector calibration.
 * 
 * @author monterial1
 */
@ReaderInfo(EnergyWindowsReader.class)
@WriterInfo(EnergyWindowsWriter.class)
public class EnergyWindows extends ComplexObject
{
  private double[] startEnergyValues;
  private double[] endEnergyValues;

  public double[] getStartEnergyValues()
  {
    return startEnergyValues;
  }

  public void setStartEnergyValues(double[] startEnergyValues)
  {
    this.startEnergyValues = startEnergyValues;
  }

  public double[] getEndEnergyValues()
  {
    return endEnergyValues;
  }

  public void setEndEnergyValues(double[] endEnergyValues)
  {
    this.endEnergyValues = endEnergyValues;
  }
  
}
