/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.writer;

import gov.llnl.utility.xml.bind.Marshaller;
import gov.llnl.utility.xml.bind.ObjectWriter;
import gov.llnl.utility.xml.bind.WriterContext;

/**
 *
 * @author nelson85
 */
class DoubleMarshaller implements Marshaller<Double>
{
  @Override
  public String marshall(Double o, WriterContext.MarshallerOptions options)
  {
    if (options == null)
      return o.toString();
    String format = options.get(ObjectWriter.DOUBLE_FORMAT, String.class, null);
    if (format == null && o == 0)
      return "0";
    if (format == null) 
    {
//      if (o == Math.rint(o))
//      {
//        return Long.toString((long) Math.rint(o));
//      }

      // Maintain scientific notation for objects already in that form
      // 1.77E07 -> 1.77E+07
      if ((Double.toString(o).contains("E")) || (Double.toString(o).contains("e")))
      {
        return String.format("%.14E", o).replaceFirst("\\.?0+E", "E");
      }
      else
      {
        // If the length of the object is <= 5, check the sign to determine its format
        if (Double.toString(Math.abs(o)).split("\\.")[0].length() <= 5)
        {
          // If the object is 0 or positive, format it like a float and remove trailing zeros
          // 123.45000 -> 123.45
          if (Math.signum(o) == 0.0 || Math.signum(o) == 1.0)
          {
            return String.format("%.8f", o).replaceFirst("\\.0*$|(\\.\\d*?)0+$", "$1");
          }
          // If the object is negative, format it like a float without scientific notation
          // -90.0 -> -90.0
          else if (Math.signum(o) == -1.0)
          {
            return String.format("%.8f", o).replaceFirst("\\.0*$|(\\d+\\.+\\d+?)0+$", "$1");
          }
        }
        // Format all objects with length > 5 in scientific notation
        // 123456789 -> 1.23456789E+08
        return String.format("%.14E", o).replaceFirst("\\.?0+E", "E");
      }
    }
    return String.format(format, o);
  }

  @Override
  public Class<Double> getObjectClass()
  {
    return Double.class;
  }

}
