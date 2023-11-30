/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.writer;

import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.ObjectWriter;
import java.time.Duration;

/**
 *
 * @author her1
 */
public class DurationWriter extends ObjectWriter<Duration>
{
  public DurationWriter()
  {
    super(Options.NONE, "Duration", null);
  }

  @Override
  public void attributes(WriterAttributes attributes, Duration object) throws WriterException
  {
  }

  @Override
  public void contents(Duration object) throws WriterException
  {
    getContext().addContents(object.toString());
  }
  
}
