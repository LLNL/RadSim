/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomBuilder;
import gov.llnl.utility.xml.bind.Reader.ElementHandler;
import gov.llnl.utility.xml.bind.Reader.Option;
import java.util.EnumSet;
import org.xml.sax.Attributes;

/**
 * Proxy to an existing reader.  
 * 
 * This use used when we need to override a method for a handler created
 * else where.
 * 
 * @author nelson85
 */
public class ProxyHandler implements ElementHandler
{
  ElementHandler base;

  ProxyHandler(ElementHandler base)
  {
    this.base = base;
  }

  @Override
  public Reader getReader()
  {
    return base.getReader();
  }

  @Override
  public String getName()
  {
    return base.getName();
  }

  @Override
  public ElementGroup getParentGroup()
  {
    return base.getParentGroup();
  }

  @Override
  public String getKey()
  {
    return base.getKey();
  }

  @Override
  public EnumSet<Reader.Option> getOptions()
  {
    return base.getOptions();
  }

  @Override
  public Class getObjectClass()
  {
    return base.getObjectClass();
  }

  @Override
  public ElementHandler getNextHandler()
  {
    return base.getNextHandler();
  }

  @Override
  public void addOption(Option option)
  {
    this.base.addOption(option);
  }

  @Override
  public void setParentGroup(ElementGroup parentGroup)
  {
    this.base.setParentGroup(parentGroup);
  }

  @Override
  public void setNextHandler(ElementHandler handler)
  {
    this.base.setNextHandler(handler);
  }

  @Override
  public Object onStart(ReaderContext context, Attributes attr) throws ReaderException
  {
    return base.onStart(context, attr);
  }

  @Override
  public Object onEnd(ReaderContext context) throws ReaderException
  {
    return base.onEnd(context);
  }

  @Override
  public void onCall(ReaderContext context, Object parent, Object child) throws ReaderException
  {
    base.onCall(context, parent, child);
  }

  @Override
  public Object onTextContent(ReaderContext context, String textContent) throws ReaderException
  {
    return base.onTextContent(context, textContent);
  }

  @Override
  public void createSchemaElement(SchemaBuilder builder, DomBuilder type) throws ReaderException
  {
    base.createSchemaElement(builder, type);
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    return base.getHandlers(context);
  }
  
  @Override
  public String toString()
  {return String.format("Proxy(%s)", this.base.toString());
  
  }

  @Override
  public boolean hasOption(Option option)
  {
    return base.hasOption(option);
  }

}
