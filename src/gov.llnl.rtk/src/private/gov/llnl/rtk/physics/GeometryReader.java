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

@Reader.Declaration(pkg = RtkPackage.class, name = "geometry", cls = Geometry.class,
        order = Reader.Order.OPTIONS, referenceable = true)
@Reader.Attribute(name = "type", type = String.class)
public class GeometryReader extends ObjectReader<Geometry>
{
  @Override
  public Geometry start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    Geometry object = new Geometry();
    String typeString = attributes.getValue("type");
    Geometry.Type type = Geometry.Type.valueOf(typeString.toUpperCase());
    object.setType(type);
    return object;
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    ReaderBuilder<Geometry> builder = this.newBuilder();
    builder.element("extent1").reader(new Units.UnitReader(PhysicalProperty.LENGTH)).call(Geometry::setExtent1);
    builder.element("extent2").reader(new Units.UnitReader(PhysicalProperty.LENGTH)).call(Geometry::setExtent2);
    return builder.getHandlers();
  }

}
