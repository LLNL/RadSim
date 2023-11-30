/*
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.rtk.RtkPackage;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = RtkPackage.class, name = "quantity", 
        contents = Reader.Contents.TEXT, cls = Quantity.class)
@Reader.Attribute(name = "units", type = String.class)
public class QuantityReader extends ObjectReader<Quantity>
{
 
  @Override
  public Quantity start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    context.setState(attributes.getValue("units"));
    return null;
  }

  @Override
  public Quantity contents(ReaderContext context, String textContents) throws ReaderException
  {
    return Quantity.of(Double.parseDouble(textContents.trim()), (String)context.getState());
  }
}
