/*
 * Copyright (c) 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.utility.ExpandableObject;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * FIXME rename this.  This is a 1dm model for an object
 *
 * @author nelson85
 */
@ReaderInfo(SourceModelReader.class)
@WriterInfo(SourceModelWriter.class)
public class SphericalModel extends ExpandableObject
{
  private String title;
  private Geometry geometry;
  private final ArrayList<Layer> layers = new ArrayList<>();

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
    this.layers.add(layer);
  }

  public String getTitle()
  {
    return this.title;
  }

  public Geometry getGeometry()
  {
    return geometry;
  }

  public List<Layer> getLayers()
  {
    return layers;
  }

  public double getRadius()
  {
    double out = 0;
    for (Layer l : this.layers)
    {
      out += l.getThickness();
    }
    return out;
  }

  public int size()
  {
    return this.layers.size();
  }

}
