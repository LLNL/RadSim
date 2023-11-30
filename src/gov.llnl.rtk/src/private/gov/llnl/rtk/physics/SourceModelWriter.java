/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.rtk.RtkPackage;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.ObjectWriter;

/**
 *
 * @author nelson85
 */
@Internal
public class SourceModelWriter extends ObjectWriter<SphericalModel>
{
  public SourceModelWriter()
  {
    super(Options.NONE, "sourceModel", RtkPackage.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, SphericalModel object) throws WriterException
  {
    if (object.getTitle() != null)
      attributes.add("title", object.getTitle());
  }

  @Override
  public void contents(SphericalModel object) throws WriterException
  {
    WriterBuilder wb = newBuilder();
    wb.element("geometry").writer(new GeometryWriter()).put(object.getGeometry());
    WriteObject<LayerImpl> wo = wb.element("layer").writer(new LayerWriter());
    int i = 0;
    for (Layer layer : object.getLayers())
      wo.put(layer); //.id(String.format("layer." + i++));
  }

}
