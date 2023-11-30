/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.spline;

import gov.llnl.utility.annotation.Debug;
import gov.llnl.utility.annotation.Internal;

/**
 *
 * @author nelson85
 */
@Internal
class ControlConstraint
{
  double x;
  double y;
  double m;
  boolean constrainedSlope = false;

  ControlConstraint(double x, double y, double m, boolean b)
  {
    this.x = x;
    this.y = y;
    this.m = m;
    this.constrainedSlope = b;
  }

}
