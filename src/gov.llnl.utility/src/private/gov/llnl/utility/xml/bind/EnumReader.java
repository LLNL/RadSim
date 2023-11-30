/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.io.ReaderException;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(name = "enum", 
        pkg = UtilityPackage.class, 
        contents = ObjectReader.Contents.TEXT)
public class EnumReader<T extends Enum> extends ObjectReader<T>
{
  Class<T> cls;

  public EnumReader()
  {
  }

  public EnumReader(Class<T> cls)
  {
    this.cls = cls;
  }

  @Override
  public T contents(ReaderContext context, String textContents) throws ReaderException
  {
    return (T) Enum.valueOf(cls, textContents);
  }

  @Override
  public Class getObjectClass()
  {
    return cls;
  }
}
