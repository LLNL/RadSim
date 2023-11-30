/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

import java.io.ByteArrayOutputStream;

/**
 *  Special encoding that includes one of a given type.
 * 
 * All types must derive from a common base.
 * 
 * @author nelson85
 */
public class OneOfEncoding<T> extends MessageEncoding<T>
{
  final Class<T> cls;
  final ProtoBuilder builder;
  ProtoField[] fields;

  public OneOfEncoding(String name, Class<T> cls)
  {
    this.cls = cls;
    this.builder = newBuilder(null, name, () -> new Object[1],
            (p) -> (T) ((Object[]) p)[0]
    );
  }

  public void add(int code, Class<? extends T> cls, MessageEncoding<? extends T> encoding)
  {
    builder.field("c" + code, code, () -> new ClassField(cls)).encoding(encoding)
            .as(o->o, (o,v)->((Object[])o)[0]=v);
  }

  private static class ClassField extends ProtoField
  {
    Class cls;

    ClassField(Class cls)
    {
      this.cls = cls;
    }
  }

  /**
   * Serialize a double[][][] into bytes.
   *
   * @param values are the array to serialize, may be null.
   * @return the byte representation for this.
   */
  @Override
  public byte[] serializeContents(ProtoContext context, T values) throws ProtoException
  {
    if (values == null)
    {
      return null;
    }
    // Figure out which encoding is best
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    for (ProtoField f : getFields())
    {
      if (f instanceof ClassField)
      {
        var u = (ClassField) f;
        if (u.cls.isInstance(values))
        {
          u.encoding.serializeField(f, baos, values);
          return baos.toByteArray();
        }
      }
    }
    return baos.toByteArray();
  }

  @Override
  public ProtoField[] getFields()
  {
    if (fields == null)
      fields = builder.toFields();
    return fields;
  }

}
