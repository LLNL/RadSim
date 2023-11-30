/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.RadItemQuantityReader;
import gov.nist.physics.n42.writer.RadItemQuantityWriter;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(RadItemQuantityReader.class)
@WriterInfo(RadItemQuantityWriter.class)
public class RadItemQuantity extends ComplexObject
{
  /**
   * @return the value
   */
  public double getValue()
  {
    return value;
  }

  /**
   * @return the uncertainty
   */
  public Double getUncertainty()
  {
    return uncertainty;
  }

  /**
   * @return the units
   */
  public String getUnits()
  {
    return units;
  }
  private double value;
  private Double uncertainty;
  private String units;

  public void setValue(double u)
  {
    this.value = u;
  }

  public void setUncertainty(Double u)
  {
    this.uncertainty = u;
  }

  public void setUnits(String u)
  {
    this.units = u;
  }
  
}
