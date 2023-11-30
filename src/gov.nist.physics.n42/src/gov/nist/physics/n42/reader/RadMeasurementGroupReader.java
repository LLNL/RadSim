/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.ComplexObject;
import gov.nist.physics.n42.data.RadMeasurementGroup;
import org.xml.sax.Attributes;

/**
 *
 * @author monterial1
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "RadMeasurementGroup",
        order = Reader.Order.SEQUENCE,
        cls = RadMeasurementGroup.class,
        typeName = "RadMeasurementGroupType")
@Reader.Attribute(name = "id", type = String.class, required = true)

@Reader.Attribute(name = "radMeasurementGroupUUID", type = String.class, required = false)
public class RadMeasurementGroupReader extends ObjectReader<RadMeasurementGroup>
{
  @Override
  public RadMeasurementGroup start(ReaderContext context, Attributes attr) throws ReaderException
  {
    RadMeasurementGroup out = new RadMeasurementGroup();
    ReaderUtilities.register(context, out, attr);
    out.setUUID(attr.getValue("radMeasurementGroupUUID"));
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {

    Reader.ReaderBuilder<RadMeasurementGroup> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("RadMeasurementGroupDescription").callString(RadMeasurementGroup::setDescription).optional();
    return builder.getHandlers();
  }

}
