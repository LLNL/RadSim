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
public class ComponentWriter extends ObjectWriter<Component>
{
  public ComponentWriter()
  {
    super(Options.REFERENCEABLE, "entry", RtkPackage.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, Component object) throws WriterException
  {
    attributes.add("nuclide", object.getNuclide().getName());
  }

  @Override
  public void contents(Component object) throws WriterException
  {
    WriterBuilder wb = newBuilder();
    if (object.getDoseFraction() > 0)
      wb.element("doseFraction").putDouble(object.getDoseFraction());
    if (object.getMassFraction() > 0)
      wb.element("massFraction").putDouble(object.getMassFraction());
    //   if (getContext().getProperty(LibraryWriter.WRITE_ACTIVITY, Boolean.class, true))
    wb.element("activity").writer(new Units.UnitWriter("activity:Bq")).put(object.getActivity());
  }

}
