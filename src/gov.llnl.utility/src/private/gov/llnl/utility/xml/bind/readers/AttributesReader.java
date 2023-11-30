/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind.readers;

import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.xml.bind.SchemaBuilderUtilities;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomBuilder;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.Reader.Order;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.llnl.utility.xml.bind.SchemaBuilder;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(
        pkg = UtilityPackage.class,
        name = "attributes",
        order = Order.ALL,
        cls = Attributes.class)
@Reader.AnyAttribute(processContents = Reader.ProcessContents.Skip)
@Reader.TextContents(base = "util:empty-attr")
public class AttributesReader extends ObjectReader<Attributes>
{

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    return null;
  }

  @Override
  public Attributes start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    return attributes;
  }

  @Override
  public void createSchemaType(SchemaBuilder builder) throws ReaderException
  {
  }

  @Override
  public DomBuilder createSchemaElement(SchemaBuilder builder, String name, DomBuilder group, boolean topLevel)
  {
    return SchemaBuilderUtilities.createSchemaElementSimple(this, builder, name, group, topLevel);
  }

}
