/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind.readers;

import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;

/**
 *
 * @author nelson85
 */
//@ReaderExtern
@Reader.Declaration(
        pkg = UtilityPackage.class,
        name = "string",
        referenceable = true,
        contents = Reader.Contents.TEXT,
        cls = String.class)
@Reader.TextContents(base = "xs:string")
public class StringContents extends ContentsReader<String>
{

  @Override
  public String contents(ReaderContext context, String textContents)
  {
    return textContents;
  }

}
