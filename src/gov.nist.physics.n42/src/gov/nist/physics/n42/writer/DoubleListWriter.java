/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.writer;

import gov.llnl.utility.PackageResource;
import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.ObjectWriter;
import gov.nist.physics.n42.N42Package;

/**
 *
 * @author her1
 */
public class DoubleListWriter extends ObjectWriter<double[]>
{
  public DoubleListWriter()
  {
    super(Options.NONE, "NonNegativeDoubleList", N42Package.getInstance());
  }

  public DoubleListWriter(int options, String name, PackageResource pkg)
  {
    super(options, name, pkg);
  }

  @Override
  public void attributes(WriterAttributes attributes, double[] object) throws WriterException
  {
  }

  @Override
  public void contents(double[] object) throws WriterException
  {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < object.length; ++i)
    {
      sb.append(WriterUtilities.formatDoubleObject(object[i]));

      if (i != object.length - 1)
      {
        sb.append(" ");
      }
    }

    getContext().addContents(sb.toString());
  }

}
