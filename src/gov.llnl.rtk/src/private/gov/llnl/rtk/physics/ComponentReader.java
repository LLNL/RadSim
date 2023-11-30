/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
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
@Reader.Declaration(pkg = RtkPackage.class, name = "component",
        cls = Component.class,
        order = Reader.Order.OPTIONS,
        referenceable = true)
@Reader.AnyAttribute(processContents = Reader.ProcessContents.Skip)
public class ComponentReader extends ObjectReader<Component>
{

  @Override
  public Component start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    ComponentImpl entry = new ComponentImpl();
    String nuclideName = attributes.getValue("nuclide");
    Nuclide nuclide = Nuclides.get(nuclideName);
    entry.setNuclide(nuclide);
    return entry;
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    ReaderBuilder<ComponentImpl> rb = newBuilder(ComponentImpl.class);
    rb.element("doseFraction").callDouble(ComponentImpl::setDoseFraction);
    rb.element("massFraction").callDouble(ComponentImpl::setMassFraction);
    rb.element("activity")
            .reader(new Units.UnitReader(PhysicalProperty.ACTIVITY))
            .call(ComponentImpl::setActivity);
    return rb.getHandlers();
  }

}
