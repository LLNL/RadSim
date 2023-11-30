/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.regex.Pattern;

/**
 *
 * @author nelson85
 */
class TokenDef
{
  String regex;
  Pattern pattern;
  
  TokenDef(String regex)
  {
    this.regex = regex;
    this.pattern = Pattern.compile(this.regex);
  }
  
  @Override
  public boolean equals(Object o)
  {
    if (!(o instanceof TokenDef))
        return false;
      TokenDef o2 = (TokenDef)o;
      return o2.regex.equals(this.regex) &&
              o2.pattern.toString().equals(this.pattern.toString());
  }
}
