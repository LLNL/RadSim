/*
 * Copyright 2021, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
import static gov.llnl.utility.DateUtilities.convert;
import static gov.llnl.utility.DateUtilities.match;
import java.text.ParseException;
import java.time.Instant;

/**
 *
 * @author nelson85
 */
public class DateUilityTest
{
  public static void main(String[] args) throws ParseException
  {
    String[] TESTS = new String[]
    {
      "17-JUL-2019 15:03:01.17",
      "2018-03-15T10:22:05-07:00",
      "19-Jul-2020 14:35:02",
      // "19-Jul-99 14:35:02",
      "1988-01-20T14:35:02.1",
      "1988-01-20T14:35:02.12",
      "1988-01-20T14:35:02.123",
      "1988-01-20T14:35:02.12Z",
      "1988-01-20T14:35:02.123Z",
      // "1988-01-20T14:35:02.1234Z",
      // "1988-01-20T14:35:02.12+10",
      // "1988-01-20T14:35:02.123+10",
      "1988/01/20 14:35:02",
      "1988-01-20T14:35:02",
      // "1988-01-20T14:35:02+10",
      // "1988-01-20T14:35:02Z",
      "1988-01-20T14:35:02.123Z",
      "00000",
      "0.1",
      "0.01",
    };
    for (String test : TESTS)
    {
      System.out.println("====");
      System.out.println("0         1         2");
      System.out.println("01234567890123456789012345");

      System.out.println(test + " " + match(test));
      Instant i = convert(test, Instant::from);
      System.out.println(" ==> " + i);
      if (i==null)
        return;
    }
  }
}
