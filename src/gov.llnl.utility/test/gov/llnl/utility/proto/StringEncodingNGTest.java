/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class StringEncodingNGTest
{

  public StringEncodingNGTest() throws ProtoException
  {
  }

  public static class A
  {
    String s;

    public A()
    {
    }

    public A(String i)
    {
      this.s = i;
    }

    public String getS()
    {
      return s;
    }

    public void setS(String i)
    {
      this.s = i;
    }

  }

  public static class AProto extends MessageEncoding<A>
  {
    final static ProtoField[] FIELDS;

    static
    {
      ProtoBuilder<A, A> builder = newBuilder(null, "A", A::new);
      builder.field("i", 1).string().as(A::getS, A::setS);
      FIELDS = builder.toFields();
    }

    @Override
    public ProtoField[] getFields()
    {
      return FIELDS;
    }
  }

  @Test
  public void testEndToEnd() throws ProtoException
  {
    A a = new A("hello");
    MessageEncoding<A> encoding = new AProto();
    byte[] bytes = encoding.toBytes(a);
    a = encoding.parseBytes(bytes);
    System.out.println(a.s);
    for (byte b : bytes)
    {
      System.out.print(String.format("%02x ", b & 0xff));
    }
    System.out.println();
    assertEquals(a.s, "hello");
  }

  @Test
  public void testParseField() throws Exception
  {
    ProtoContext context = new ProtoContext();
    ProtoField field = AProto.FIELDS[1];
    int type = 2;
    A o = new A(null);
    ByteBuffer b = ByteBuffer.allocate(3);
    b.put((byte) 2);
    b.put((byte) 'A');
    b.put((byte) 'B');
    b.rewind();
    StringEncoding instance = new StringEncoding();
    instance.parseField(context, field, type, o, ByteSource.wrap(b));
    assertEquals(o.s, "AB");
  }

  @Test(expectedExceptions = ProtoException.class)
  public void testParseFieldUnderflow() throws Exception
  {
    ProtoContext context = new ProtoContext();
    ProtoField field = AProto.FIELDS[1];
    int type = 2;
    A o = new A(null);
    ByteBuffer b = ByteBuffer.allocate(1);
    b.order(ByteOrder.LITTLE_ENDIAN);
    b.put((byte) 10);
    b.rewind();
    StringEncoding instance = new StringEncoding();
    instance.parseField(context, field, type, o, ByteSource.wrap(b));
  }

  @Test(expectedExceptions = ProtoException.class)
  public void testParseFieldBadWire() throws Exception
  {
    ProtoContext context = new ProtoContext();
    ProtoField field = AProto.FIELDS[1];
    int type = 6;
    A o = new A(null);
    ByteBuffer b = ByteBuffer.allocate(8);
    b.put((byte) 1);
    StringEncoding instance = new StringEncoding();
    instance.parseField(context, field, type, o, ByteSource.wrap(b));
  }

  @Test
  public void testSerializeField() throws ProtoException
  {
    ProtoField field = AProto.FIELDS[1];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Object obj = new A("hello");
    StringEncoding instance = new StringEncoding();
    instance.serializeField(field, baos, obj);
    assertEquals(baos.toByteArray(), new byte[]
    {
      0x0a, 5, 0x68, 0x65, 0x6c, 0x6c, 0x6f
    });
  }

}
