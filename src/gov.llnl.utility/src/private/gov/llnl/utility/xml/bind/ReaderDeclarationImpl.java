/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.PackageResource;
import gov.llnl.utility.xml.bind.Reader.Option;
import gov.llnl.utility.xml.bind.Reader.Order;
import java.lang.annotation.Annotation;

/**
 * Front end for creating a dynamic reader declaration.
 *
 * Note: The defaults should always match the defaults in the class definition.
 *
 * @author nelson85
 */
@SuppressWarnings("AnnotationAsSuperInterface")
public abstract class ReaderDeclarationImpl implements Reader.Declaration
{
  final Reader.Declaration base;

  public ReaderDeclarationImpl()
  {
    base = null;
  }

  public ReaderDeclarationImpl(Reader.Declaration base)
  {
    this.base = base;
  }

  public Class<? extends PackageResource> pkg()
  {
    if (base != null)
      return base.pkg();
    return null;
  }

  @Override
  public String name()
  {
    if (base != null)
      return base.name();
    return null;
  }

  @Override
  public Reader.Order order()
  {
    if (base != null)
      return base.order();
    return Order.FREE;
  }

  @Override
  public boolean referenceable()
  {
    if (base != null)
      return base.referenceable();
    return false;
  }

  @Override
  public Reader.Contents contents()
  {
    if (base != null)
      return base.contents();
    return Reader.Contents.ELEMENTS;
  }

  @Override
  public boolean copyable()
  {
    if (base != null)
      return base.copyable();
    return false;
  }

  @Override
  public String typeName()
  {
    if (base != null)
      return base.typeName();
    return null;
  }

  @Override
  public boolean document()
  {
    if (base != null)
      return base.document();
    return false;
  }

  @Override
  public boolean autoAttributes()
  {
    if (base != null)
      return base.autoAttributes();
    return false;
  }

  @Override
  public Class impl()
  {
    if (base != null)
      return base.impl();
    return null;
  }

  @Override
  public Class cls()
  {
    if (base != null)
      return base.cls();
    return null;
  }

  @Override
  public Class<? extends Annotation> annotationType()
  {
    return Reader.Declaration.class;
  }

  @Override
  public String schema()
  {
    if (base != null)
      return base.schema();
    return Reader.Declaration.NULL;
  }

  static Option[] NO_OPTIONS =
  {
  };

  @Override
  public Option[] options()
  {
    if (base != null)
      return base.options();
    return NO_OPTIONS;
  }

  @Override
  public String substitutionGroup()
  {
    if (base != null)
      return base.substitutionGroup();
    return Reader.Declaration.NULL;
  }

}
