/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomBuilder;

/**
 * Helper to create schema definition from an element handler.
 *
 * @author nelson85
 */
class ElementHandlerMapBuildSchema
{

  /**
   * Create schema for elements that are in the top level of the group.
   *
   * @throws ReaderException
   */
  static void process(SchemaBuilder builder, DomBuilder root, Reader.ElementHandler handler) throws ReaderException
  {
    if (handler == null)
      throw new RuntimeException("null");
    Reader.ElementHandler current = handler;
    ElementGroup top = current.getParentGroup();
    while (top.getParent() != null)
    {
      top = top.getParent();
    }
    DomBuilder dom = top.createSchemaGroup(root);
    while (current != null)
    {
      // Deal with subgroups
      if (current.getParentGroup() != top)
      {
        current = process2(builder, dom, current);
      }
      else
      {
        current.createSchemaElement(builder, dom);
        current = current.getNextHandler();
      }
    }
  }

  /**
   * Insert the schema definitions for elements that are members of a subgroup.
   *
   * @param domParent
   * @throws ReaderException
   */
  static private Reader.ElementHandler process2(SchemaBuilder builder, DomBuilder domParent, Reader.ElementHandler current) throws ReaderException
  {
    ElementGroup group = current.getParentGroup();
    DomBuilder dom = group.createSchemaGroup(domParent);
    while (current != null)
    {
      if (current.getParentGroup() != group)
        break;
      current.createSchemaElement(builder, dom);
      current = current.getNextHandler();
    }
    return current;
  }

}
