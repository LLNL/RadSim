/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class such that all concrete classes must be a singleton.
 *
 * This requires SingletonProcessor to be enabled.
 *
 * @author nelson85
 */
@Inherited
//@Target(ElementType.TYPE)
//@Retention(RetentionPolicy.RUNTIME)
public @interface SingletonRequired
{

}
