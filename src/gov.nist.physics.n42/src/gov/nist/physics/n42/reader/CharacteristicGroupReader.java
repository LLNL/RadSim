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
import gov.nist.physics.n42.data.Characteristic;
import gov.nist.physics.n42.data.CharacteristicGroup;
import gov.nist.physics.n42.data.ComplexObject;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "CharacteristicGroup",
        order = Reader.Order.SEQUENCE,
        cls = CharacteristicGroup.class,
        typeName = "CharacteristicGroupType")
@Reader.Attribute(name = "id", type = String.class, required = false)
@Reader.Attribute(name = "groupOutOfLimits", type = String.class)
public class CharacteristicGroupReader extends ObjectReader<CharacteristicGroup>
{
  @Override
  public CharacteristicGroup start(ReaderContext context, Attributes attr) throws ReaderException
  {
    CharacteristicGroup out = new CharacteristicGroup();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<CharacteristicGroup> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("CharacteristicGroupName").callString(CharacteristicGroup::setName);
    builder.element("Characteristic").call(CharacteristicGroup::addCharacteristic, Characteristic.class).optional().unbounded();
    return builder.getHandlers();
  }

}
