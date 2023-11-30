/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.DocumentReader;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Schema;
import gov.llnl.utility.xml.bind.Schema.Include;
import gov.llnl.utility.xml.bind.Schema.Using;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

// TODO we have assumed that schema are stored only as jar reosurces, but we
//  should support resources stored as files or at an http address.  We need
// to generalize this functionality.
/**
 * Information stored for the package. PackageResources must be singletons. They
 * hold information about where the schema for the xml bindings as well as
 * version information.
 */
public abstract class PackageResource implements Singletons.Singleton
{

  private final String namespaceURI;
  private final URI schemaURI;
  private URL schemaURL;
  private String schemaFileName = null;
  private Logger logger;

  /**
   * Create a resource. The resource will be verified to exist. This presents a
   * boot strapping issue when creating a new package.
   *
   *
   * package.
   */
  public PackageResource()
  {
    Schema schema = getClass().getAnnotation(Schema.class);
    try
    {
      this.namespaceURI = schema.namespace();
      this.schemaURI = new URI(schema.schema());
      this.schemaURL = null;
      this.logger = Logger.getLogger(schema.prefix());
      if ("http".equals(this.schemaURI.getScheme()))
      {
        this.schemaFileName = mangleURI(this.schemaURI);
        this.schemaURL = this.getClass().getClassLoader().getResource(this.schemaFileName);
      }
      else if ("file".equals(this.schemaURI.getScheme()))
      {
        this.schemaFileName = this.schemaURI.getPath();
        this.schemaURL = this.schemaURI.toURL();
      }
      if (this.schemaURL == null)
      {
        UtilityPackage.LOGGER.log(Level.WARNING, "Unable to locate resource {0}", schemaURI);
      }
    }
    catch (URISyntaxException | MalformedURLException ex)
    {
      throw new RuntimeException("SchemaURI must be valid uri " + schema.schema());
    }
  }

  /**
   * Convenience method for loading resource to initialize a singleton.
   *
   * @param <T>
   * @param cls
   * @param resourceName
   * @return the requested resource
   * @throws RuntimeException on any error during loading. This will likely
   * cause a ExceeptionInInitializerError on a failure.
   */
  public static <T> T loadResource(Class<T> cls, String resourceName)
  {
    try
    {
      DocumentReader<T> dr = DocumentReader.create(cls);
      return dr.loadResource(resourceName);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
  public Schema getSchema()
  {
    return this.getClass().getAnnotation(Schema.class);
  }

  /**
   * Get the preferred schema prefix for this namespace.
   *
   * @return the prefix to be used for types in the schema file.
   */
  public String getSchemaPrefix()
  {
    return getSchema().prefix();
  }

  /**
   * Get the unique URI for this package. Should either be a HTTP address or a
   * URN.
   *
   * @return a unique namespace string.
   */
  public String getNamespaceURI()
  {
    return namespaceURI;
  }

  /**
   * Get the unique URI that holds the schema. Should either be a HTTP address.
   *
   * @return an http address pointing to the schema file.
   */
  public String getSchemaURI()
  {
    return schemaURI.toString();
  }

  /**
   * Get the list of packages this package depends on.
   *
   * @return a list of dependencies.
   */
  public synchronized Collection<PackageResource> getDependencies()
  {
    Collection<Using> using
            = AnnotationUtilities.getRepeatingAnnotation(this.getClass(),
                    Schema.Using.class);

    return using.stream()
            .map(p -> getResource(p.value()))
            .collect(Collectors.toList());
  }

    /**
   * Get the list of packages this package depends on.
   *
   * @return a list of dependencies.
   */
  public synchronized Collection<PackageResource> getIncludes()
  {
    Collection<Include> using
            = AnnotationUtilities.getRepeatingAnnotation(this.getClass(),
                    Schema.Include.class);

    return using.stream()
            .map(p -> getResource(p.value()))
            .collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  private PackageResource getResource(Class c)
  {
    return (PackageResource) Singletons.getSingleton(c);
  }

  public static PackageResource getClassPackage(Class<?> cls)
  {
    try
    {
      ObjectReader or = ObjectReader.create(cls);
      if (or != null)
        return or.getPackage();
      return null;
    }
    catch (ReaderException ex)
    {
      return null;
    }
  }

  public static String getClassPackageName(Class<?> cls)
  {
    PackageResource pkg = getClassPackage(cls);
    if (pkg == null || pkg.getSchema().prefix().equals(Schema.NONE))
      return "";
    else
      return pkg.getSchemaPrefix() + ":";
  }

  public final Logger getLogger()
  {
    return this.logger;
  }

  public void enableLog(Level level)
  {
    //   Logger root = getLogger();
    Logger root = Logger.getLogger("");
    logger.setLevel(level);
    for (Handler handler : root.getHandlers())
    {
      if (handler instanceof ConsoleHandler)
      {
        // java.util.logging.ConsoleHandler.level = ALL
        handler.setLevel(level);
      }
    }
  }

  public String getQualifiedName(String name)
  {
    String prefix = this.getSchemaPrefix();
    if (prefix != null)
      return prefix + ":" + name;
    return name;
  }

  private static String mangleURI(URI uri)
  {
    if (!"http".equals(uri.getScheme()))
      return null;
    StringBuilder sb = new StringBuilder();
    if (uri.getHost() != null)
      sb.append(StringUtilities.join(ListUtilities.reverse(uri.getHost().split("\\.")), "/"));
    sb.append(uri.getPath());
    return sb.toString();
  }

}
