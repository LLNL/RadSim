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
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(
        pkg = UtilityPackage.class,
        name = "file",
        referenceable = true,
        contents = Reader.Contents.TEXT,
        cls = Path.class)
@Reader.TextContents(base="xs:string")
public class FileContents extends ContentsReader<Path>
{

  @Override
  public Path contents(ReaderContext context, String textContents)
  {
    return Paths.get(textContents);
  }

}
