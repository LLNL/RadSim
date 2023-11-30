/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

import java.nio.ByteBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Encoding for a string as UTF-8.
 * 
 * This is a private class.   Use Type.String to access.
 *
 * @author nelson85
 */
class StringEncoding extends MessageEncoding<String>
{
  final static StringEncoding INSTANCE = new StringEncoding();

  @Override
  public byte[] serializeContents(ProtoContext context, String obj)
  {
    return obj.getBytes(UTF_8);
  }

  @Override
  public String parseContents(ProtoContext context, ByteSource bs) throws ProtoException
  {
    int size = bs.remaining();
    ByteBuffer bb = bs.request(-1);
    if (bb.remaining() != size)
      throw new ProtoException("truncated string " + size + " " + bb.remaining(), bs.position());
    return UTF_8.decode(bb).toString();
  }

  @Override
  public ProtoField[] getFields()
  {
    return null;
  }

  @Override
  public String getSchemaName()
  {
    return "string";
  }

}
