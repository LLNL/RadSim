/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.RadInstrumentInformationReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.RadInstrumentInformationWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nelson85
 */
@ReaderInfo(RadInstrumentInformationReader.class)
@WriterInfo(RadInstrumentInformationWriter.class)
public class RadInstrumentInformation extends ComplexObject implements OriginReference
{
  private String manufacturerName;
  private String identifier;
  private String modelName;
  private String description;
  private String classCode;
  private final LinkedHashMap<String, String> version = new LinkedHashMap<>();
  private final List<Characteristics> characteristics = new ArrayList<>();
  private RadInstrumentQualityControl qualityControl;

  /**
   * @return the manufacturerName
   */
  public String getManufacturerName()
  {
    return manufacturerName;
  }

  /**
   * @param manufacturerName the manufacturerName to set
   */
  public void setManufacturerName(String manufacturerName)
  {
    this.manufacturerName = manufacturerName;
  }

  /**
   * @return the identifier
   */
  public String getIdentifier()
  {
    return identifier;
  }

  /**
   * @param identifier the identifier to set
   */
  public void setIdentifier(String identifier)
  {
    this.identifier = identifier;
  }

  /**
   * @return the modelName
   */
  public String getModelName()
  {
    return modelName;
  }

  /**
   * @param modelName the modelName to set
   */
  public void setModelName(String modelName)
  {
    this.modelName = modelName;
  }

  /**
   * @return the modelName
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * @param modelName the modelName to set
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * @return the classCode
   */
  public String getClassCode()
  {
    return classCode;
  }

  /**
   * @param classCode the classCode to set
   */
  public void setClassCode(String classCode)
  {
    this.classCode = classCode;
  }

  /**
   * @return the version
   */
  public Map<String, String> getVersion()
  {
    return version;
  }

  /**
   * @param version the version to set
   */
  public void addVersion(RadInstrumentVersion version)
  {
    this.version.put(version.getComponentName(), version.getComponentVersion());
  }

  public void setQualityControl(RadInstrumentQualityControl value)
  {
    this.qualityControl = value;
  }

  public void addCharacteristics(Characteristics chars)
  {
    this.characteristics.add(chars);
  }

  public List<Characteristics> getCharacteristics()
  {
    return this.characteristics;
  }

  /**
   * @return the qualityControl
   */
  public RadInstrumentQualityControl getQualityControl()
  {
    return qualityControl;
  }
  
}
