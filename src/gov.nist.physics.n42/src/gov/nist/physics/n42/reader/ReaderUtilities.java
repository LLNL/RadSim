/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.ComplexObject;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
public class ReaderUtilities
{
  /**
   * Convert a string with separated doubles into a double array. This method
   * used regular expressions so it can be rather slow for long lists.
   *
   * @param str is a string with whitespace delimited doubles
   * @return the array of doubles converted
   */
  public static double[] doublesFromString(String str)
  {
    ArrayList<Double> out = new ArrayList<>();
    Matcher matcher = Pattern.compile("[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?").matcher(str);
    while (matcher.find())
    {
      double element = Double.parseDouble(matcher.group());
      out.add(element);
    }
    double[] out2 = new double[out.size()];
    int i = 0;
    for (double d : out)
    {
      out2[i++] = d;
    }
    return out2;
  }
  
  /**
   * Convert a string with separated doubles into a double array. This method
   * used regular expressions so it can be rather slow for long lists.
   * Some N42 does not follow the counted zeroes compression rule which 
   * will lead to subtle bugs. This method address this issue by inserting the
   * number 1 if the value after the zero is a float. 
   * 
   * @param str is a string with whitespace delimited doubles
   * @return the array of doubles converted
   */
  public static double[] doublesFromCountedZerosString(String str)
  {
    ArrayList<Double> out = new ArrayList<>();
    Matcher matcher = Pattern.compile("[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?").matcher(str);
    while (matcher.find())
    {
      // Some N42 files doesn't follow the counted zeroes comporession rule
      if(!out.isEmpty() && out.get(out.size() - 1) == 0.0)
      { // Previous value was a 0.0 so we need to check if the current value is an integer or not
        String value = matcher.group();
        if(value.contains(".")){
          // The current value is a double so we need to add the number one to out so the 
          // 0.0 gets unpacked correctly. If we didn't add the number one then casting
          // the current value migth give a result of 0 which leads to the 0.0 getting discarded.
          out.add(1.0);
        }
        
      }
      double element = Double.parseDouble(matcher.group());              
      out.add(element);
    }
    double[] out2 = new double[out.size()];
    int i = 0;
    for (double d : out)
    {
      out2[i++] = d;
    }
    return out2;
  }
  
  //</editor-fold>

  public static <T, T2> void addReferences(
          ReaderContext context,
          T target,
          BiConsumer<T, T2> method,
          Class<T2> cls,
          String references) throws ReaderException
  {
    if (references == null)
      return;
    for (String ref : references.split(" "))
    {
      context.addDeferred(target, method, ref, cls);
    }
  }

  static void register(ReaderContext context, ComplexObject out, Attributes attr) throws ReaderException
  {
    String id = attr.getValue("id");
    if (id == null)
      return;
    out.setId(id);
    context.put(id, out);
  }

}
