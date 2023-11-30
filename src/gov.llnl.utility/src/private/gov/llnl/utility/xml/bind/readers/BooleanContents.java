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

/**
 *
 * @author nelson85
 */
@Reader.Declaration(
        pkg = UtilityPackage.class,
        name = "boolean",
        referenceable = true,
        contents = Reader.Contents.TEXT)
@Reader.TextContents(base = "xs:boolean")
public class BooleanContents extends PrimitiveReaderImpl<Boolean>
{
  public BooleanContents()
  {
    super(ClassUtilities.BOOLEAN_PRIMITIVE);
  }

}
