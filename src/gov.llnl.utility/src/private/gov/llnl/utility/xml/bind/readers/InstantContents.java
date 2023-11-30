/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind.readers;

import gov.llnl.utility.DateUtilities;
import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import java.time.Instant;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(
        pkg = UtilityPackage.class,
        name = "instant",
        referenceable = true,
        contents = Reader.Contents.TEXT,
        cls = Instant.class)
@Reader.TextContents(base = "xs:string")
public class InstantContents extends ContentsReader<Instant>
{

  @Override
  public Instant contents(ReaderContext context, String textContents) throws ReaderException
  {
    Instant date = DateUtilities.convert(textContents, Instant::from);
    if (date == null)
      throw new ReaderException("Unable to parse date string " + textContents);
    return date;
  }

}
