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
import gov.nist.physics.n42.data.LocationDescription;

/**
 *
 * @author her1
 */
public class LocationDescriptionWriter extends ObjectWriter<LocationDescription>
{
  public LocationDescriptionWriter()
  {
    super(Options.NONE, "LocationDescription", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, LocationDescription object) throws WriterException
  {
  }

  @Override
  public void contents(LocationDescription object) throws WriterException
  {
    getContext().addContents(object.getValue());
  }
  
}
