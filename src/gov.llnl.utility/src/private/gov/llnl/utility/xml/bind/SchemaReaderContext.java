/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.io.PathLocation;
import gov.llnl.utility.io.ReaderException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.function.BiConsumer;

class SchemaReaderContext implements ReaderContext
{
  Reader reader;
  Object object;

  static Object notUsed()
  {
    throw new UnsupportedOperationException("Not used.");
  }

  public SchemaReaderContext(Reader reader)
  {
    this.reader = reader;
  }

  @Override
  @SuppressWarnings(value = "unchecked")
  public <T> T get(String name, Class<T> cls)
  {
    return (T) notUsed();
  }

  @Override
  @SuppressWarnings(value = "unchecked")
  public <Obj> Obj put(String name, Obj object) throws ReaderException
  {
    return (Obj) notUsed();
  }

  @Override
  public String getElementPath()
  {
    return (String) notUsed();
  }

  @Override
  public URL getExternal(String extern, boolean prioritizeSearchPaths) throws ReaderException
  {
    return (URL) notUsed();
  }

  @Override
  public URI getFile()
  {
    return (URI) notUsed();
  }

  @Override
  public PathLocation getLocation()
  {
    return (PathLocation) notUsed();
  }

  @Override
  public ElementContext getParentContext()
  {
    return (ElementContext) notUsed();
  }

  @Override
  public ElementContext getCurrentContext()
  {
    return new SchemaHandlerContext();
  }

  @Override
  public ElementContext getChildContext()
  {
    return (ElementContext) notUsed();
  }

  @Override
  public void setErrorHandler(ReaderContext.ExceptionHandler handler)
  {
    notUsed();
  }

  @Override
  public void handleException(Throwable ex) throws ReaderException
  {
    notUsed();
  }

  @Override
  public void setPropertyHandler(PropertyMap handler)
  {
    notUsed();
  }

  @Override
  public void addDeferred(Object target, BiConsumer methodName, String refId, Class cls) throws ReaderException
  {
    notUsed();
  }

  @Override
  @SuppressWarnings(value = "unchecked")
  public Iterable<Map.Entry<String, Object>> getReferences()
  {
    return (Iterable<Map.Entry<String, Object>>) notUsed();
  }

  @Override
  public DocumentReader getDocumentReader()
  {
    notUsed();
    return null;
  }

  @Override
  public void pushTemporaryContext(Object object, Object previous)
  {
  }

  @Override
  public void popTemporaryContext()
  {
  }

  @Override
  public ElementContext getContext(Class cls)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  private class SchemaHandlerContext implements ReaderContext.ElementContext
  {
    private SchemaHandlerContext()
    {
    }

//    @Override
//    public Object getReference(String name) throws ReaderException
//    {
//      return notUsed();
//    }
//
//    @Override
//    @SuppressWarnings(value = "unchecked")
//    public <T> T putReference(String name, T obj)
//    {
//      return (T) notUsed();
//    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Object getObject()
    {
      // Some readers call getObject() in setting up the handler list.
      // Though this is bad practice and thus requires an object which
      // we are just building a schema for, we need to support it until
      // we can resolve all of these troublesome cases.
      Reader.Declaration decl = reader.getDeclaration();
      Class cls = reader.getObjectClass();
      // This is mostly legacy.  We had cases in which we were building
      // readers for abstract classes and thus the getObjectClass which
      // is abstract does not match the implement class.  However, unless
      // getObject() was called it really does not matter.  This portion
      // needs to be deprecated and removed.
      Class dcls = decl.impl();
      if (dcls != null && dcls != void.class)
        cls = dcls;
      // Try to create an instance of an object for the getHandlers method
      // this code is unsafe and should be removed for new Java versions.
      try
      {
        if (object == null)
        {
          Constructor ctor = cls.getDeclaredConstructor((Class[]) null);
          ctor.setAccessible(true);
          object = ctor.newInstance((Object[]) null);
        }
        return object;
      }
      catch (IllegalAccessException | InstantiationException | NoSuchMethodException ex)
      {
        throw new RuntimeException("Unable to create object for schema with type " + cls, ex);
      }
      catch (SecurityException | IllegalArgumentException | InvocationTargetException ex)
      {
        throw new RuntimeException(ex);
      }
    }

    @Override
    public Object getParentObject()
    {
      return notUsed();
    }

    @Override
    public String getNamespaceURI()
    {
      return (String) notUsed();
    }

    @Override
    public String getLocalName()
    {
      return (String) notUsed();
    }

    @Override
    public Object getState()
    {
      return null;
    }
  }

}
