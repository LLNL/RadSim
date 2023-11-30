/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.YEAR;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Collection of utilities for converting dates.
 */
public class DateUtilities
{
  // I believe this has fallen out of use
//  static public String toStringTimeZone(Instant date, String TZ)
//  {
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
//    sdf.setTimeZone(TimeZone.getTimeZone(TZ));
//    return sdf.format(date);
//  }

    public static final Map<Long, String>  DAYS_OF_WEEK_MAP  = new HashMap<>();
    static {
        DAYS_OF_WEEK_MAP.put(1L, "Mon");
        DAYS_OF_WEEK_MAP.put(2L, "Tue");
        DAYS_OF_WEEK_MAP.put(3L, "Wed");
        DAYS_OF_WEEK_MAP.put(4L, "Thu");
        DAYS_OF_WEEK_MAP.put(5L, "Fri");
        DAYS_OF_WEEK_MAP.put(6L, "Sat");
        DAYS_OF_WEEK_MAP.put(7L, "Sun");
    }

    public static final Map<Long, String> MONTHS_OF_YEAR_MAP = new HashMap<>();
    static {
        MONTHS_OF_YEAR_MAP.put(1L, "Jan");
        MONTHS_OF_YEAR_MAP.put(2L, "Feb");
        MONTHS_OF_YEAR_MAP.put(3L, "Mar");
        MONTHS_OF_YEAR_MAP.put(4L, "Apr");
        MONTHS_OF_YEAR_MAP.put(5L, "May");
        MONTHS_OF_YEAR_MAP.put(6L, "Jun");
        MONTHS_OF_YEAR_MAP.put(7L, "Jul");
        MONTHS_OF_YEAR_MAP.put(8L, "Aug");
        MONTHS_OF_YEAR_MAP.put(9L, "Sep");
        MONTHS_OF_YEAR_MAP.put(10L, "Oct");
        MONTHS_OF_YEAR_MAP.put(11L, "Nov");
        MONTHS_OF_YEAR_MAP.put(12L, "Dec");
    }

  /**
   * Covert a date into an Iso8601 compliant string.
   *
   * @param date
   * @return the string formatted in Iso8601.
   */
  public static String getIso8601(TemporalAccessor date)
  {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("Z"));
    return formatter.format(date);
  }

  static public DateTimeFormatter match(String time)
  {
    for (DatePattern datePattern : DATE_PATTERNS)
    {
      if (datePattern.pattern.matcher(time).matches())
        return datePattern.formatter;
    }
    return null;
  }

  /**
   * Convert a string to a date time with automatic identification of the date
   * pattern.
   *
   * This uses a number of regular expressions to find the best date formatter
   * for the time. Not every time string can be interpreted unambiguously.
   *
   * Local date times are interpreted using the current time zone and may not be
   * correct.
   *
   * The TemporalQuery is used to control the type of Temporal quantity that is
   * produced. Typically this is Instant::from, but others can be used.
   *
   * This method is primarily used to convert strings from xml attributes,
   * elements, or string fields in data files in which the data format is not
   * well specified or a user supplied date string without validation is
   * allowed.
   *
   * @param <T> is the type of Temporal quantity to produce.
   * @param time is the string to parse.
   * @param query is a lambda that creates the Temporal quantity.
   * @return a new date extracted from the string.
   * @throws DateTimeParseException if unable to parse the requested result
   */
  static public <T> T convert(String time, TemporalQuery<T> query)
          throws DateTimeParseException
  {
    DateTimeFormatter formatter = match(time);
    if (formatter == null)
    {
      UtilityPackage.LOGGER.warning("Unknown Date Format: " + time);
      return null;
    }

    TimeZone l = TimeZone.getDefault();
    return formatter.withZone(l.toZoneId()).parse(time, query);
  }

//<editor-fold desc="patterns" defaultstate="collapsed">
  static class DatePattern
  {
    Pattern pattern;
    DateTimeFormatter formatter;

    private DatePattern(Pattern compile, DateTimeFormatter formatter)
    {
      this.pattern = compile;
      this.formatter = formatter;
    }
  }

  static final List<DatePattern> DATE_PATTERNS = new ArrayList<>();

  private static void addPattern(String pattern, DateTimeFormatter formatter)
  {
    DATE_PATTERNS.add(new DatePattern(Pattern.compile(pattern), formatter));
  }

  static
  {
    // These patterns were set up to support the various patterns that we see
    // in measurements produced by radiation detectors.  New patterns are added
    // to support the needs on current work and conflicting patterns tend to get
    // dropped.

    // Patterns that have appear in GADRAS files
    DateTimeFormatter date_format = new DateTimeFormatterBuilder()
                .appendValue(DAY_OF_MONTH, 2)
                .appendLiteral('-')
                .appendText(MONTH_OF_YEAR, MONTHS_OF_YEAR_MAP)
                .appendLiteral('-')
                .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
                .toFormatter();

    // Java does not accept a HHMM without a colon though it is valid pattern,
    // so we will need to add it manually.
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendOptional(DateTimeFormatter.ofPattern("d-MMM-y H:m:s[.[SSS][SS]]"))
            .optionalStart()
            .append(ISO_LOCAL_DATE_TIME)
            .parseLenient()
            .appendOffset("+HHMM", "Z")
            .parseStrict()
            .optionalEnd()
            .toFormatter();
    addPattern("\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{2}:\\d{2}(\\.\\d+)?[+-]\\d{4}", formatter);
    addPattern("\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{2}:\\d{2}", DateTimeFormatter.ofPattern("y/M/d H:m:s"));
    addPattern("\\d{2}-\\S{3}-\\d{4} \\d{1,2}:\\d{2}:\\d{2}(\\.\\d+)?", formatter);
    // addPattern("\\d{2}-\\S{3}-\\d{2} \\d{1,2}:\\d{2}:\\d{2}", DateTimeFormatter.ofPattern("d-MMM-y H:m:s"));

    // A few of the common ISO patterns
    addPattern("\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{2}:\\d{2}(\\.\\d+)?", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    addPattern("\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{2}:\\d{2}(\\.\\d+)?Z", DateTimeFormatter.ISO_INSTANT);
    addPattern("\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{2}:\\d{2}(\\.\\d+)?[+-][0-9:]+", DateTimeFormatter.ISO_OFFSET_DATE_TIME);


    // Raw epoch times have sometimes appeared
    addPattern("\\d{1,19}(\\.\\d{1,9})?", new DateTimeFormatterBuilder()
            .appendValue(ChronoField.INSTANT_SECONDS, 1, 19, SignStyle.NEVER)
            .optionalStart()
            .appendFraction(NANO_OF_SECOND, 0, 9, true)
            .toFormatter());
  }
//</editor-fold>
}
