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
 * This annotation denotes code that could be optimized or written in a better
 * style. The idea is to be able to tag the parts of code that can be optimized
 * later, when time allows for it.
 *
 * @author monterial1
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Optimize
{
}
