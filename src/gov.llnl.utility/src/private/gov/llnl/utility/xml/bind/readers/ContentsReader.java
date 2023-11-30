/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind.readers;

import gov.llnl.utility.xml.bind.SchemaBuilderUtilities;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomBuilder;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.llnl.utility.xml.bind.SchemaBuilder;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
public abstract class ContentsReader<Obj> extends ObjectReader<Obj>
{

  @Override
  public Obj start(ReaderContext context, Attributes attr) throws ReaderException
  {
    return null;
  }

  @Override
  public abstract Obj contents(ReaderContext context, String textContents) throws ReaderException;

  @Override
  public final void createSchemaType(SchemaBuilder builder) throws ReaderException
  {
  }

  @Override
  public DomBuilder createSchemaElement(SchemaBuilder builder, String name, DomBuilder group, boolean topLevel)
  {
    return SchemaBuilderUtilities.createSchemaElementSimple(this, builder, name, group, topLevel);
  }

}
