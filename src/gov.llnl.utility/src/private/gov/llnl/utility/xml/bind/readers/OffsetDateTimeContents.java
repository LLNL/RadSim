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
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(
        pkg = UtilityPackage.class,
        name = "offsetDateTime",
        referenceable = true,
        contents = Reader.Contents.TEXT,
        cls = OffsetDateTime.class)
@Reader.TextContents(base = "xs:string")
public class OffsetDateTimeContents extends ContentsReader<OffsetDateTime>
{

  @Override
  public OffsetDateTime contents(ReaderContext context, String textContents) throws ReaderException
  {
    try
    {
      return DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(textContents, OffsetDateTime::from);
    }
    catch (DateTimeParseException ex)
    {
    }

    return DateUtilities.convert(textContents, OffsetDateTime::from);
  }

}
