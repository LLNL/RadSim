/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.FaultReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.FaultWriter;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(FaultReader.class)
@WriterInfo(FaultWriter.class)
public class Fault extends ComplexObject
{
  private String code;
  private String description;
  private FaultSeverityCode severityCode;

  public Fault()
  {

  }

  public Fault(String code,
          String description,
          FaultSeverityCode severityCode)
  {
    this.code = code;
    this.description = description;
    this.severityCode = severityCode;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public void setSeverityCode(FaultSeverityCode severity)
  {
    this.severityCode = severity;
  }

  /**
   * @return the code
   */
  public String getCode()
  {
    return code;
  }

  /**
   * @return the description
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * @return the severityCode
   */
  public FaultSeverityCode getSeverityCode()
  {
    return severityCode;
  }
  
}
