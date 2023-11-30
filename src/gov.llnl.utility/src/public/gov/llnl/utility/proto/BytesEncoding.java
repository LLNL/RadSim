/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

import java.nio.ByteBuffer;

/**
 * Encoding for arbitrary byte data.
 *
 * @author nelson85
 */
public class BytesEncoding extends MessageEncoding<byte[]>
{
  final static BytesEncoding INSTANCE = new BytesEncoding();

  @Override
  public byte[] serializeContents(ProtoContext context, byte[] obj)
  {
    return obj;
  }

  @Override
  public byte[] parseContents(ProtoContext context, ByteSource bs)
          throws ProtoException
  {
    int size = bs.remaining();
    ByteBuffer bb = bs.request(-1);
    if (size != bb.remaining())
      throw new ProtoException("truncated bytes", bs.position());
    byte[] dst = new byte[bb.remaining()];
    bb.get(dst);
    return dst;
  }

  @Override
  public ProtoField[] getFields()
  {
    return null;
  }

  @Override
  public String getSchemaName()
  {
    return "bytes";
  }

}
