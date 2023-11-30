/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Define the encoding for this type.
 * 
 * This annotation is required to encode arbitrary data types.
 * 
 * @author nelson85
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Encoding
{
  Class<? extends MessageEncoding> value();
}
