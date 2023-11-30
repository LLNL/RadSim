/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.io.ReaderException;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = UtilityPackage.class, name = "importsAny", cls = Object.class,
        options = Reader.Option.NO_SCHEMA)
public class ImportsAny extends ObjectReader<Object>
{

  @Override
  public ObjectReader findReader(String namespaceURI, String localName, String qualifiedName, Attributes attr)
  {
    try
    {
      SchemaManager schemaMgr = SchemaManager.getInstance();
      Class<?> cls = schemaMgr.getObjectClass(namespaceURI, localName);
      if (cls == null)
        throw new RuntimeException("Unable to find class for "+namespaceURI + "#" + localName);
      
      ObjectReader reader = schemaMgr.findObjectReader(cls);
      Reader.Declaration decl = reader.getDeclaration();
      if (!decl.document())
        throw new RuntimeException(namespaceURI + "#" + localName + " is not a document type.");
      return new ImportsReader(reader);
    }
    catch (ClassNotFoundException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public Object start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    return null;
  }

}
//</editor-fold>
