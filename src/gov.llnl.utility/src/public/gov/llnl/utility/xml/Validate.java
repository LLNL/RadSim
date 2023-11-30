/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml;

import gov.llnl.utility.PackageResource;
import gov.llnl.utility.xml.bind.SchemaManager;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 *
 * @author nelson85
 */
public class Validate
{
  static class ValidateException extends Exception
  {
    ValidateException(String msg)
    {
      super(msg);
    }

    ValidateException(Throwable ex)
    {
      super(ex);
    }

    private ValidateException(String msg, Throwable ex)
    {
      super(msg, ex);
    }
  }

  static public void main(String[] args)
  {
    try
    {
      Validate validator = new Validate();

      if (args.length != 2)
        throw new ValidateException("Usage: Validate <Package.Class> file.xml");

      PackageResource obj = validator.getPackageResource(Class.forName(args[0]));
      boolean valid = validator.validate(obj, Paths.get(args[1]));
      System.out.println("Done.");
      if (!valid)
        System.exit(-1);
      System.exit(0);
    }
    catch (ValidateException ex)
    {
      System.out.println(ex.getMessage());
    }
    catch (Exception ex)
    {
      ex.printStackTrace(System.out);
    }
    System.exit(-1);
  }

  public PackageResource getPackageResource(Class cls) throws ValidateException
  {
    if (!PackageResource.class.isAssignableFrom(cls))
      throw new ValidateException("First argument must be a PackageResource (got " + cls + ")");

    Class<PackageResource> pr = cls;
    Method method;
    try
    {
      method = pr.getDeclaredMethod("getInstance");
    }
    catch (NoSuchMethodException | SecurityException ex)
    {
      throw new ValidateException("Failed find method getInstance() in " + cls.getName(), ex);
    }
    PackageResource obj;
    try
    {
      obj = (PackageResource) method.invoke(null);
    }
    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
    {
      throw new ValidateException("Failed execute method getInstance() in " + cls.getName(), ex);
    }
    if (obj == null)
      throw new ValidateException("Package instance is null");
    return obj;
  }

  public boolean validate(PackageResource obj, Path file) throws IOException
  {
    boolean valid = true;
    try (InputStream is = Files.newInputStream(file))
    {
      InputStream is2 = is;
      if (file.getFileName().toString().endsWith(".gz"))
      {
        System.out.println("Handle compressed");
        is2 = new GZIPInputStream(is);
      }
      String src = file.normalize().toString();
      valid = validate(obj, is2, src);
    }
    return valid;
  }

//<editor-fold desc="validate" defaultstate="collapsed">
  /**
   * Validates an input stream against the schema.
   *
   * @param resource is the package we are validating against.
   * @param inputStream holds the document to validate.
   * @return true if the document is valid.
   * @throws RuntimeException if there is no schema defined.
   */
  public boolean validate(PackageResource resource, InputStream inputStream, String systemId)
  {
    ValidationHandler vh = new ValidationHandler();
    try
    {
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setNamespaceAware(true);
      spf.setValidating(true);
      SAXParser saxParser = spf.newSAXParser();
      saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
              "http://www.w3.org/2001/XMLSchema");
      saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource",
              resource.getSchemaURI());
      XMLReader reader = saxParser.getXMLReader();
      reader.setEntityResolver(SchemaManager.getInstance().getEntityResolver());
      reader.setErrorHandler(vh);
      InputSource inputSource = new InputSource(inputStream);
      inputSource.setSystemId(systemId);
      reader.parse(inputSource);
      return vh.good;
    }
    catch (ParserConfigurationException ex)
    {
      throw new RuntimeException(ex);
    }
    catch (SAXException ex)
    {
      throw new RuntimeException(ex);
    }
    catch (IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  static public class ValidationHandler implements ErrorHandler
  {
    boolean good = true;

    private String getSystemId(SAXParseException ex)
    {
      String src = ex.getSystemId();
      if (src.startsWith("file:"))
      {
        Path p = Paths.get(URI.create(src));
        p = Paths.get(".").normalize().toAbsolutePath().relativize(p);
        src = p.toString();
      }
      return src;
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException
    {
      System.out.println("Warning: " + getSystemId(exception) + ":" + exception.getLineNumber() + ": " + exception.getMessage());
    }

    @Override
    public void error(SAXParseException exception) throws SAXException
    {
      good = false;
      System.out.println("Error: " + getSystemId(exception) + ":" + exception.getLineNumber() + ": " + exception.getMessage());

    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException
    {
      good = false;
      System.out.println("Fatal: " + getSystemId(exception) + ":" + exception.getLineNumber() + ": " + exception.getMessage());
    }
  }

//</editor-fold>
}
