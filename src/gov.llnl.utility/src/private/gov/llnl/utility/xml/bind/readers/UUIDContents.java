/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind.readers;

import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import java.util.UUID;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(
        pkg = UtilityPackage.class,
        name = "uuid",
        referenceable = true,
        contents = Reader.Contents.TEXT,
        cls = UUID.class)
@Reader.TextContents(base = "xs:string")
public class UUIDContents extends ContentsReader<UUID>
{

  @Override
  public UUID contents(ReaderContext context, String textContents) throws ReaderException
  {
    try
    {
      return UUID.fromString(textContents);
    }
    catch (IllegalArgumentException ex)
    {
      throw new ReaderException("Unable to parse UUID string " + textContents);
    }
  }

}
