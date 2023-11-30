/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author nelson85
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Schema
{
  /**
   * Namespaces is the uri for the package.
   */
  String namespace();

  /**
   * Schema is the resource name uri for this class.
   */
  String schema();

  /**
   * Prefix is the preferred namespace prefix for elements.
   */
  String prefix();

  @Target(ElementType.TYPE)
  @Repeatable(UsingList.class)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Using
  {
    Class value();
  }

  @Target(ElementType.TYPE)
  @Repeatable(IncludeList.class)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Include
  {
    Class value();
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface UsingList
  {
    Using[] value();
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface IncludeList
  {
    Include[] value();
  }
  
  public static final String NONE = "NONE";
}
