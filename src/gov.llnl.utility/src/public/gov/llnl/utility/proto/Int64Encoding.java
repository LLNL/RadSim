/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

import java.io.ByteArrayOutputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;
import java.util.function.ToLongFunction;

/**
 * Encoding for a signed int64 with variable length encoding.
 *
 * Negative numbers store poorly in this encoding.
 *
 * @author nelson85
 */
public class Int64Encoding extends LongEncoding
{
  final static Int64Encoding INSTANCE = new Int64Encoding();

  @Override
  public void parseField(ProtoContext context, ProtoField field, int type, Object o, ByteSource b)
          throws ProtoException
  {
    if (type != 0)
      throw new ProtoException("bad wire type", b.position());
    if (field.setter instanceof BiConsumer)
      ((BiConsumer) field.setter).accept(o, Int64Encoding.decodeVInt64(b));
    else
      ((ObjLongConsumer) field.setter).accept(o, Int64Encoding.decodeVInt64(b));
  }

  @Override
  public void serializeField(ProtoField field, ByteArrayOutputStream baos, Object obj)
  {
    long result;
    if (field.getter instanceof Function)
    {
      Object value = ((Function) field.getter).apply(obj);
      if (value == null)
        return;
      result = (Long) value;
    }
    else
      result = ((ToLongFunction) field.getter).applyAsLong(obj);
    // field and wire type
    if (field.id != -1)
      baos.write((field.id << 3));
    // contents
    Int64Encoding.encodeVInt64(baos, result);
  }

  static long decodeVInt64(ByteSource bs) throws ProtoException
  {
    long i = 0;
    int shift = 0;
    while (true)
    {
      int j = bs.get();
      if (j == -1)
        throw new ProtoException("truncated fixed int field", bs.position());
      i |= (j & (0x7f)) << shift;
      if ((j & 0x80) == 0)
        break;
      shift += 7;
    }
    return i;
  }

  static long decodeVInt64(ByteBuffer bs) throws ProtoException
  {
    try
    {
      long i = 0;
      int shift = 0;
      while (true)
      {
        int j = bs.get();
        i |= (j & (0x7f)) << shift;
        if ((j & 0x80) == 0)
          break;
        shift += 7;
      }
      return i;
    }
    catch (BufferUnderflowException ex)
    {
      throw new ProtoException("truncated fixed int field", bs.position());
    }
  }

  static void encodeVInt64(ByteArrayOutputStream baos, long v)
  {
    while (true)
    {
      if (v >= 0 && v < 0x80)
      {
        baos.write((byte) v);
        return;
      }
      baos.write((byte) ((v & 0x7f) | 0x80));
      v >>>= 7;
    }
  }

  @Override
  public int getWireType()
  {
    return 0;
  }

  @Override
  public String getSchemaName()
  {
    return "int64";
  }

}
