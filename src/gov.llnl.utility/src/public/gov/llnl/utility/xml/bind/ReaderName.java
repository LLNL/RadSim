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
 *
 * @author nelson85
 */
@Target(value =
{
  ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReaderName
{
  public String value();
}
