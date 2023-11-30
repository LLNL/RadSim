/*
 * Copyright 2017, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.rtk.RtkPackage;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Internal

@Reader.Declaration(pkg = RtkPackage.class, name = "material", cls = MaterialImpl.class,
        order = Reader.Order.SEQUENCE, referenceable = true)
public class MaterialReader extends ObjectReader<MaterialImpl>
{

  @Override
  public MaterialImpl start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    return new MaterialImpl();
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    ReaderBuilder<MaterialImpl> builder = this.newBuilder();
    builder.element("age")
            .reader(new Units.UnitReader(PhysicalProperty.TIME))
            .call(MaterialImpl::setAge).optional();
    builder.element("density")
            .reader(new Units.UnitReader(PhysicalProperty.DENSITY))
            .call(MaterialImpl::setDensity);
    builder.element("component")
            .reader(new ComponentReader())
            .call(MaterialImpl::addEntry).unbounded();
    return builder.getHandlers();
  }


}
