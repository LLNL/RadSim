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
 * This annotation denotes a private member that has been exposed as public for
 * auditing purposes. It will not be part of the public release and will be
 * returned to private once debugging is complete.
 *
 * @author nelson85
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Debug
{
}
