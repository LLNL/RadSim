/*
 * Copyright 2017, Lawrence Livermore National Security, LLC.
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
public class MaterialWriter extends ObjectWriter<MaterialImpl>
{
  public MaterialWriter()
  {
    super(Options.NONE, "material", RtkPackage.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, MaterialImpl object) throws WriterException
  {
  }

  @Override
  public void contents(MaterialImpl object) throws WriterException
  {
    if (object == null)
      throw new RuntimeException("Null material");
    WriterBuilder wb = newBuilder();
    wb.element("age")
            .writer(new Units.UnitWriter("time:a"))
            .put(object.getAge());
    wb.element("density")
            .writer(new Units.UnitWriter("density:g/cm3"))
            .put(object.getDensity());
    WriteObject<Component> wo = wb.element("component")
            .writer(new ComponentWriter());
    for (Component entry : object)
      wo.put(entry);
  }

}
