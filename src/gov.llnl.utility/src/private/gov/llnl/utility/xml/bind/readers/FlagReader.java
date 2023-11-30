/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind.readers;

import gov.llnl.utility.ClassUtilities;
import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(
        pkg = UtilityPackage.class,
        name = "flag",
        referenceable = true,
        contents = Reader.Contents.TEXT)
@Reader.TextContents(base = "util:boolean-optional")
public class FlagReader extends PrimitiveReaderImpl<Boolean>
{
  public FlagReader()
  {
    super(ClassUtilities.BOOLEAN_PRIMITIVE);
  }

  @Override
  public Boolean contents(ReaderContext context, String textContents)
  {
    if (textContents.isEmpty())
      return true;
    return (Boolean) primitive.valueOf(textContents);
  }

  @Override
  public TextContents getTextContents()
  {
    return this.getClass().getDeclaredAnnotation(Reader.TextContents.class);
  }

}
