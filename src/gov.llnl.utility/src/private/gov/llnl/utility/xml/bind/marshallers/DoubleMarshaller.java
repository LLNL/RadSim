/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind.marshallers;

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
      if(Double.toString(o).contains("E"))
      {
        return String.format("%.14E", o).replaceFirst("\\.?0+E", "E");
      }
      else 
      {
        if(Double.toString(Math.abs(o)).split("\\.")[0].length() <= 5)
        {
          if(Math.signum(o) == 0.0 || Math.signum(o) == 1.0)
          {
            return String.format("%.8f", o).replaceFirst("\\.0*$|(\\.\\d*?)0+$", "$1");
          }
          else if(Math.signum(o) == -1.0)
          {
            return String.format("%.8f", o).replaceFirst("\\.0*$|(\\d+\\.+\\d+?)0+$", "$1");
          }
        }
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
