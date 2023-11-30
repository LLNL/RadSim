/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind.readers;

import gov.llnl.utility.ArrayEncoding;
import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomBuilder;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.llnl.utility.xml.bind.SchemaBuilder;
import java.text.ParseException;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(
        pkg = UtilityPackage.class,
        name = "longArray",
        referenceable = true,
        contents = Reader.Contents.TEXT,
        cls= long[].class)
@Reader.TextContents(base = "util:string-attr")
public class LongArrayContents extends ObjectReader<long[]>
{

  @Override
  public void createSchemaType(SchemaBuilder builder) throws ReaderException
  {
  }

  @Override
  public DomBuilder createSchemaElement(SchemaBuilder builder, String name, DomBuilder group, boolean options)
  {
    return group.element("xs:element")
            .attr("name", name)
            .attr("type", "util:string-attr");
  }

  @Override
  public long[] contents(ReaderContext context, String textContents) throws ReaderException
  {
    try
    {
      return ArrayEncoding.decodeLongs(textContents);
    }
    catch (ParseException ex)
    {
      throw new ReaderException("Error decoding long[]\n " + textContents, ex);
    }
  }

}
