/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.RadDetectorInformationReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.RadDetectorInformationWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nelson85
 */
@ReaderInfo(RadDetectorInformationReader.class)
@WriterInfo(RadDetectorInformationWriter.class)
public class RadDetectorInformation extends ComplexObject implements OriginReference
{
  private Instant n42DocDateTime;
  private String name;
  private String categoryCode;
  private String kindCode;
  private String description;
  private Quantity length;
  private Quantity width;
  private Quantity depth;
  private Quantity volume;
  private Quantity diameter;
  private final List<Characteristics> characteristics = new ArrayList<>();

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
   * @return the categoryCode
   */
  public String getCategoryCode()
  {
    return categoryCode;
  }

  /**
   * @param categoryCode the categoryCode to set
   */
  public void setCategoryCode(String categoryCode)
  {
    this.categoryCode = categoryCode;
  }

  /**
   * @return the kindCode
   */
  public String getKindCode()
  {
    return kindCode;
  }

  /**
   * @param kindCode the kindCode to set
   */
  public void setKindCode(String kindCode)
  {
    this.kindCode = kindCode;
  }

  /**
   * @return the description
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * @return the length
   */
  public Quantity getLength()
  {
    return length;
  }

  /**
   * @param length the length to set
   */
  public void setLength(Quantity length)
  {
    this.length = length;
  }

  /**
   * @return the width
   */
  public Quantity getWidth()
  {
    return width;
  }

  /**
   * @param width the width to set
   */
  public void setWidth(Quantity width)
  {
    this.width = width;
  }

  /**
   * @return the depth
   */
  public Quantity getDepth()
  {
    return depth;
  }

  /**
   * @param depth the depth to set
   */
  public void setDepth(Quantity depth)
  {
    this.depth = depth;
  }

  /**
   * @return the volume
   */
  public Quantity getVolume()
  {
    return volume;
  }

  /**
   * @param volume the volume to set
   */
  public void setVolume(Quantity volume)
  {
    this.volume = volume;
  }

  /**
   * @return the diameter
   */
  public Quantity getDiameter()
  {
    return diameter;
  }

  /**
   * @param diameter the diameter to set
   */
  public void setDiameter(Quantity diameter)
  {
    this.diameter = diameter;
  }

  public void addCharacteristics(Characteristics c)
  {
    this.characteristics.add(c);
  }

  public void setN42DocDateTime(Instant time)
  {
    this.n42DocDateTime = time;
  }

  public Instant getN42DocDateTime()
  {
    return this.n42DocDateTime;
  }

  /**
   * @return the characteristics
   */
  public List<Characteristics> getCharacteristics()
  {
    return characteristics;
  }

}
