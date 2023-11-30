/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.NuclideReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.NuclideWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * The analysis results for a single radionuclide.
 *
 * @author nelson85
 */
@ReaderInfo(NuclideReader.class)
@WriterInfo(NuclideWriter.class)
public class Nuclide extends ComplexObject
{

  private boolean identifiedIndicator;
  private final List<NuclideIdentificationConfidence> confidence = new ArrayList<>();
  private String name;
  private String category;
  private Double shieldingAtomicNumber;
  private Double shieldingArealDensity;
  private Quantity activity;
  private Quantity activityUncertainty;
  private Quantity minimumDetectableActivity;
  private SourceGeometryCode sourceGeometry;
  private SourcePosition sourcePosition;

  public String getCategory()
  {
    return category;
  }

  public void setCategory(String category)
  {
    this.category = category;
  }

  public void setName(String name)
  {
    this.name = name;
  }
  
  public <T> T findConfidence(Class<T> type)
  {
    for (NuclideIdentificationConfidence c:this.confidence)
    {
      if (type.isInstance(c))
        return (T) c;
    }
    return null;
  }

  public void addConfidence(NuclideIdentificationConfidence confidence)
  {
    this.confidence.add(confidence);
  }
  
  public void setIdentifiedIndicator(boolean flag)
  {
    this.identifiedIndicator = flag;
  }

  /**
   * @return the identifiedIndicator
   */
  public boolean isIdentifiedIndicator()
  {
    return identifiedIndicator;
  }

  /**
   * @return the confidence
   */
  public List<NuclideIdentificationConfidence> getConfidence()
  {
    return confidence;
  }

  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  public void setShieldingAtomicNumber(Double u)
  {
    this.shieldingAtomicNumber = u;
  }

  public void setShieldingArealDensity(Double u)
  {
    this.shieldingArealDensity = u;
  }

  /**
   * @return the activity
   */
  public Quantity getActivity()
  {
    return activity;
  }

  /**
   * @param activity the activity to set
   */
  public void setActivity(Quantity activity)
  {
    this.activity = activity;
  }

  /**
   * @return the activityUncertainty
   */
  public Quantity getActivityUncertainty()
  {
    return activityUncertainty;
  }

  /**
   * @param activityUncertainty the activityUncertainty to set
   */
  public void setActivityUncertainty(Quantity activityUncertainty)
  {
    this.activityUncertainty = activityUncertainty;
  }

  /**
   * @return the shieldingAtomicNumber
   */
  public Double getShieldingAtomicNumber()
  {
    return shieldingAtomicNumber;
  }

  /**
   * @return the shieldingArealDensity
   */
  public Double getShieldingArealDensity()
  {
    return shieldingArealDensity;
  }

  /**
   * @return the minimumDetectableActivity
   */
  public Quantity getMinimumDetectableActivity()
  {
    return minimumDetectableActivity;
  }

  /**
   * @param minimumDetectableActivity the minimumDetectableActivity to set
   */
  public void setMinimumDetectableActivity(Quantity minimumDetectableActivity)
  {
    this.minimumDetectableActivity = minimumDetectableActivity;
  }

  public void setSourceGeometryCode(SourceGeometryCode sourceGeometry)
  {
    this.sourceGeometry = sourceGeometry;
  }

  /**
   * @return the sourceGeometry
   */
  public SourceGeometryCode getSourceGeometry()
  {
    return sourceGeometry;
  }

  /**
   * @param sourceGeometry the sourceGeometry to set
   */
  public void setSourceGeometry(SourceGeometryCode sourceGeometry)
  {
    this.sourceGeometry = sourceGeometry;
  }

  public void setSourcePosition(SourcePosition position)
  {
    this.sourcePosition = position;
  }

  /**
   * @return the sourcePosition
   */
  public SourcePosition getSourcePosition()
  {
    return sourcePosition;
  }

}
