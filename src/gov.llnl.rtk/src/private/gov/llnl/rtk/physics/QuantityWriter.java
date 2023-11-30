/*
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.rtk.RtkPackage;
import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.ObjectWriter;

/**
 *
 * @author nelson85
 */
public class QuantityWriter extends ObjectWriter<Quantity>
{
  public QuantityWriter()
  {
    super(Options.NONE, "quantity", RtkPackage.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, Quantity object) throws WriterException
  {
    if (object.units != null)
      attributes.add("units", object.units);
  }

  @Override
  public void contents(Quantity object) throws WriterException
  {
    this.addContents(Double.toString(object.value));
  }

}
