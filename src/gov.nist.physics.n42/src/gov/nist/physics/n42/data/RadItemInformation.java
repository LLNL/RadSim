/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.RadItemInformationReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.RadItemInformationWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author monterial1
 */
@ReaderInfo(RadItemInformationReader.class)
@WriterInfo(RadItemInformationWriter.class)
public class RadItemInformation extends ComplexObject implements OriginReference
{

  private String description;
  private Quantity quantity;
  private String measurementGeometryDescription;
  private final List<Characteristics> characteristics = new ArrayList<>();

  public void setDescription(String itemDescription)
  {
    this.description = itemDescription;
  }

  public void setQuantity(Quantity quantity)
  {
    this.quantity = quantity;
  }

  public void setMeasurementGeometryDescription(String description)
  {
    this.measurementGeometryDescription = description;
  }

  public void addCharacteristics(Characteristics chars)
  {
    this.characteristics.add(chars);
  }

  public String getDescription()
  {
    return description;
  }

  public List<Characteristics> getCharacteristics()
  {
    return this.characteristics;
  }
  
  public Quantity getQuantity()
  {
    return quantity;
  }

  public String getMeasurementGeometryDescription()
  {
    return measurementGeometryDescription;
  }
  
}
