/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class EnsdfTimeQuantity implements Serializable
{

  static final Pattern PATTERN = Pattern.compile("([0-9E.+-]+) +([A-z]+)");
  static final HashMap<String, Double> UNITS = new HashMap<>();

  String field;
  double value;
  String unit;
  String unc;

  static
  {
    UNITS.put("EV", 6.582e-16);
    UNITS.put("KEV", 6.582e-19);
    UNITS.put("MEV", 6.582e-22);
    UNITS.put("FS", 1e-15);
    UNITS.put("PS", 1e-12);
    UNITS.put("NS", 1e-9);
    UNITS.put("US", 1e-6);
    UNITS.put("MS", 1e-3);
    UNITS.put("S", 1.0);
    UNITS.put("M", 60.0);
    UNITS.put("H", 3600.0);
    UNITS.put("D", 86400.0);
    UNITS.put("Y", 31536000.0);
  }

  public EnsdfTimeQuantity(String field, String unc)
  {
    if (field == null || field.isBlank())
    {
      this.field = "";
      return;
    }

    field = field.strip();
    if (field.equals("STABLE"))
    {
      this.field = field;
      this.value = Double.POSITIVE_INFINITY;
    } else if (field.equals("?"))
    {
      this.field = field;
      this.value = 0;
    } else
    {
      Matcher m = PATTERN.matcher(field);
      if (!m.matches())
      {
        throw new RuntimeException("Unable to parse \"" + field + "\"");
      }
      Double unitValue = UNITS.get(m.group(2));
      if (unitValue == null)
        throw new RuntimeException("Unknown units " + m.group(2));
      this.field = field;
      boolean isEV = false;
      if(m.group(2).length() >= 2)
        isEV = m.group(2).substring(m.group(2).length()-2,m.group(2).length()).contentEquals("EV");
      if (isEV)
        this.value = unitValue / Double.parseDouble(m.group(1));
      else 
        this.value = Double.parseDouble(m.group(1)) * unitValue;
      this.unit = m.group(2);
    }
    if (unc == null || unc.isBlank())
      this.unc = null;
    else
      this.unc = unc.strip();
  }

  @Override
  public String toString()
  {
    return String.format("TimeQuantity(%s,%s)", this.field, this.unc);
  }
  
  public boolean isSpecified()
  {
    return !this.field.isBlank();
  }

  @Override
  public int hashCode()
  {
    int hash = 3;
    hash = 37 * hash + Objects.hashCode(this.field);
    hash = 37 * hash + Objects.hashCode(this.unc);
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
    final EnsdfTimeQuantity other = (EnsdfTimeQuantity) obj;
    if (this.toDouble() - other.toDouble() < this.toDouble()*0.0001)
        return true;
    if (!Objects.equals(this.field, other.field))
      return false;
    if (!Objects.equals(this.unc, other.unc))
      return false;
    return true;
  }

  public double toDouble()
  {
    return this.value;
  }

}
