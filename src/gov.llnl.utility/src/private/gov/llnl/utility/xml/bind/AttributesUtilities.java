/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.xml.DomBuilder;
import gov.llnl.utility.xml.bind.Reader.AnyAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import gov.llnl.utility.xml.bind.Reader.Attribute;
import gov.llnl.utility.xml.bind.Reader.ProcessContents;

/**
 * Utilities to create attributes in the schema.
 *
 * @author nelson85
 */
class AttributesUtilities
{
  static public void createSchemaType(DomBuilder type, Attribute[] attributes, AnyAttribute anyattribute)
  {
    if (attributes != null)
    {

      List<Attribute> attributesList = new ArrayList<>(Arrays.asList(attributes));
      // Must first sort the attributes by name so they are stable in the xsd
      Collections.sort(attributesList,
              (Reader.Attribute t, Reader.Attribute t1)
              -> t.name().compareTo(t1.name()));

      // Call the declare for each method
      for (Reader.Attribute entry : attributesList)
      {
        declareAttribute(type, entry);
      }
    }
    if (anyattribute != null)
    {
      declareAnyAttribute(type, anyattribute);
    }
  }

  public static void declareAttribute(DomBuilder type, Reader.Attribute ad)
  {
    if (Attribute.NULL.equals(ad.name()))
      throw new RuntimeException("unname attribute");

    DomBuilder attrElement = type.element("xs:attribute")
            .attr("name", ad.name())
            .attr("type", getAttributeType(ad.type()));
    if (ad.required())
      attrElement.attr("use", "required");
  }

  protected static String getAttributeType(Class<?> parameterType)
  {
    if (parameterType == String.class)
      return "xs:string";
    if (parameterType == Double.TYPE || parameterType == Double.class)
      return "xs:double";
    if (parameterType == Integer.TYPE || parameterType == Integer.class)
      return "xs:int";
    if (parameterType == Boolean.TYPE || parameterType == Boolean.class)
      return "xs:boolean";

    if (parameterType.isEnum())
      return "xs:string";

    throw new RuntimeException("Unknown attribute type " + parameterType.getName());
  }

  private static void declareAnyAttribute(DomBuilder type, AnyAttribute anyattribute)
  {
    DomBuilder attrElement = type.element("xs:anyAttribute");
    if (!anyattribute.id().equals(AnyAttribute.NULL))
    {
      attrElement.attr("id", anyattribute.id());
    }
    if (!anyattribute.namespace().equals(AnyAttribute.NULL))
    {
      attrElement.attr("namespace", anyattribute.namespace());
    }
    if (anyattribute.processContents() != ProcessContents.Strict)
    {
      attrElement.attr("processContents", anyattribute.processContents().toString().toLowerCase());
    }
  }
}
