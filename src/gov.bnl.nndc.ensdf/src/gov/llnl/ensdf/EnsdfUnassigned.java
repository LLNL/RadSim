/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class EnsdfUnassigned extends EnsdfEmissions implements Serializable
{

  public EnsdfUnassigned(EnsdfDataSet dataSet)
  {
    super(dataSet, 'Z');
  }

  @Override
  public String toString()
  {
    return String.format("Unassigned: alpha=%d beta=%d delayed=%d ec=%d gamma=%d",
            this.alpha.size(),
            this.beta.size(),
            this.particles.size(),
            this.ec.size(),
            this.gamma.size());
  }
}
