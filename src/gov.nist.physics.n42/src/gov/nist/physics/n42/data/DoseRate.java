/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.DoseRateReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.DoseRateWriter;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(DoseRateReader.class)
@WriterInfo(DoseRateWriter.class)
public class DoseRate extends ComplexObject
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
    // FIXME add rawDoseRate if present
  }

}
