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
 * This annotation indicates this method is for use with Matlab. Changing the
 * function name will break Matlab scripts. Matlab annotation should only be
 * applied to methods that are part of the public interface that are only there
 * to support Matlab. Matlab functions are for ease of writing Matlab code and
 * should not be used in java. Thus they are excluded from the public interface.
 *
 * @author nelson85
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Matlab
{
}
