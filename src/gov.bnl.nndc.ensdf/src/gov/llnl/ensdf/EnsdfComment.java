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
public class EnsdfComment extends EnsdfRecord
{

  public final char ctype;
  public final char continuation;
  public final String contents;

  EnsdfComment(EnsdfDataSet ds, char type, char continuation, String contents)
  {
    super(ds, type);
    this.ctype = type;
    this.continuation = continuation;
    this.contents = contents;
  }
  
  @Override
  public String toString()
  {
    return String.format("%s,%s,%s", this.ctype, this.continuation, this.contents);
  }
}
