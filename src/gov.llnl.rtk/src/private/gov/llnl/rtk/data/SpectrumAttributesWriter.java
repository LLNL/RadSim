/*
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.data;

import gov.llnl.rtk.RtkPackage;
import gov.llnl.rtk.data.SpectrumAttributes;
import gov.llnl.rtk.physics.Quantity;
import gov.llnl.utility.Expandable;
import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.ObjectWriter;
import java.io.Serializable;
import java.util.function.Predicate;

/**
 *
 * @author nelson85
 */
class SpectrumAttributesWriter extends ObjectWriter<Expandable>
{
  public SpectrumAttributesWriter()
  {
    super(Options.NONE, "attributes", RtkPackage.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, Expandable object) throws WriterException
  {
  }

  @Override
  @SuppressWarnings(value = "unchecked")
  public void contents(Expandable object) throws WriterException
  {
    Predicate<String> exclude = this.getContext().getProperty(SpectrumAttributes.WRITER_EXCLUDE, Predicate.class, null);
    WriterBuilder wb = newBuilder();
    for (String keys : object.getAttributes().keySet())
    {
      if (exclude != null && exclude.test(keys))
        continue;
      String[] tokens = keys.split("#");
      if (tokens.length < 2)
        continue;
      String localName = tokens[1];
      Serializable value = object.getAttribute(keys);
      if (value instanceof Quantity)
        wb.element(localName).contents(Quantity.class).put(value);
        else
        wb.element(localName).putContents(value);
    }
  }

}
