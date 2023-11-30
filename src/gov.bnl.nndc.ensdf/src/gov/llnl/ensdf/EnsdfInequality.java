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
public class EnsdfInequality extends EnsdfQuantity
{

  public enum Relationship
  {
    GT, LT, GE, LE, AP;
  }

  public EnsdfInequality(String relationship, String value, String unc, String reference)
  {
    super(value, unc, reference);
  }
}
