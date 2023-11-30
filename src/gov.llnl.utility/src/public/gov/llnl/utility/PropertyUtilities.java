/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import gov.llnl.utility.ClassUtilities.ValueOf;
import static gov.llnl.utility.ClassUtilities.newValueOf;
import java.util.logging.Level;

/**
 *
 * @author nelson85
 */
public class PropertyUtilities
{
  private static void warnIgnored(String propertyName, String propertyValue, String propertyType)
  {
    UtilityPackage.LOGGER.log(Level.WARNING, "Ignoring {0} property {1} with value {2}",
            new Object[]
            {
              propertyType, propertyName, propertyValue
            });
  }

  /**
   * Get a system property. If not found it gives the default value.
   *
   * @param <Type>
   * @param propertyName
   * @param defaultValue to be used if the property is not present, must not be
   * null,
   * @return the property found the system properties or the default value if
   * not found.
   */
  @SuppressWarnings("unchecked")
  static public <Type> Type get(String propertyName, Type defaultValue)
  {
    String propertyValue = System.getProperty(propertyName);

    // Prevent the user from abusing
    if (defaultValue == null)
      throw new RuntimeException("null default value is not allowed");

    // If not defined give the default
    if (propertyValue == null)
      return defaultValue;

    // Use the default unmarshaller
    ValueOf vo = newValueOf(defaultValue.getClass());

    // Convert the value
    try
    {
      return (Type) vo.valueOf(propertyValue);
    }
    catch (IllegalArgumentException ex)
    {
      warnIgnored(propertyName, propertyValue, defaultValue.getClass().getName().toLowerCase());
      return defaultValue;
    }
  }

//  static public String get(String propertyName, String defaultValue)
//  {
//    String propertyValue = System.getProperty(propertyName);
//    if (propertyValue == null)
//      return defaultValue;
//    return propertyValue;
//  }
//  static public void main(String[] args)
//  {
//    System.out.println("string "+get("foobar","string"));
//    System.out.println("boolean "+get("foobar",true));
//    System.out.println("double "+get("foobar",1.0));
//    System.out.println("integer "+get("foobar",1));
//    System.out.println("char "+get("foobar",'a'));
//  }
//
//  static public void main(String[] args)
//  {
//    Boolean b=true;
//    System.out.println(Number.class.isAssignableFrom(b.getClass()));
//    ValueOf vo = newValueOf(b.getClass());
//    System.out.println(vo.valueOf("true"));
//    System.out.println(vo.valueOf("True"));
//    System.out.println(vo.valueOf("TRUE"));
//    System.out.println(vo.valueOf("false"));
//    System.out.println(vo.valueOf("False"));
//    System.out.println(vo.valueOf("FALSE"));
//     System.out.println(vo.valueOf("FOOBAR"));
//  }
}
