/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Utilities to deal with URI. Native methods don't deal well with the jar
 * schema. This we need extensions to make use of files in jars.
 */
public class URIUtilities
{
  /**
   * Constructs a new URI by parsing the given string and then resolving it
   * against this URI. This is a replacement for
   * {@link URI#resolve(java.lang.String) URI.resolve()} that properly handles
   * jar schema URI.
   *
   * @param uri
   * @param object
   * @return a URI for the requested object.
   */
  static public URI resolve(URI uri, String object)
  {
    // special handling because jar uri is opaque
    // See discusion at http://stackoverflow.com/questions/13046150/java-net-uri-relativize-doesnt-work-with-jar-uris
    if ("jar".equals(uri.getScheme()))
    {
      try
      {
        String schemePart = uri.getRawSchemeSpecificPart();
        URI uri2 = new URI(schemePart);
        URI uri3 = uri2.resolve(object);
        return new URI("jar", uri3.toString(), null);
      }
      catch (URISyntaxException ex)
      {
        throw new RuntimeException(ex);
      }
    }
    return uri.resolve(object);
  }

  /**
   * Extracts the file component of a URI. This method is aware of jar schema
   * URI.
   *
   * @param uri
   * @return a string with the file component.
   */
  static public String getFileName(URI uri)
  {
    try
    {
      if ("jar".equals(uri.getScheme()))
      {
        URI uri2 = new URI(uri.getSchemeSpecificPart());
        return Paths.get(uri2).getFileName().toString();
      }
      return Paths.get(uri).getFileName().toString();
    }
    catch (URISyntaxException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Verifies that an object pointed to by a URI is available to open. Opens a
   * stream to a URI and verifies it can be read. May be costly on some types of
   * resources.
   *
   * @param uri is the URI to verify.
   * @return false is the object cannot be opened or the URI does not map to a
   * URL.
   */
  public static boolean exists(URI uri)
  {
    try
    {
      URL url = uri.toURL();
      // PENDING do we need to handle caching here?
      try (InputStream is = url.openStream())
      {
        return is != null;
      }
      catch (IOException ex)
      {
        return false;
      }
    }
    catch (MalformedURLException ex)
    {
      return false;
    }
  }
}
