/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.algebra;

import gov.llnl.math.DoubleArray;
import gov.llnl.math.matrix.MatrixFactory;
import gov.llnl.math.matrix.MatrixOps;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Test code for Nnlsq.
 */
public class NnlsqNGTest
{

  /**
   * Test of initialize method, of class Nnlsq.
   */
  @Test
  public void testInitialize()
  {
    // Tested end to end
  }

  /**
   * Test of solve method, of class Nnlsq.
   */
  @Test
  public void testSolve_0args()
  {
    // Tested end to end
  }

  /**
   * Test of solve method, of class Nnlsq.
   */
  @Test
  public void testEndToEnd()
  {
    Random r = new Random();
    r.setSeed(0);
    Nnlsq.InputDoubleMatrix input = new Nnlsq.InputDoubleMatrix();
    input.use = IntStream.range(0, 10).toArray();
    input.regressors = MatrixFactory.newColumnMatrix(20, 10);
    MatrixOps.fill(input.regressors, r::nextDouble);
    input.regressand = new double[20];
    DoubleArray.addAssignScaled(input.regressand, input.regressors.copyColumn(0), 1.0);
    DoubleArray.addAssignScaled(input.regressand, input.regressors.copyColumn(2), 4.0);
    DoubleArray.addAssignScaled(input.regressand, input.regressors.copyColumn(5), 8.0);
    input.regressandWeighted = input.regressand;
    input.regressorsWeighted = input.regressors;
    NnlsqImpl instance = new NnlsqImpl();
    instance.initialize(input);
    instance.solve(input);
    ArrayList<Nnlsq.Datum> solution = new ArrayList<>();
    instance.getSolution().forEach(solution::add);
    solution.sort((p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
    assertEquals(solution.get(0).getId(), 0);
    assertEquals(solution.get(1).getId(), 2);
    assertEquals(solution.get(2).getId(), 5);
    assertEquals(instance.getMSE(), 0, 0.00001);
    assertTrue(DoubleArray.equivalent(input.regressand, instance.getProjection()));
  }

  /**
   * Test of getSolution method, of class Nnlsq.
   */
  @Test
  public void testGetSolution()
  {
    // tested end to end
  }

  /**
   * Test of getMSE method, of class Nnlsq.
   */
  @Test
  public void testGetMSE()
  {
    // tested end to end
  }

  /**
   * Test of getProjection method, of class Nnlsq.
   */
  @Test
  public void testGetProjection()
  {
    // Tested end to end
  }

}
