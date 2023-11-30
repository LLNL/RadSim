/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.io;

import java.net.URI;
import java.net.URL;

/**
 * Used to describe the location of a fault when reading an xml document.
 * 
 * @author nelson85
 */
public class PathLocation
{
  String file;
  int lineNumber;
  String section;

  public PathLocation(URI file, int lineNumber, String sectionName)
  {
    this.file = file.toString();
    this.lineNumber = lineNumber;
    this.section = sectionName;
  }

  public PathLocation(URL file, int lineNumber, String sectionName)
  {
    this.file = file.toString();
    this.lineNumber = lineNumber;
    this.section = sectionName;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    if (file != null)
      sb.append(file);
    else
      sb.append("unknown");

    if (lineNumber > 0)
      sb.append(":").append(lineNumber);

    if (section != null)
      sb.append("#").append(section);
    return sb.toString();
  }

}
