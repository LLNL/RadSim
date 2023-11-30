/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.io;

import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import java.util.LinkedList;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Internal
//@ReaderInfo(DataContentFactoryImpl.Reader.class)
class DataContentFactoryImpl extends DataContentFactory
{
  LinkedList<ContentHandler> handlers = new LinkedList<>();

  DataContentFactoryImpl()
  {
  }

  void addContentHandler(ContentHandler handler)
  {
    handlers.add(handler);
  }

  private ContentHandler findHandler(String uri, Class<?> cls)
  {
    String className = cls.getName();
    for (ContentHandler handler : handlers)
    {
      if (handler.uri.equals(uri) && handler.typeName.equals(className))
        return handler;
    }
    return null;
  }

  @Override
  public <T> DataFileReader<T> getDataFileReader(String uri, Class<T> cls)
  {
    try
    {
      ContentHandler handler = findHandler(uri, cls);
      if (handler == null)
        return null;
      String name = handler.dataStreamReader;
      if (name == null)
        return null;
      Class<?> readerClass = Class.forName(name);
      return (DataFileReader<T>) readerClass.newInstance();
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex)
    {
      return null;
    }
  }

  @Override
  public <T> DataStreamReader<T> getDataStreamReader(String uri, Class<T> cls)
  {
    try
    {
      ContentHandler handler = findHandler(uri, cls);
      if (handler == null)
        return null;
      String name = handler.dataStreamReader;
      if (name == null)
        return null;
      Class<?> readerClass = Class.forName(name);
      return (DataStreamReader<T>) readerClass.newInstance();
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex)
    {
      return null;
    }
  }

  @Override
  public <T> DataFileWriter<T> getDataFileWriter(String uri, Class<T> cls)
  {
    try
    {
      ContentHandler handler = findHandler(uri, cls);
      if (handler == null)
        return null;
      String name = handler.dataStreamWriter;
      if (name == null)
        return null;
      Class<?> readerClass = Class.forName(name);
      return (DataFileWriter<T>) readerClass.newInstance();
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex)
    {
      return null;
    }
  }

  @Override
  public <T> DataStreamWriter<T> getDataStreamWriter(String uri, Class<T> cls)
  {
    try
    {
      ContentHandler handler = findHandler(uri, cls);
      if (handler == null)
        return null;
      String name = handler.dataStreamWriter;
      if (name == null)
        return null;
      Class<?> readerClass = Class.forName(name);
      return (DataStreamWriter<T>) readerClass.newInstance();
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex)
    {
      return null;
    }
  }

  @Reader.Declaration(pkg = UtilityPackage.class, name = "DataContentFactoryHandler",
          order = Reader.Order.OPTIONS, autoAttributes = true,
          cls=ContentHandler.class, options=Reader.Option.AUTO_ATTRIBUTES)
  public static class ContentHandlerReader extends ObjectReader<ContentHandler>
  {
    @Override
    public ContentHandler start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      return new ContentHandler();
    }

    @Override
    public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
    {
      ReaderBuilder<ContentHandler> builder = newBuilder();
      builder.element("dataFileReader").callString(ContentHandler::setDataFileReader);
      builder.element("dataFileWriter").callString(ContentHandler::setDataFileWriter);
      builder.element("dataStreamReader").callString(ContentHandler::setDataStreamReader);
      builder.element("dataStreamWriter").callString(ContentHandler::setDataStreamWriter);
      return builder.getHandlers();
    }

  }

  @ReaderInfo(ContentHandlerReader.class)
  public static class ContentHandler
  {
    private String typeName;
    private String extension;
    private String uri;
    private String dataFileReader;
    private String dataFileWriter;
    private String dataStreamReader;
    private String dataStreamWriter;
    private String dataContentReader;
    private String dataContentWriter;
   
    /**
     * @param typeName the typeName to set
     */
    @Reader.Attribute(required = true)
    public void setTypeName(String typeName)
    {
      this.typeName = typeName;
    }

    /**
     * @param extension the extension to set
     */
    @Reader.Attribute
    public void setExtension(String extension)
    {
      this.extension = extension;
    }

    /**
     * @param uri the uri to set
     */
    @Reader.Attribute(required = true)
    public void setUri(String uri)
    {
      this.uri = uri;
    }

    /**
     * @param dataFileReader the dataFileReader to set
     */
    @Reader.Element(name = "dataFileReader")
    public void setDataFileReader(String dataFileReader)
    {
      this.dataFileReader = dataFileReader;
    }

    /**
     * @param dataFileWriter the dataFileWriter to set
     */
    @Reader.Element(name = "dataFileWriter")
    public void setDataFileWriter(String dataFileWriter)
    {
      this.dataFileWriter = dataFileWriter;
    }

    /**
     * @param dataStreamReader the dataStreamReader to set
     */
    @Reader.Element(name = "dataStreamReader")
    public void setDataStreamReader(String dataStreamReader)
    {
      this.dataStreamReader = dataStreamReader;
    }

    /**
     * @param dataStreamWriter the dataStreamWriter to set
     */
    @Reader.Element(name = "dataStreamWriter")
    public void setDataStreamWriter(String dataStreamWriter)
    {
      this.dataStreamWriter = dataStreamWriter;
    }

    /**
     * @param dataContentReader the dataContentReader to set
     */
    public void setDataContentReader(String dataContentReader)
    {
      this.dataContentReader = dataContentReader;
    }

    /**
     * @param dataContentWriter the dataContentWriter to set
     */
    public void setDataContentWriter(String dataContentWriter)
    {
      this.dataContentWriter = dataContentWriter;
    }
  }

//</editor-fold>
}

/*
<dataContentFactory>
  <handler type="gov.llnl.rtk.data.Bins" ns="urn:sandia:gadras" >
    <dataFileReader name="gov.llnl.rtk.gadras.data.BinGadrasInputFile"/>
    <dataFileWriter name="gov.llnl.rtk.gadras.data.BinGadrasOutputFile"/>
  </handler>

</dataContentFactory>
 */
