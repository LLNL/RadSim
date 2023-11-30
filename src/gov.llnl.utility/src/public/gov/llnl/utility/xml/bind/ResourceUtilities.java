/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Utility classes for dealing with resources.
 *
 * These utilities were used for dealing with dealing with resources on other
 * platforms.  Used only in Android ports.
 *
 */
public class ResourceUtilities
{
  /**
   * Open a resource with optional cache.
   *
   * @param cls is a class in the jar holding the resource.
   * @param resource is the name of the resource to search
   * @param cached true allows the connect to use caching.
   * @return a new input stream.
   * @throws FileNotFoundException
   * @throws IOException
   */
  static public InputStream openResourceStream(Class cls, String resource, boolean cached)
          throws FileNotFoundException, IOException
  {
    return openResourceStream(cls.getClassLoader(), resource, cached);
  }

  /**
   * Open a resource with optional cache.
   *
   * @param classLoader is the class loader used to fetch the resource.
   * @param resource is the name of the resource to search
   * @param cached true allows the connect to use caching.
   * @return a new input stream.
   * @throws FileNotFoundException
   * @throws IOException
   */
  static public InputStream openResourceStream(ClassLoader classLoader, String resource, boolean cached)
          throws FileNotFoundException, IOException
  {
    URL url = classLoader.getResource(resource);
    if (url == null)
      throw new FileNotFoundException("Unable to locate resource " + resource);
    URLConnection connection = url.openConnection();
    connection.setUseCaches(cached);
    return connection.getInputStream();
  }

  /**
   * Not currently used.
   */
  static public void setDefaultUseCache(boolean use)
  {
    try
    {
      Class klass = ResourceUtilities.class;
      ClassLoader cl = klass.getClassLoader();
      String name = klass.getName();
      name = name.replace('.', '/') + ".class";
      URL url = cl.getResource(name);
      URLConnection connection = url.openConnection();
      connection.setDefaultUseCaches(use);
    }
    catch (IOException ex)
    {
      throw new RuntimeException("Unable to access class");
    }
  }

  /**
   * Not currently used.
   */
  static public boolean getDefaultUseCache()
  {
    try
    {
      Class klass = ResourceUtilities.class;
      ClassLoader cl = klass.getClassLoader();
      String name = klass.getName();
      name = name.replace('.', '/') + ".class";
      URL url = cl.getResource(name);
      URLConnection connection = url.openConnection();
      return connection.getDefaultUseCaches();
    }
    catch (IOException ex)
    {
      throw new RuntimeException("Unable to access class");
    }
  }
}
