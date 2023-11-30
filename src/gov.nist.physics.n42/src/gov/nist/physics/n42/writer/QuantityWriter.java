/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.writer;

import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.ObjectWriter;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.Quantity;

/**
 *
 * @author her1
 */
public class QuantityWriter extends ObjectWriter<Quantity>
{
  public QuantityWriter()
  {
    super(Options.NONE, "Quantity", N42Package.getInstance());
  }
  
  @Override
  public void attributes(WriterAttributes attributes, Quantity object) throws WriterException
  {
    if(object.getUnits() != null)
    {
      attributes.add("units", object.getUnits());
    }
  }

  @Override
  public void contents(Quantity object) throws WriterException
  {
    getContext().addContents(Double.toString(object.getValue()));
  }
  
}
