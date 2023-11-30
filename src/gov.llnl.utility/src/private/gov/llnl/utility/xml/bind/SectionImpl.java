/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.PackageResource;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.ReaderException;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 * @param <Component>
 */
@Internal
public abstract class SectionImpl<Component> implements Reader.SectionInterface<Component>
{
  @Override
  public Component start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    return null;
  }

  @Override
  public Component contents(ReaderContext context, String textContents) throws ReaderException
  {
    return null;
  }

  @Override
  public Component end(ReaderContext context) throws ReaderException
  {
    return null;
  }

  @Override
  final public String getHandlerKey()
  {
    String name = getXmlName();
    // Using annotaion @Reader.Declaration on derived classes, 
    // name will be a constant expression and cannot be null.
    if (name == null)
      return null;
    // Using annotaion @Reader.Declaration on derived classes, 
    // resource will not be null. 
    PackageResource resource = this.getPackage();
    if (resource != null)
      return name + "#" + resource.getNamespaceURI();
    return name + "#";
  }

  final protected String getXmlPrefix()
  {
    // Using annotaion @Reader.Declaration on derived classes, 
    // resource will not be null. 
    PackageResource resource = this.getPackage();
    if (resource == null)
      return "";
    return resource.getSchemaPrefix() + ":";
  }

  @Override
  public String getSchemaType()
  {
    String className = this.getClass().getName();
    return className.replaceAll("[A-z]*\\.", "").replaceAll("[$]", "-") + "-type";
  }

}
