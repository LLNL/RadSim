/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nelson85
 */
public class Proto3SchemaBuilder
{
  List<String> includes = new ArrayList<>();
  HashMap<String, String> options = new HashMap<>();
  HashSet<MessageEncoding> encodings = new HashSet<>();

  public Proto3SchemaBuilder()
  {
  }

  public void option(String key, String value)
  {
    options.put(key, value);
  }

  /**
   * All objects added should have the same package.
   *
   * @param m
   */
  public void add(MessageEncoding m)
  {
    // Skip peudomessages
    if (m.getFields() == null)
      return;

    Object pkg = m.getPackage();
    encodings.add(m);

    for (ProtoField field : m.getFields())
    {
      // Skip the header
      if (field.id == -1)
        continue;

      if (!(field.encoding instanceof MessageEncoding))
        continue;

      MessageEncoding me = (MessageEncoding) field.encoding;
      if (encodings.contains(me))
        continue;

      if (me.getPackage() != pkg)
        continue;
      add(me);
    }
  }

  public String build()
  {
    StringBuilder sb = new StringBuilder();
    String name = "unnamed";
    sb.append("syntax = \"proto3\";\n");
    sb.append("package ").append(name).append("\n");
    for (Map.Entry<String, String> entry : options.entrySet())
    {
      sb.append("option ").append(entry.getKey()).append(" = \"").append(entry.getValue()).append("\"\n");
    }
    for (String include : includes)
    {
      sb.append("include \"").append(include).append("\"\n");
    }
    for (MessageEncoding e : this.encodings)
    {
      sb.append("\n");
      sb.append("message ").append(e.getSchemaName()).append("{\n");
      for (ProtoField field : e.getFields())
      {
        if (field.id == -1)
          continue;
        sb.append("  ");
        if (field.repeated)
          sb.append("repeated ");
        sb.append(field.encoding.getSchemaName()).append(" ");
        sb.append(field.name);
        sb.append(" = ");
        sb.append(field.id);
        sb.append(field.encoding.getSchemaOptions()).append(";\n");
      }
      sb.append("}\n");
    }
    return sb.toString();
  }
}
