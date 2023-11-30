/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.OriginReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.OriginWriter;
import java.util.function.Consumer;

/**
 *
 * @author pham21
 */
@ReaderInfo(OriginReader.class)
@WriterInfo(OriginWriter.class)
public class Origin
{
  private OriginReference reference;
  private GeographicPoint geographicPoint;
  private String description;

  /**
   * @return the georgraphicPoint
   */
  public GeographicPoint getGeographicPoint()
  {
    return geographicPoint;
  }

  /**
   * @param georgraphicPoint the georgraphicPoint to set
   */
  public void setGeographicPoint(GeographicPoint geographicPoint)
  {
    this.geographicPoint = geographicPoint;
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
   * @return the reference
   */
  public OriginReference getReference()
  {
    return reference;
  }

  /**
   * @param reference the reference to set
   */
  public void setReference(OriginReference reference)
  {
    this.reference = reference;
  }

  public void visitReferencedObjects(Consumer<ComplexObject> visitor)
  {
    if (this.reference != null)
      visitor.accept((ComplexObject) this.reference);
  }

}
