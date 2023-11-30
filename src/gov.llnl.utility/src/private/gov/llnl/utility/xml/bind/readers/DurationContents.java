/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind.readers;

import gov.llnl.utility.DurationUtilities;
import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import java.text.ParseException;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(
        pkg = UtilityPackage.class,
        name = "duration",
        referenceable = true,
        contents = Reader.Contents.TEXT,
        cls = Duration.class)
@Reader.TextContents(base = "xs:string")
@Reader.Attribute(name = "units", type = String.class)
public class DurationContents extends ObjectReader<Duration>
{

  @Override
  public Duration start(ReaderContext context, Attributes attr) throws ReaderException
  {
    return null;
  }

  @Override
  public Duration contents(ReaderContext context, String textContents) throws DateTimeParseException, NumberFormatException, ReaderException
  {
    Attributes attr = context.getCurrentContext().getAttributes();
    ChronoUnit timeUnit = null;
    String units = attr.getValue("units");
    if (units == null)
    {
      try
      {
        textContents = DurationUtilities.format(textContents);
      }
      catch (ParseException ex)
      {
//        Logger.getLogger(DurationContents.class.getName()).log(Level.SEVERE, null, ex);
      }
      try
      {
        return Duration.parse(textContents);
      }
      catch (DateTimeParseException ex)
      {
        throw new ReaderException(ex);
      }
    }

    switch (units)
    {
      case "ns":
        timeUnit = ChronoUnit.NANOS;
        break;
      case "us":
        timeUnit = ChronoUnit.MICROS;
        break;
      case "ms":
        timeUnit = ChronoUnit.MILLIS;
        break;
      case "s":
        timeUnit = ChronoUnit.SECONDS;
        break;
      default:
        throw new ReaderException("Unknown unit");
    }

    try
    {
      return Duration.of(Long.parseLong(textContents), timeUnit);
    }
    catch (NumberFormatException ex)
    {
      throw new ReaderException(ex);
    }
  }

}
