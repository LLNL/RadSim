/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class EnsdfQuantity implements Serializable
{

  public String field;
  public String unc;
  public String reference;

  public EnsdfQuantity(String value, String unc)
  {
    this(value, unc, null);
    if (this.field == null)
      throw new RuntimeException();
  }

  public EnsdfQuantity(String field, String unc, String reference)
  {
    this.reference = reference;
    if (field == null || field.isBlank())
      this.field = "";
    else
      this.field = field.strip();
    if (unc == null || unc.isBlank())
      this.unc = null;
    else
      this.unc = unc.strip();
  }

  @Override
  public String toString()
  {
    return String.format("Quantity(%s,%s)", this.field, this.unc);
  }

  final static Pattern pattern1 = Pattern.compile("[0-9]*[.]?[0-9]*(?:[Ee]?[+-]?[0-9]+)?");
  final static Pattern pattern2 = Pattern.compile("\\(([0-9]*[.]?[0-9]*(?:[Ee]?[+-]?[0-9]+)?)\\)");

  public double toDouble()
  {
    Matcher matcher = pattern1.matcher(field);
    if (matcher.matches())
      return Double.parseDouble(this.field);
    matcher = pattern2.matcher(field);
    if (matcher.matches())
      return Double.parseDouble(matcher.group(1));
    System.out.println("Bad quantity " + field);
    return 0;
  }

  public boolean isSpecified()
  {
    return !field.isBlank();
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 11 * hash + Objects.hashCode(this.field);
    hash = 11 * hash + Objects.hashCode(this.unc);
    return hash;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final EnsdfQuantity other = (EnsdfQuantity) obj;
    return Objects.equals(this.field, other.field)
            && Objects.equals(this.unc, other.unc);
  }

}
