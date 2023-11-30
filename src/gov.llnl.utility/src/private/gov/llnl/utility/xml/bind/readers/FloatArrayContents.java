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
import gov.llnl.utility.xml.bind.ObjectStringReader;
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
        name = "floatArray",
        referenceable = true,
        contents = Reader.Contents.TEXT, 
        cls = float[].class)
@Reader.TextContents(base="xs:string")
public class FloatArrayContents extends ObjectStringReader<float[]>
{

  @Override
  public float[] contents(ReaderContext context, String textContents) throws ReaderException
  {
    try
    {
      return ArrayEncoding.decodeFloats(textContents);
    }
    catch (ParseException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void createSchemaType(SchemaBuilder builder) throws ReaderException
  {
  }

  @Override
  public DomBuilder createSchemaElement(SchemaBuilder builder, String name, DomBuilder group, boolean options)
  {
    return group.element("xs:element").attr("name", name).attr("type", "util:string-attr");
  }

}
