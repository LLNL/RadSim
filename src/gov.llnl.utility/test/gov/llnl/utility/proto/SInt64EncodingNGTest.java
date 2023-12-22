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
public class SInt64EncodingNGTest
{

  public SInt64EncodingNGTest() throws ProtoException
  {
  }

  public static class A
  {
    long i;
    Long j;
    public A(long i)
    {
      this.i = i;
      this.j = i;
    }

    public long getI()
    {
      return i;
    }

    public void setI(long i)
    {
      this.i = i;
    }

    public Long getJ()
    {
      return j;
    }

    public void setJ(Long j)
    {
      this.j = j;
    }
  }

  public static class AProto extends MessageEncoding<A>
  {
    final static ProtoField[] FIELDS;

    static
    {
      ProtoBuilder<A,A> builder = newBuilder(null,"A",()->new A(0));
      builder.field("i", 1).type(Type.SInt64).asLong(A::getI, A::setI);
      builder.field("j", 2).type(Type.SInt64).as(A::getJ, A::setJ);
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
    A a = new A(-12345);
    a.j = (long) 12345;
    MessageEncoding<A> encoding = new AProto();
    byte[] bytes = encoding.toBytes(a);
    a = encoding.parseBytes(bytes);
    System.out.println(a.i);
    System.out.println(a.j);
    for (byte b : bytes)
    {
      System.out.print(String.format("%02x ", b & 0xff));
    }
    System.out.println();
    assertEquals(a.i, -12345);
    assertEquals((long)a.j, 12345);
  }

  @Test
  public void testParseField() throws Exception
  {
    ProtoContext context = new ProtoContext();
    ProtoField field = AProto.FIELDS[1];
    int type = 0;
    A o = new A(0);
    ByteBuffer b = ByteBuffer.allocate(3);
    b.order(ByteOrder.LITTLE_ENDIAN);
    b.put((byte)0xf1);
    b.put((byte)0xc0);
    b.put((byte)0x01);
    b.rewind();
    SInt64Encoding instance = new SInt64Encoding();
    instance.parseField(context, field, type, o, ByteSource.wrap(b));
    assertEquals(o.i, -12345);
  }

  @Test(expectedExceptions = ProtoException.class)
  public void testParseFieldUnderflow() throws Exception
  {
    ProtoContext context = new ProtoContext();
    ProtoField field = AProto.FIELDS[1];
    int type = 1;
    A o = new A(0);
    ByteBuffer b = ByteBuffer.allocate(1);
    b.order(ByteOrder.LITTLE_ENDIAN);
    b.put((byte) 1);
    b.rewind();
    SInt64Encoding instance = new SInt64Encoding();
    instance.parseField(context, field, type, o, ByteSource.wrap(b));
    assertEquals(o.i, 12345);
  }

  @Test(expectedExceptions = ProtoException.class)
  public void testParseFieldBadWire() throws Exception
  {
    ProtoContext context = new ProtoContext();
    ProtoField field = AProto.FIELDS[1];
    int type = 6;
    A o = new A(0);
    ByteBuffer b = ByteBuffer.allocate(8);
    b.order(ByteOrder.LITTLE_ENDIAN);
    b.putLong(-12345);
    b.rewind();
    SInt64Encoding instance = new SInt64Encoding();
    instance.parseField(context, field, type, o, ByteSource.wrap(b));
    assertEquals(o.i, 12345);
  }

  @Test
  public void testSerializeField()
  {
    ProtoField field = AProto.FIELDS[1];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Object obj = new A(-12345);
    SInt64Encoding instance = new SInt64Encoding();
    instance.serializeField(field, baos, obj);
    assertEquals(baos.toByteArray(), new byte[]
    {
      0x08, (byte) 0xf1, (byte) 0xc0, 0x01
    });
  }

  @Test
  public void testGetWireType()
  {
    SInt64Encoding instance = new SInt64Encoding();
    int expResult = 0;
    int result = instance.getWireType();
    assertEquals(result, expResult);
  }

}
