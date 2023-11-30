/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.euclidean;

import gov.llnl.math.MathPackage;
import gov.llnl.math.euclidean.Versor;
import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.ObjectWriter;

/**
 *
 * @author nelson85
 */
public class VersorWriter extends ObjectWriter<Versor>
{
  public VersorWriter()
  {
    super(Options.REFERENCEABLE, "versor", MathPackage.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, Versor object) throws WriterException
  {
    double i = object.getI();
    double j = object.getJ();
    double k = object.getK();
    double r = Math.sqrt(i * i + j * j + k * k);
    double angle = 2 * Math.atan2(r, object.getU());

    attributes.add("x", i / r);
    attributes.add("y", j / r);
    attributes.add("z", k / r);
    attributes.add("angle", angle);
  }

  @Override
  public void contents(Versor object) throws WriterException
  {
  }

}
