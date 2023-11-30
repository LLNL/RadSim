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
public class LayerImpl implements Layer
{
  String label;
  Material material;
  Geometry geometry;
  double thickness;
  double mass;

  /**
   * Get the age of the material.
   *
   * @return get age in seconds.
   */
  @Override
  public double getAge()
  {
    return material.getAge();
  }

  @Override
  public double getDensity()
  {
    return material.getDensity();
  }

  @Override
  public Geometry getGeometry()
  {
    return geometry;
  }

  public void setGeometry(Geometry geometry)
  {
    this.geometry = geometry;
  }

  @Override
  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public void setMass(double mass)
  {
    this.mass = mass;
  }

  @Override
  public double getMass()
  {
    return mass;
  }

  @Override
  public Material getMaterial()
  {
    return material;
  }

  public void setMaterial(Material material)
  {
    this.material = material;
  }

  @Override
  public double getThickness()
  {
    return thickness;
  }

  public void setThickness(double th)
  {
    thickness = th;
  }
}
