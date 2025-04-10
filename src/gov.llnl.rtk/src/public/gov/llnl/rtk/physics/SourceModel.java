/*
 * Copyright 2019, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.utility.Expandable;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author nelson85
 */
@ReaderInfo(SourceModelReader.class)
@WriterInfo(SourceModelWriter.class)
public interface SourceModel extends Expandable, Serializable
{
  Geometry getGeometry();

  List<? extends Layer> getLayers();

  String getTitle();

  default int size()
  {
    return this.getLayers().size();
  }

  default Quantity getRadius()
  {
    double out = 0;
    for (Layer l : this.getLayers())
    {
      out += l.getThickness().as(PhysicalProperty.LENGTH);
    }
    return Quantity.of(out, PhysicalProperty.LENGTH);
  }
}
