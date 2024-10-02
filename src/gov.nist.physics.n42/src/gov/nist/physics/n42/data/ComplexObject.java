/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
public abstract class ComplexObject
{
  private final List<String> remarks = new ArrayList<>();
  private String id;
  List<Object> extensions = new ArrayList<>();

  final public String getId()
  {
    return id;
  }

  final public void setId(String id)
  {
    this.id = id;
  }

  final public List<String> getRemarks()
  {
    return this.remarks;
  }

  final public void addRemark(String remark)
  {
    this.remarks.add(remark);
  }

  public void addExtension(Object o)
  {
    this.extensions.add(o);
  }

  public List<Object> getExtensions()
  {
    return this.extensions;
  }

  public void visitReferencedObjects(Consumer<ComplexObject> visitor)
  {
  }
}
