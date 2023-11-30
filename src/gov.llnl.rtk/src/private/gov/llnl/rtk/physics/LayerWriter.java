/*
 * Copyright 2017, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.rtk.RtkPackage;
import gov.llnl.rtk.physics.Units;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.ObjectWriter;

/**
 *
 * @author nelson85
 */
@Internal
public class LayerWriter extends ObjectWriter<LayerImpl>
{
  public LayerWriter()
  {
    super(Options.NONE, "layer", RtkPackage.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, LayerImpl object) throws WriterException
  {
    if (object.getLabel() != null)
      attributes.add("label", object.getLabel());
  }

  @Override
  public void contents(LayerImpl object) throws WriterException
  {
    WriterBuilder wb = newBuilder();
    wb.element("thickness").writer(new Units.UnitWriter("length:cm")).put(object.getThickness());
    wb.element("geometry").writer(new GeometryWriter()).put(object.getGeometry());
    wb.element("material").writer(new MaterialWriter()).put(object.getMaterial());
  }

}
