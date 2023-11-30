/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.io;

import java.util.LinkedList;

/**
 * Utility to report a collection of paths when reporting faults.
 * 
 * @author nelson85
 */
public class PathTrace
{
  LinkedList<PathLocation> trace;

  void add(PathLocation location)
  {
    if (trace == null)
      trace = new LinkedList<>();
    trace.add(location);
  }

  @Override
  public String toString()
  {
    if (trace == null)
      return "";
    StringBuilder sb = new StringBuilder();
    for (PathLocation entry : this.trace)
    {
      sb.append("        in ").append(entry.toString()).append("\n");
    }
    return sb.toString();
  }

}
