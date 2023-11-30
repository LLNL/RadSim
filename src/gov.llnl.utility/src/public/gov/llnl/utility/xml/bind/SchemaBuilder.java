/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.ClassUtilities;
import gov.llnl.utility.PackageResource;
import gov.llnl.utility.Singletons;
import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomBuilder;
import gov.llnl.utility.xml.DomUtilities;
import gov.llnl.utility.xml.bind.Reader.ElementHandlerMap;
import gov.llnl.utility.xml.bind.Reader.Option;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * SchemaBuilder write a schema for ObjectReader. Multiple elements can be
 * created in the same schema. SchemaBuilder automatically tracks what items are
 * already definedType, so it is not an issue if a Reader is added more than
 * once.
 */
public class SchemaBuilder
{
  private Document document;
  private final TreeMap<String, String> namespaces = new TreeMap<>();
  // static final String XSD_URI = XMLConstants.W3C_XML_SCHEMA_NS_URI;
  private PackageResource defaultSchema;
  private final TreeSet<String> definedType = new TreeSet<>();
  private final TreeMap<String, Reader> definedElementKey = new TreeMap<>();
  int error = 0;

  @SuppressWarnings("unchecked")
  static public void main(String[] args) throws IOException, ClassNotFoundException
  {
    Class<PackageResource> schema = (Class<PackageResource>) Class.forName(args[0]);
    PackageResource instance = Singletons.getSingleton(schema);

    SchemaBuilder sb = new SchemaBuilder();
    sb.setTargetNamespace(instance);
    sb.scanForReaders(Paths.get(args[1]), ".class");
    Path file = Paths.get(args[2], args[3]);
    Files.createDirectories(file.getParent());
    try ( OutputStream os = new BufferedOutputStream(Files.newOutputStream(file));  ByteArrayOutputStream baos = new ByteArrayOutputStream())
    {
      DomUtilities.printXml(baos, sb.getDocument());
      String str = baos.toString("UTF-8").replaceAll("\r\n", "\n");
      os.write(str.getBytes("UTF-8"));
    }
    UtilityPackage.LOGGER.log(Level.FINE, "Wrote schema for {0}", args[0]);
    System.exit(sb.getError());
  }

  /**
   * Create a new SchemaBuilder.
   */
  public SchemaBuilder()
  {
    createDocument();
  }

  /**
   * (Internal) Creates a document.
   */
  private void createDocument()
  {
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      DOMImplementation dom = db.getDOMImplementation();
      document = dom.createDocument(XMLConstants.W3C_XML_SCHEMA_NS_URI, "xs:schema", null);
      Element element = document.getDocumentElement();
      element.setAttribute("version", "1.0");
      element.setAttribute("elementFormDefault", "qualified");
    }
    catch (ParserConfigurationException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Get a DomBuilder for the root element.
   *
   * @return a dom builder for the root element of the document.
   */
  public DomBuilder getRoot()
  {
    return new DomBuilder(document.getDocumentElement());
  }

  /**
   * Define a schema for an object reader.
   *
   * @param reader is the reader to add a schema for.
   * @throws ReaderException
   */
  public void addObjectReader(Reader reader)
          throws ReaderException
  {
    // Skip readers marked for no schema.
    if (Option.check(reader.getDeclaration().options(), Option.NO_SCHEMA))
      return;

    if (defaultSchema != reader.getPackage())
    {
      throw new ReaderException("Incorrect package for " + reader.getClass());
    }

    Class objectClass = reader.getObjectClass();
    if (objectClass != null)
    {
      UtilityPackage.LOGGER.log(Level.FINE, "Creating reader schema for {0}", objectClass);
      if (ClassUtilities.isInnerClass(objectClass))
        UtilityPackage.LOGGER.log(Level.SEVERE, "Inner classes may not be type base {0}", objectClass);
    }
    createReaderSchemaType(reader, true);

    // Must be a document or referenceable to be included as a top level element.
    Reader.Declaration decl = reader.getDeclaration();
    if (decl.document() || decl.referenceable())
      createReaderSchemaElement(reader, reader.getXmlName());
  }

  public <T> void alias(Class<T> cls, String name) throws ReaderException
  {
    UtilityPackage.LOGGER.log(Level.FINE, "Alias {0}", name);
    ObjectReader<T> reader = ObjectReader.create(cls);
    createReaderSchemaElement(reader, name);
  }

  /**
   * (internal)
   *
   * @param reader
   * @param name
   * @throws ReaderException
   */
  public void createReaderSchemaElement(Reader reader, String name)
          throws ReaderException
  {

    // Check for conflicts between names
    if (definedElementKey.containsKey(name))
    {
      if (definedElementKey.get(name).getClass() != reader.getClass())
      {
        throw new ReaderException("Schema conflict on element " + name
                + " between " + reader.getClass() + " and "
                + definedElementKey.get(name).getClass());
      }
      return;
    }
    definedElementKey.put(name, reader);

    // Watch for unnamed elements
    if (name.isEmpty())
      throw new ReaderException("Unnamed element " + reader.getClass().getName());

    // Create the schema element so that it can be used for definitions and documents.
    UtilityPackage.LOGGER.log(Level.FINE, "Create schema element {0}", name);
    reader.createSchemaElement(this, name, this.getRoot(), true);
  }

  /**
   * (internal)
   *
   * @param reader
   * @param recursive
   * @throws ReaderException
   */
  public void createReaderSchemaType(Reader reader, boolean recursive)
          throws ReaderException
  {
    // Prevent the object reader form being definedType twice
    if (definedType.contains(getReaderKey(reader)))
      return;
    definedType.add(getReaderKey(reader));

    // Skip packages that are not in this schema.
    if (defaultSchema != reader.getPackage())
    {
      UtilityPackage.LOGGER.log(Level.SEVERE, "Skip {0}", reader.getClass().getName());
      return;
    }

    UtilityPackage.LOGGER.log(Level.FINE, "Create schema type {0}", reader.getXmlName());
    reader.createSchemaType(this);

    if (recursive)
    {
      ElementHandlerMap handlers = reader.getHandlers(new SchemaReaderContext(reader));
      if (handlers == null)
        return;

      for (Reader.ElementHandler handler : handlers.toList())
      {
        // Check if the child needs a schema included.
        Reader childReader = handler.getReader();
        if (childReader == null)
          continue;

        UtilityPackage.LOGGER.log(Level.FINE, "Create schema type {0}", childReader.getXmlName());
        createReaderSchemaType(childReader, recursive);
      }
    }
  }

  /**
   * Get the DOM hold the schema for all readers stored.
   *
   * @return the document.
   */
  public Document getDocument()
  {
    // Horrible hack to sort the nodes after we have completed building the dom
    Element element = document.getDocumentElement();

    // Remove all the existing elements
    List<Element> contents = removeAllChildren(element);

    split(contents, e -> !e.hasAttribute("name"), element::appendChild);
    List<Node> types = new LinkedList();
    split(contents, e -> !e.getTagName().equals("xs:element"), types::add);

    // Sort all of them by name
    Collections.sort(types, (Node x, Node y)
            -> ((Element) x).getAttribute("name").compareTo(((Element) y).getAttribute("name")));
    Collections.sort(contents, (Node x, Node y)
            -> ((Element) x).getAttribute("name").compareTo(((Element) y).getAttribute("name")));

    // Insert back in the document
    types.stream().forEach(element::appendChild);
    contents.stream().forEach(element::appendChild);

    return document;
  }

  /**
   * Add an include statement to the schema.
   *
   * @param file is the name of the file to include in the schema.
   */
  public void include(String file)
  {
    getRoot().element("xs:include", true)
            .attr("schemaLocation", file);
  }

  /**
   * Add an import statement to the schema.
   *
   * @param resource defines the file and the prefix to use.
   */
  public void imports(PackageResource resource)
  {
    if (hasNamespace(resource))
      return;
    addNamespace(resource);
    getRoot().element("xs:import", true)
            .attr("namespace", resource.getNamespaceURI())
            .attr("schemaLocation", resource.getSchemaURI());
  }

  /**
   * Test to see if a resource has been included in the schema being built.
   *
   * @param resource
   * @return
   */
  public boolean hasNamespace(PackageResource resource)
  {
    if (resource.getNamespaceURI().equals(Schema.NONE))
      return true;
    return this.namespaces.containsKey(resource.getNamespaceURI());
  }

  /**
   * Adds a namespace to be used for this schema. This is called automatically
   * by {@link #imports} and {@link #setTargetNamespace setTargetNamespace}.
   *
   * @param resource
   */
  public void addNamespace(PackageResource resource)
  {
    if (this.namespaces.containsKey(resource.getNamespaceURI()))
      return;
    namespaces.put(resource.getNamespaceURI(), resource.getSchemaPrefix());
    Element element = document.getDocumentElement();
    element.setAttributeNS(resource.getNamespaceURI(), resource.getSchemaPrefix() + ":version", "1.0");
    for (PackageResource dep : resource.getDependencies())
    {
      imports(dep);
    }

    for (PackageResource dep : resource.getIncludes())
    {
      include(dep.getSchemaURI());
    }
  }

  /**
   * Define the namespace for the resulting schema.
   *
   * @param resource
   */
  public void setTargetNamespace(PackageResource resource)
  {
    this.defaultSchema = resource;
    Element element = document.getDocumentElement();
    if (resource != null)
    {
      if (resource.getSchema().namespace().equals(Schema.NONE))
        return;
      element.setAttribute("targetNamespace", resource.getNamespaceURI());
    }
    addNamespace(resource);
  }

  private String getReaderKey(Reader reader)
  {
    String key = reader.getSchemaType();
    if (key == null)
      return reader.getClass().getName();
    return key;
  }

  public void scanForReaders(Path dir) throws IOException
  {
    scanForReaders(dir, ".java");
  }

  @SuppressWarnings("unchecked")
  public void scanForReaders(Path dir, String extension) throws IOException
  {

    final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*" + extension);
    final List<Path> files = new LinkedList<>();
    Files.walkFileTree(dir, new FileVisitor<Path>()
    {
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
      {
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
      {
        if (matcher.matches(file))
          files.add(file);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
      {
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
      {
        return FileVisitResult.CONTINUE;
      }
    });
    Collections.sort(files, (Path p1, Path p2) ->
    {
      String s1 = p1.toFile().getAbsolutePath();
      String s2 = p2.toFile().getAbsolutePath();
      return s1.compareTo(s2);
    });
    for (Path file : files)
    {
      String name = "";
      try
      {
        name = dir.relativize(file).toString();
        name = name.substring(0, name.length() - extension.length());
        name = name.replaceAll(Pattern.quote(File.separator), ".");
        UtilityPackage.LOGGER.log(Level.FINEST, "Looking for class {0}", name);
        ClassLoader cl = this.getClass().getClassLoader();
        Class<?> cls = Class.forName(name, false, cl);

        // Scan for element fields
        for (Field field : cls.getDeclaredFields())
        {
          Reader.ElementDeclaration annotation = field.getAnnotation(Reader.ElementDeclaration.class);
          if (annotation == null)
            continue;

          try
          {
            createReaderSchemaElement(ObjectReader.create(annotation.type()), annotation.name());
          }
          catch (ReaderException ex)
          {
            throw new RuntimeException(ex);
          }
        }

        // Check if it has a Reader.Declaration, else skip it.
        // Skip unnamed readers as they do not produce a schema.
        // Skip inner classes for now
        Reader.Declaration rd = cls.getAnnotation(Reader.Declaration.class);
        if (rd == null
                || rd.name().equals(Reader.Declaration.NULL)
                || ClassUtilities.isInnerClass(cls))
          continue;

        if (Option.check(rd.options(), Option.NO_SCHEMA))
          continue;
        try
        {
          UtilityPackage.LOGGER.log(Level.FINE, "process {0} {1}", new Object[]
          {
            cls, ObjectReader.class.isAssignableFrom(cls)
          });

          // If this is an already an Object Reader
          if (Reader.class.isAssignableFrom(cls))
          {
            try
            {
              // Create an instance and add it.
              Reader reader = (Reader) cls.getDeclaredConstructor().newInstance();
              this.addObjectReader(reader);
            }
            catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
              UtilityPackage.LOGGER.warning("Skip no such method");
            }
          }
          else
          {
            // Attempt to create an object reader for the class.
            this.addObjectReader(ObjectReader.create(cls));
          }

        }
        catch (ReaderException ex)
        {
          ByteArrayOutputStream os = new ByteArrayOutputStream();
          ex.printStackTrace(new PrintStream(os));
          UtilityPackage.LOGGER.log(Level.WARNING, "Failure in object reader {0}\n{1}", new Object[]
          {
            cls, new String(os.toByteArray())
          });
          error = -1;
        }
      }
      catch (ClassNotFoundException ex)
      {
        UtilityPackage.LOGGER.log(Level.WARNING, "Class not found while getting {0}", name);
      }
    }
  }

  public int getError()
  {
    return error;
  }

  private List<Element> removeAllChildren(Element element)
  {
    List<Element> contents = new LinkedList<>();
    while (element.getFirstChild() != null)
    {
      Node child = element.removeChild(element.getFirstChild());
      if (child.getNodeType() == Element.ELEMENT_NODE)
        contents.add((Element) child);
    }
    return contents;
  }

  /**
   * Utility to remove all elements than have some condition and act on them.
   *
   * @param contents
   * @param condition
   * @param consumer
   */
  private void split(List<Element> contents,
          Predicate<Element> condition,
          Consumer<Element> consumer)
  {
    Iterator<Element> iter;
    for (iter = contents.iterator(); iter.hasNext();)
    {
      Element next = iter.next();
      if (!condition.equals((Element) next))
        continue;
      consumer.accept(next);
      iter.remove();
    }
  }

  public static String getXmlPrefix(Reader reader)
  {
    PackageResource resource = reader.getPackage();
    if (resource == null)
      return "";
    return resource.getSchemaPrefix() + ":";
  }

}
