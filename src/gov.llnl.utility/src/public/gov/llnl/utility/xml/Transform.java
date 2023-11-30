package gov.llnl.utility.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A command line utility for transforming an xml document through an XSLT.
 *
 * This is used to help keep configuration files up to date by allowing us to
 * rename tags.
 *
 * @author nelson85
 */
public class Transform
{

  public static void transform(Result result, InputSource datafile, Path xslt)
          throws IOException
  {
    try{
    // Parse the input into a DOM for modification
    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    documentFactory.setNamespaceAware(true);
    DocumentBuilder builder = documentFactory.newDocumentBuilder();
    Document document = builder.parse(datafile);

    // Create an XSLT transform using the xslt document
    StreamSource styleSource = new StreamSource(xslt.toFile());
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer(styleSource);

    // Apply the transform to the DOM
    DOMSource source = new DOMSource(document);
    transformer.transform(source, result);
    }
    catch (TransformerException | SAXException | ParserConfigurationException ex)
    {
      throw new IOException(ex);
    }
  }

  public static void write(OutputStream os, DOMResult result) throws IOException
  {
    try
    {
      // Keep indenting format
      DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
      // LS stands for "Load and Save"
      DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
      LSOutput output = impl.createLSOutput();
      output.setByteStream(os);
      output.setEncoding("UTF-8");

      // Write the DOM back to a document
      LSSerializer writer = impl.createLSSerializer();
      writer.setNewLine("\n");
      DOMConfiguration config = writer.getDomConfig();
      config.setParameter("format-pretty-print", true);
      writer.write(result.getNode(), output);
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException ex)
    {
      throw new IOException(ex);
    }
  }

  public static void main(String argv[])
          throws ParserConfigurationException, SAXException,
          IOException, TransformerConfigurationException, TransformerException, ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    // Parse the arguments
    Path xslt = Paths.get(argv[0]);
    Path inputFile = Paths.get(argv[1]);
    Path outputFile = Paths.get(argv[2]);

    InputSource is = new InputSource(Files.newBufferedReader(inputFile));
    is.setSystemId(inputFile.toString());
    DOMResult result = new DOMResult();
    transform(result, is, xslt);
    write(Files.newOutputStream(outputFile), result);
  }
}
