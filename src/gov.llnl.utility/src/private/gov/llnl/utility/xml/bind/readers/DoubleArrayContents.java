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
        name = "doubleArray",
        referenceable = true,
        contents = Reader.Contents.TEXT, 
        cls = double[].class)
@Reader.TextContents(base="xs:string")
public class DoubleArrayContents extends ObjectReader<double[]>
{

  @Override
  public double[] contents(ReaderContext context, String textContents) throws ReaderException
  {
    try
    {
      return ArrayEncoding.decodeDoubles(textContents);
    }
    catch (ParseException ex)
    {
      throw new ReaderException("Error decoding double[]\n " + textContents, ex);
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
