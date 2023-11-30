/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.writer;

import gov.llnl.utility.io.WriterException;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.utility.SpectrumUtilities;

/**
 *
 * @author her1
 */
public class ChannelDataWriter extends DoubleListWriter
{
  private boolean compress;

  public ChannelDataWriter()
  {
    super(Options.NONE, "ChannelData", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, double[] object) throws WriterException
  {
    this.compress = getContext().getProperty(WriterOptions.COMPRESSED_ZEROS, Boolean.class, true);
    attributes.add("compressionCode", compress ? "CountedZeroes" : "None");
  }

  @Override
  public void contents(double[] object) throws WriterException
  {
    if (compress)
    {
      object = SpectrumUtilities.packCountedZeros(object);
    }
    super.contents(object);
  }

}
