/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.AnalysisAlgorithmVersionReader;
import gov.nist.physics.n42.writer.AnalysisAlgorithmVersionWriter;
import java.util.Collections;
import java.util.List;

/**
 * A data type for information regarding an analysis algorithm version.
 * 
 * @author monterial1
 */
@ReaderInfo(AnalysisAlgorithmVersionReader.class)
@WriterInfo(AnalysisAlgorithmVersionWriter.class)
public class AnalysisAlgorithmVersion extends ComplexObject
{
  private String componentName;
  private String componentVersion;

  /**
   * 
   * @return Name of an algorithm component.
   */
  public String getComponentName()
  {
    return componentName;
  }

  public void setComponentName(String componentName)
  {
    this.componentName = componentName;
  }

  /**
   * 
   * @return Version information for the algorithm component.
   */
  public String getComponentVersion()
  {
    return componentVersion;
  }

  public void setComponentVersion(String componentVersion)
  {
    this.componentVersion = componentVersion;
  }
  
}
