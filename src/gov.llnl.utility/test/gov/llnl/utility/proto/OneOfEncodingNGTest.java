/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class OneOfEncodingNGTest
{
  static class A
  {
  };

  static class B extends A
  {
    int i = 1;
  }

  static class BEncoding extends MessageEncoding<B>
  {
    final static ProtoField[] FIELDS;

    static
    {
      var builder = MessageEncoding.newBuilder(null, "B", B::new);
      builder.field("i", 1).type(Type.Int32)
              .as((o) -> o.i, (o, v) -> o.i = v);
      FIELDS = builder.toFields();
    }

    @Override
    public ProtoField[] getFields()
    {
      return FIELDS;
    }
  }

  static class C extends A
  {
    int j = 2;
  }

  static class CEncoding extends MessageEncoding<C>
  {
    final static ProtoField[] FIELDS;

    static
    {
      var builder = MessageEncoding.newBuilder(null, "C", C::new);
      builder.field("j", 1).type(Type.Int32)
              .as((o) -> o.j, (o, v) -> o.j = v);
      FIELDS = builder.toFields();
    }

    @Override
    public ProtoField[] getFields()
    {
      return FIELDS;
    }
  }

  static class D
  {
    A a;
  }

  static class DEncoding extends MessageEncoding<D>
  {
    final static ProtoField[] FIELDS;

    static
    {
      var builder = MessageEncoding.newBuilder(null, "D", D::new);
      builder.field("a", 1).oneof(A.class)
              .add(1, B.class, new BEncoding())
              .add(2, C.class, new CEncoding())
              .as((o) -> o.a, (o, v) -> o.a = v);
      FIELDS = builder.toFields();
    }

    @Override
    public ProtoField[] getFields()
    {
      return FIELDS;
    }

  }

  @Test
  void testOneOf() throws ProtoException
  {
    D d = new D();
    d.a = new B();
    DEncoding encoding = new DEncoding();
    byte[] bytes = encoding.toBytes(d);
    D d2 = encoding.parseBytes(bytes);
    assertTrue(d2.a instanceof B);
    d.a = new C();
    bytes = encoding.toBytes(d);
    d2 = encoding.parseBytes(bytes);
    assertTrue(d2.a instanceof C);
  }

}
