/*
 * Copyright 2017, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.geo;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author forsyth2
 */
public class CoordinateUtilitiesTests
{
  @Test
  public void testFindOrthogonalPoint1()
  {
    // Line segment from (4,2) to (10,8) (bottom left to top right).
    double[] a =
    {
      4, 2
    };
    double[] b =
    {
      10, 8
    };
    // So, the midpoint is (7, 5).
    double px = 7;
    double py = 5;
    // Our expected point q is (8,4).
    double expected_qx = 8;
    double expected_qy = 4;
    // dist(q,p) = sqrt((8-7)^2 + (4-5)^2) = sqrt(1+1) = sqrt(2)
    double expected_distance = Math.sqrt(2);
    double[] q = CoordinateUtilities.findOrthogonalPoint(a, b, expected_distance);
    double delta_x = expected_qx - px;
    double delta_y = expected_qy - py;
    double distance = Math.sqrt((delta_x * delta_x) + (delta_y * delta_y));
    assertEquals(distance, expected_distance, 0.01);
    assertEquals(q[0], expected_qx, 0.01);
    assertEquals(q[1], expected_qy, 0.01);
  }

  @Test
  public void testFindOrthogonalPoint2()
  {
    // Line segment from (4,2) to (10,8) (bottom left to top right).
    double[] a =
    {
      4, 2
    };
    double[] b =
    {
      10, 8
    };
    // So, the midpoint is (7, 5).
    double px = 7;
    double py = 5;
    // Our expected point q is (6,6).
    double expected_qx = 6;
    double expected_qy = 6;
    // dist(q,p) = sqrt((6-7)^2 + (6-5)^2) = sqrt(1+1) = sqrt(2)
    // expected_distance is negative so that we go n meters the other direction.
    double expected_distance = -Math.sqrt(2);
    double[] q = CoordinateUtilities.findOrthogonalPoint(a, b, expected_distance);
    double delta_x = expected_qx - px;
    double delta_y = expected_qy - py;
    double distance = Math.sqrt((delta_x * delta_x) + (delta_y * delta_y));
    assertEquals(distance, Math.abs(expected_distance), 0.01);
    assertEquals(q[0], expected_qx, 0.01);
    assertEquals(q[1], expected_qy, 0.01);
  }

  @Test
  public void testFindOrthogonalPoint3()
  {
    // Line segment from (4,8) to (10,2) (top left to bottom right).
    double[] a =
    {
      4, 8
    };
    double[] b =
    {
      10, 2
    };
    // So, the midpoint is (7, 5).
    double px = 7;
    double py = 5;
    // Our expected point q is (6,4).
    double expected_qx = 6;
    double expected_qy = 4;
    // dist(q,p) = sqrt((6-7)^2 + (4-5)^2) = sqrt(1+1) = sqrt(2)
    double expected_distance = Math.sqrt(2);
    double[] q = CoordinateUtilities.findOrthogonalPoint(a, b, expected_distance);
    double delta_x = expected_qx - px;
    double delta_y = expected_qy - py;
    double distance = Math.sqrt((delta_x * delta_x) + (delta_y * delta_y));
    assertEquals(distance, expected_distance, 0.01);
    assertEquals(q[0], expected_qx, 0.01);
    assertEquals(q[1], expected_qy, 0.01);
  }

  @Test
  public void testFindOrthogonalPoint4()
  {
    // Line segment from (10,8) to (4,2) (top right to bottom left).
    double[] a =
    {
      10, 8
    };
    double[] b =
    {
      4, 2
    };
    // So, the midpoint is (7, 5).
    double px = 7;
    double py = 5;
    // Our expected point q is (6,6).
    double expected_qx = 6;
    double expected_qy = 6;
    // dist(q,p) = sqrt((6-7)^2 + (6-5)^2) = sqrt(1+1) = sqrt(2)
    double expected_distance = Math.sqrt(2);
    double[] q = CoordinateUtilities.findOrthogonalPoint(a, b, expected_distance);
    double delta_x = expected_qx - px;
    double delta_y = expected_qy - py;
    double distance = Math.sqrt((delta_x * delta_x) + (delta_y * delta_y));
    assertEquals(distance, expected_distance, 0.01);
    assertEquals(q[0], expected_qx, 0.01);
    assertEquals(q[1], expected_qy, 0.01);
  }
}
