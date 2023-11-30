/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.euclidean;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class VersorNGTest
{

  public VersorNGTest()
  {
  }

  @Test
  public void testOf_Vector3_double()
  {
    assertEquals(Versor.of(Vector3.AXIS_X, Math.PI).round(4), 
            Versor.of(0,1,0,0));
    assertEquals(Versor.of(Vector3Ops.scale(Vector3.AXIS_Y, 2), Math.PI).round(6), 
            Versor.of(0,0,1,0));
    assertEquals(Versor.of(Vector3.ZERO, Math.PI).round(6), 
            Versor.of(1,0,0,0));
    assertEquals(Versor.of(Vector3.AXIS_Z, 2*Math.PI).round(6), 
            Versor.of(-1,0,0,0));
  }

  @Test
  public void testToString()
  {
    Versor instance = Versor.of(Vector3.AXIS_X, Math.PI/2);
    String expResult = "Versor(0.707107,0.707107,0.000000,0.000000)";
    String result = instance.toString();
    assertEquals(result, expResult);
  }

  @Test
  public void testOf_4args()
  {
    Versor result = Versor.of(1, 1, 1, Math.PI / 4);
    assertEquals(result.getU(), 0.9238795325112867);
    assertEquals(result.getI(), 0.22094238269039454);
    assertEquals(result.getJ(), 0.22094238269039454);
    assertEquals(result.getK(), 0.22094238269039454);
  }

  @Test
  public void testInv()
  {
    Versor instance = Versor.of(Vector3.AXIS_X, Math.PI / 2);
    Versor result = instance.inv();
    assertEquals(result.getU(), 0.7071067811865476);
    assertEquals(result.getI(), -0.7071067811865475);
    assertEquals(result.getJ(), 0.0, 1e-10);
    assertEquals(result.getK(), 0.0, 1e-10);
  }

  @Test
  public void testRotate()
  {
    Vector3 v = Vector3.of(1, 2, 3);
    assertEquals(Versor.of(Vector3.AXIS_X, Math.PI / 2).rotate(v).roundTo(6),
            Vector3.of(1, -3, 2));
    assertEquals(Versor.of(Vector3.AXIS_Y, Math.PI / 2).rotate(v).roundTo(6),
            Vector3.of(3, 2, -1));
    assertEquals(Versor.of(Vector3.AXIS_Z, Math.PI / 2).rotate(v).roundTo(6),
            Vector3.of(-2, 1, 3));
    assertEquals(Versor.of(Vector3.AXIS_X, -Math.PI / 2).rotate(v).roundTo(6),
            Vector3.of(1, 3, -2));
    assertEquals(Versor.of(Vector3.AXIS_Y, -Math.PI / 2).rotate(v).roundTo(6),
            Vector3.of(-3, 2, 1));
    assertEquals(Versor.of(Vector3.AXIS_Z, -Math.PI / 2).rotate(v).roundTo(6),
            Vector3.of(2, -1, 3));
  }
  
}
