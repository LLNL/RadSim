/*
 * Copyright (c) 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.utility.ExpandableObject;
import java.util.ArrayList;
import java.util.List;

/**
 * FIXME rename this. This is a 1dm model for an object
 *
 * @author nelson85
 */
public class SourceModelImpl extends ExpandableObject implements SourceModel
{
  private String title;
  private Geometry geometry = Geometry.newSpherical();
  final ArrayList<LayerImpl> layers = new ArrayList<>();
  LayerImpl last = null;

  public void setTitle(String title)
  {
    this.title = title;
  }

  public void setGeometry(Geometry geometry)
  {
    this.geometry = geometry;
  }

  public void addLayer(Layer layer)
  {
    LayerImpl li;
    if (layer == null)
      li = new LayerImpl(geometry, last);
    else
      li = new LayerImpl(layer);
    li.previous = last;
    li.update();
    this.layers.add(li);
    last = li;
  }

  @Override
  public String getTitle()
  {
    return this.title;
  }

  @Override
  public Geometry getGeometry()
  {
    return geometry;
  }

  @Override
  public List<? extends Layer> getLayers()
  {
    return layers;
  }

  /**
   * Makes sure all the fields are consistent for a model.
   *
   * This requires all layers to be LayerImpl.
   *
   * @throws ClassCastException if one of the layers is not a LayerImpl
   */
  public void normalize()
  {
    for (LayerImpl layer : layers)
    {
      layer.update();
    }
  }

}
