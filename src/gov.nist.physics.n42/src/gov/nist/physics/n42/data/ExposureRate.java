/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.ExposureRateReader;
import gov.nist.physics.n42.writer.ExposureRateWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * A data type for radiation exposure rate data.
 * 
 * @author nelson85
 */
@ReaderInfo(ExposureRateReader.class)
@WriterInfo(ExposureRateWriter.class)
public class ExposureRate extends ComplexObject
{
  private RadDetectorInformation detector;
  private Quantity value;
  private String levelDescription;

  public RadDetectorInformation getDetector()
  {
    return detector;
  }

  public void setDetector(RadDetectorInformation detector)
  {
    this.detector = detector;
  }

  public Quantity getValue()
  {
    return value;
  }

  public void setValue(Quantity value)
  {
    this.value = value;
  }

  public String getLevelDescription()
  {
    return levelDescription;
  }

  public void setLevelDescription(String levelDescription)
  {
    this.levelDescription = levelDescription;
  }  
    
   @Override
  public void visitReferencedObjects(Consumer<ComplexObject> visitor)
  {
    visitor.accept(this.detector);
  }

}
