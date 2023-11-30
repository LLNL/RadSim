/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

/**
 *
 * @author nelson85
 */
public class EnsdfRatio extends EnsdfQuantity
{
   EnsdfRatio(String value, String reference)
   {
     super(value, null, reference);
   }
}
