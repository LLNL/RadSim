/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind.readers;

import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(
        pkg = UtilityPackage.class,
        name = "float",
        referenceable = true,
        contents = Reader.Contents.TEXT,
        cls = Float.class)
@Reader.TextContents(base = "xs:double")
public class FloatContents extends ObjectReader<Float>
{

  @Override
  @TextContents(base = "xs:double")
  public Float contents(ReaderContext context, String textContents) throws ReaderException
  {
    return Float.parseFloat(textContents);
  }

}
