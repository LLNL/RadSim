/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.Reader.Attribute;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.Quantity;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "Quantity",
        contents = Reader.Contents.TEXT,
        cls = Quantity.class,
        typeName = "QuantityType")
@Attribute(name="units", type=String.class)
public class QuantityReader extends ObjectReader<Quantity>
{
  @Override
  public Quantity start(ReaderContext context, Attributes attr)
  {
    Quantity out = new Quantity();
    out.setUnits(attr.getValue("units"));
    return out;
  }

  @Override
  public Quantity contents(ReaderContext context, String string)
  {
    getObject(context).setValue(Double.parseDouble(string));
    return null;
  }

}