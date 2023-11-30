/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.CountRateReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.CountRateWriter;
import java.util.function.Consumer;

/**
 * 
 * (Nonstandard)  Appears in Detective X
 *
 * @author nelson85
 */
@ReaderInfo(CountRateReader.class)
@WriterInfo(CountRateWriter.class)
public class CountRate extends ComplexObject
{
  private RadDetectorInformation detector;
  private Quantity value;

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

  @Override
  public void visitReferencedObjects(Consumer<ComplexObject> visitor)
  {
    visitor.accept(this.detector);
    // FIXME add rawDoseRate if present
  }

}
