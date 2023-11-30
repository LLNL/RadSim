/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.UtilityPackage;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(name = "AttributedString", pkg = UtilityPackage.class,
        contents = Reader.Contents.TEXT, cls = String.class)
@Reader.AnyAttribute(processContents = Reader.ProcessContents.Skip)
public class AttributedStringReader extends ObjectReader<String>
{
  @Override
  public String contents(ReaderContext context, String contents)
  {
    return contents;
  }
}
