/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.CharacteristicReader;
import gov.nist.physics.n42.writer.CharacteristicWriter;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(CharacteristicReader.class)
@WriterInfo(CharacteristicWriter.class)
public class Characteristic extends ComplexObject
{
  private String name;
  private String value;
  private String valueUnits;
  private String valueDataClassCode;

  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * @return the value
   */
  public String getValue()
  {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(String value)
  {
    this.value = value;
  }

  /**
   * @return the valueUnits
   */
  public String getValueUnits()
  {
    return valueUnits;
  }

  /**
   * @param valueUnits the valueUnits to set
   */
  public void setValueUnits(String valueUnits)
  {
    this.valueUnits = valueUnits;
  }

  /**
   * @return the valueDataClassCode
   */
  public String getValueDataClassCode()
  {
    return valueDataClassCode;
  }

  /**
   * @param valueDataClassCode the valueDataClassCode to set
   */
  public void setValueDataClassCode(String valueDataClassCode)
  {
    this.valueDataClassCode = valueDataClassCode;
  }
      
}
