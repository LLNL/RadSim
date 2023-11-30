/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.ClassUtilities;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomBuilder;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Internal
final class AttributeHandlers<Type>
{
  static final class AttrPair
  {
    final Reader.Attribute annotation;
    final String name;
    final Method method;
    ClassUtilities.ValueOf vo;

    private AttrPair(Reader.Attribute attr, Method method)
    {
      this.annotation = attr;
      this.name = AttributeHandlers.getAttributeName(attr, method);
      this.method = method;
      Class<?> param = method.getParameterTypes()[0];
      this.vo = ClassUtilities.newValueOf(param);
    }
  }

  ArrayList<AttrPair> entries = new ArrayList<>();

  public AttributeHandlers(Class<Type> cls)
  {
    for (Method method : cls.getDeclaredMethods())
    {
      Reader.Attribute attr = method.getAnnotation(Reader.Attribute.class);
      if (attr == null)
        continue;
      method.setAccessible(true);
      entries.add(new AttrPair(attr, method));
    }
  }

  @SuppressWarnings("unchecked")
  public static <Type> AttributeHandlers<Type> create(Class<Type> cls)
  {
    return new AttributeHandlers(cls);
  }

  public void applyAttributes(ReaderContext rc, Type obj, Attributes attributes)
          throws ReaderException
  {
    for (AttrPair entry : entries)
    {
      String value = attributes.getValue(entry.name);
      try
      {
        if (value != null)
          entry.method.invoke(obj, entry.vo.valueOf(value));
      }
      catch (IllegalAccessException | IllegalArgumentException ex)
      {
        throw new ReaderException("Error setting attribute " + entry.method, ex);
      }
      catch (InvocationTargetException ex)
      {
        rc.handleException(ex);
      }
    }
  }

  public void createSchemaType(DomBuilder type)
  {
    // Must first sort the attributes by name so they are stable in the xsd
    Collections.sort(entries, (AttrPair t, AttrPair t1) -> t.name.compareTo(t1.name));

    // Call the declare for each method
    for (AttrPair entry : this.entries)
    {
      declareAttribute(type, entry.annotation, entry.method);
    }
  }

  public static void declareAttribute(DomBuilder type, Reader.Attribute attr, Method method)
  {
    DomBuilder attrElement = type.element("xs:attribute")
            .attr("name", getAttributeName(attr, method))
            .attr("type", getAttributeType(attr, method));
    if (attr.required())
      attrElement.attr("use", "required");
  }

  public static String getAttributeName(Reader.Attribute attr, Method method)
  {
    String attribName = attr.name();
    if (attribName.equals(attr.NULL))
    {
      String methodName = method.getName();
      if (!methodName.startsWith("set"))
        throw new RuntimeException("Method " + method.toString() + " does not start with set");
      if (methodName.length() == 3)
        throw new RuntimeException("Method " + method.toString() + " does not have a field");
      char q = methodName.charAt(3);
      attribName = Character.toLowerCase(q) + methodName.substring(4);
    }
    return attribName;
  }

  protected static String getAttributeType(Reader.Attribute attr, Method method)
  {
    String attrType = "xs:string";
    Class<?> parameterType = method.getParameterTypes()[0];
    if (parameterType == Double.TYPE)
      attrType = "xs:double";
    if (parameterType == Integer.TYPE)
      attrType = "xs:int";
    return attrType;
  }

}
