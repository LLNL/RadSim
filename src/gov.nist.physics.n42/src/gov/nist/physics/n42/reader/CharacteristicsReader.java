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
import gov.nist.physics.n42.data.CharacteristicGroup;
import gov.nist.physics.n42.data.Characteristics;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "Characteristics",
        order = Reader.Order.FREE, 
        cls = Characteristics.class,
        typeName = "CharacteristicsType")
@Reader.Attribute(name="id", type=String.class, required=false)
public class CharacteristicsReader extends ObjectReader<Characteristics>
{
  @Override
  public Characteristics start(ReaderContext context, Attributes attr) throws ReaderException
  {
    Characteristics out = new Characteristics();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<Characteristics> builder = this.newBuilder();
    builder.element("Characteristic")
            .call(Characteristics::addCharacteristic, Characteristic.class)
            .optional().unbounded();
    builder.element("CharacteristicGroup")
            .call(Characteristics::addCharacteristicGroup, CharacteristicGroup.class)
            .optional().unbounded();
    return builder.getHandlers();
  }

}
