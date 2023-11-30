/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to denote the reader for a class.
 *
 * Annotating a class with a reader allows the class to automatically create a
 * reader by calling ObjectReader.create(Class).
 */
@Target(value =
{
  ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReaderInfo
{
  /**
   * Get the associated ObjectReader.
   *
   * @return the ObjectReader for this class.
   */
  public Class<? extends ObjectReader> value();
}
