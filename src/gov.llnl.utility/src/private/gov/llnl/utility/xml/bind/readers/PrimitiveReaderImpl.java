/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind.readers;

import gov.llnl.utility.ClassUtilities;
import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import java.lang.annotation.Annotation;

/**
 *
 * @author nelson85
 * @param <Obj>
 */
@Reader.Declaration(pkg = UtilityPackage.class, name = Reader.Declaration.NULL,
        contents = Reader.Contents.TEXT)
public class PrimitiveReaderImpl<Obj> extends ContentsReader<Obj>
{
  final ClassUtilities.Primitive primitive;

  public PrimitiveReaderImpl(ClassUtilities.Primitive prim)
  {
    this.primitive = prim;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Obj contents(ReaderContext context, String textContents) throws ReaderException
  {
    try
    {
      return (Obj) primitive.valueOf(textContents);
    }
    catch (NumberFormatException ex)
    {
      throw new ReaderException(ex);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public Class<Obj> getObjectClass()
  {
    return primitive.getBoxedType();
  }
  
  @Override
  public Reader.TextContents getTextContents()
  {
    Reader.TextContents tc = 
               this.getClass().getDeclaredAnnotation(Reader.TextContents.class);

    if (tc!=null)
      return tc;
    
    return new Reader.TextContents()
    {
      @Override
      public String base()
      {
        return "xs:"+ primitive.getPrimitiveType().getName();
      }

      @Override
      public Class<? extends Annotation> annotationType()
      {
         return Reader.TextContents.class;
      }
    };
  }

}
