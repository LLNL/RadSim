/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.nist.physics.n42.data.Characteristic;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.ComplexObject;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "Characteristic",
        order = Reader.Order.SEQUENCE,
        cls = Characteristic.class,
        typeName = "CharacteristicType")
@Reader.Attribute(name = "id", type = String.class, required = false)
@Reader.Attribute(name = "valueOutOfLimits", type = String.class)
public class CharacteristicReader extends ObjectReader<Characteristic>
{
  @Override
  public Characteristic start(ReaderContext context, Attributes attr) throws ReaderException
  {
    Characteristic out = new Characteristic();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<Characteristic> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("CharacteristicName").callString(Characteristic::setName);
    builder.element("CharacteristicValue").callString(Characteristic::setValue);
    builder.element("CharacteristicValueUnits").callString(Characteristic::setValueUnits);
    builder.element("CharacteristicValueDataClassCode").callString(Characteristic::setValueDataClassCode);
    return builder.getHandlers();
  }

}
