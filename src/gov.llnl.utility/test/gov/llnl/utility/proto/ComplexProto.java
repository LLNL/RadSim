/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

import java.util.HashSet;

/**
 *
 * @author nelson85
 */
public class ComplexProto extends MessageEncoding<Complex>
{
  final static ProtoField[] fields;

  static
  {
    ProtoBuilder<Complex, Complex> builder = newBuilder(null, "A", Complex::new);
    builder.field("a", 1).type(Type.Int32).asInt((o) -> o.a, (o, v) -> o.a = v);
    builder.field("b", 2).type(Type.SInt32).asInt((o) -> o.b, (o, v) -> o.b = v);
    builder.field("c", 3).type(Type.FixedInt32).asInt((o) -> o.c, (o, v) -> o.c = v);
    builder.field("name", 4).string().as((o) -> o.name, (o, v) -> o.name = v);
    builder.field("strs", 5).list(Type.String, HashSet::new).as((o) -> o.strs, (o, v) -> o.strs.addAll(v));
    builder.field("h", 6).map(Type.String, Type.Int32).as((o) -> o.map, (o, v) -> o.map = v);
    builder.field("f", 7).packed(Type.Float).as((o) -> o.f, (o, v) -> o.f = v);
    fields = builder.toFields();
  }

  @Override
  public ProtoField[] getFields()
  {
    return this.fields;
  }
}
