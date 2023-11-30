/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.euclidean;

import gov.llnl.math.MathPackage;
import gov.llnl.math.euclidean.Vector3;
import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.ObjectWriter;

/**
 *
 * @author nelson85
 */
public class Vector3Writer extends ObjectWriter<Vector3>
{
  public Vector3Writer()
  {
    super(Options.REFERENCEABLE, "vector3", MathPackage.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, Vector3 object) throws WriterException
  {
    attributes.add("x", object.getX());
    attributes.add("y", object.getY());
    attributes.add("z", object.getZ());
  }

  @Override
  public void contents(Vector3 object) throws WriterException
  {
  }

}
