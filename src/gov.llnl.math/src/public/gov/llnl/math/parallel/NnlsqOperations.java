/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.parallel;

import gov.llnl.math.DoubleArray;
import gov.llnl.math.algebra.IndexSet;
import gov.llnl.math.algebra.Nnlsq;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author nelson85
 */
public class NnlsqOperations
{
//<editor-fold defaultstate="collapsed" desc="ProjectDemand">

  static class ProjectDemandAction implements Callable<Object>
  {
    double demand[];
    double beta[];
    Nnlsq.Input input;
    IndexSet set;
    int offset;
    int step;

    private ProjectDemandAction(double[] demand,
            double beta[],
            Nnlsq.Input input,
            IndexSet set,
            int offset,
            int step)
    {
      this.demand = demand;
      this.beta = beta;
      this.input = input;
      this.set = set;
      this.step = step;
      this.offset = offset;
    }

    @Override
    public Object call()
    {
      // w_Z= A_Z'*B- A_Z'*\beta
      int m = input.getNumVariables();
      for (int i = offset; i < set.size(); i += step)
      {
        int slot = set.get(i);
        demand[i] -= DoubleArray.multiplyInner(beta, 0, input.getRegressorWeighted(slot),
                input.getRegressorOffset(slot), m);
      }
      return 1;
    }
  }
//</editor-fold>

  public static void projectDemands(double[] demand, double[] beta, Nnlsq.Input input, IndexSet set)
  {
    try
    {
      // Parallel has overhead, thus only perform it when need
      if (set.size() > 20)
      {
        ParallelProcessor2.ParallelContext context = ParallelProcessor2.newContext();
        // Split up work
        int n = context.size();
        for (int i = 0; i < n; i++)
        {
          context.delegate(new ProjectDemandAction(demand, beta, input, set, i, n));
        }

        // Wait for work to complete
        context.waitForAll();
      }
      else
      {
        // Just perform the work in this thread
        (new ProjectDemandAction(demand, beta, input, set, 0, 1)).call();
      }
    }
    catch (ExecutionException ex)
    {
      throw new RuntimeException(ex);
    }
  }
}
