/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.writer;

import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.ObjectWriter;
import gov.llnl.utility.xml.bind.WriterContext;
import gov.nist.physics.n42.data.ComplexObject;
import java.util.List;

/**
 *
 * @author her1
 */
public class WriterUtilities
{

  public static String getObjectReference(ComplexObject co, WriterContext context)
  {
//    if (co.getId()!=null)
//      return co.getId();
    String reference = context.getReference(co);
    if (reference == null)
      throw new RuntimeException("Reference object is missing id for " + co.getClass().getName() + " " + co.getId());
    return reference;
  }

  public static String getObjectReferences(List<? extends ComplexObject> list, WriterContext context)
  {
    StringBuilder sb = new StringBuilder();
    for (ComplexObject item : list)
    {
      String ref = WriterUtilities.getObjectReference(item, context);

      if (ref != null)
      {
        if (sb.length() > 0)
          sb.append(" ");
        sb.append(ref);
      }
    }
    if (sb.length() == 0)
      return null;
    return sb.toString();
  }

  public static void writeRemarkContents(ObjectWriter.WriterBuilder builder, ComplexObject co) throws WriterException
  {
    List<String> remarks = co.getRemarks();
    if (!remarks.isEmpty())
    {
      for (int i = 0; i < remarks.size(); ++i)
      {
        builder.element("Remark").putString(remarks.get(i));
      }
    }
  }

  public static String formatDoubleObject(double o)
  {
    if (o == Math.rint(o))
    {
      return Long.toString((long) Math.rint(o));
    }
    
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

  static int id = 0;

  /**
   * Method to make sure that ids appear where needed.
   *
   * @param referencedObjects
   */
  static void ensureId(ComplexObject object)
  {
    if (object.getId() == null)
      object.setId(String.format("%s-%d", object.getClass().getSimpleName(), id++));
  }

}
