/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation denotes an internal class that should not be part of the
 * public interface.
 *
 * @author nelson85
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Internal
{
}
