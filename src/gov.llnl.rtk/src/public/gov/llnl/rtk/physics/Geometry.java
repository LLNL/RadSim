/* 
 * Copyright (c) 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

/**
 *
 * @author nelson85
 */
public class Geometry
{
  public enum Type
  {
    SPHERICAL,
    SPHERE_CAP,
    CYLINDER_FLAT,
    CYLINDER_ROUND,
    SLAB,
    CONE,
    BACKGROUND,
    MIXED
  }
  private Type type;
  private double extent1;
  private double extent2;

  public Geometry()
  {
  }

  private Geometry(Type type, double e1, double e2)
  {
    this.type = type;
    this.extent1 = e1;
    this.extent2 = e2;
  }

  public static Geometry newSpherical()
  {
    return new Geometry(Type.SPHERICAL, 0, 0);
  }

  /**
   * @return the type
   */
  public Type getType()
  {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(Type type)
  {
    this.type = type;
  }

  /**
   * @return the extent1
   */
  public double getExtent1()
  {
    return extent1;
  }

  /**
   * @param extent1 the extent1 to set
   */
  public void setExtent1(double extent1)
  {
    this.extent1 = extent1;
  }

  /**
   * @return the extent2
   */
  public double getExtent2()
  {
    return extent2;
  }

  /**
   * @param extent2 the extent2 to set
   */
  public void setExtent2(double extent2)
  {
    this.extent2 = extent2;
  }
}
