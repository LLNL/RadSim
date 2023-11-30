/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.AnalysisAlgorithmSettingReader;
import gov.nist.physics.n42.writer.AnalysisAlgorithmSettingWriter;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author nelson85
 */
@ReaderInfo(AnalysisAlgorithmSettingReader.class)
@WriterInfo(AnalysisAlgorithmSettingWriter.class)
public class AnalysisAlgorithmSetting extends ComplexObject
{
  private String name;
  private String value;
  private String units;

  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * @return the value
   */
  public String getValue()
  {
    return value;
  }

  /**
   * @return the units
   */
  public String getUnits()
  {
    return units;
  }

  public void setName(String u)
  {
    this.name = u;
  }

  public void setValue(String u)
  {
    this.value = u;
  }

  public void setUnits(String u)
  {
    this.units = u;
  }
  
}
