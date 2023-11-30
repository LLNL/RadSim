/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.RadAlarmReader;
import gov.nist.physics.n42.writer.RadAlarmWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(RadAlarmReader.class)
@WriterInfo(RadAlarmWriter.class)
public class RadAlarm extends ComplexObject
{
  private final List<RadDetectorInformation> detectors = new ArrayList<>();
  private Instant dateTime;
  private RadAlarmCategoryCode categoryCode;
  private String description;
  private Boolean audibleIndicator;
  private String lightColor;
  private int[] energyWindowIndices;

  public RadAlarmCategoryCode getCategoryCode()
  {
    return categoryCode;
  }

  public void setCategoryCode(RadAlarmCategoryCode categoryCode)
  {
    this.categoryCode = categoryCode;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public Boolean getAudibleIndicator()
  {
    return audibleIndicator;
  }

  public void setAudibleIndicator(Boolean audibleIndicator)
  {
    this.audibleIndicator = audibleIndicator;
  }

  public String getLightColor()
  {
    return lightColor;
  }

  public void setLightColor(String lightColor)
  {
    this.lightColor = lightColor;
  }

  public int[] getEnergyWindowIndices()
  {
    return energyWindowIndices;
  }

  public void setEnergyWindowIndices(int[] energyWindowIndices)
  {
    this.energyWindowIndices = energyWindowIndices;
  }

  public void addDetector(RadDetectorInformation detector)
  {
    this.detectors.add(detector);
  }

  public List<RadDetectorInformation> getDetectors()
  {
    return this.detectors;
  }

  /**
   * @return the dateTime
   */
  public Instant getDateTime()
  {
    return dateTime;
  }

  /**
   * @param dateTime the dateTime to set
   */
  public void setDateTime(Instant dateTime)
  {
    this.dateTime = dateTime;
  }

  @Override
  public void visitReferencedObjects(Consumer<ComplexObject> visitor)
  {
    detectors.forEach(p -> p.visitReferencedObjects(visitor));
  }

}
