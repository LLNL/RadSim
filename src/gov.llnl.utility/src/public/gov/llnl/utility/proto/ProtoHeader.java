/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Internal class used to speed up lookups.
 * 
 * @author nelson85
 */
class ProtoHeader extends ProtoField
{
  final Object pkg;
  final Supplier allocator;
  final Function converter;
  final HashMap<Integer, ProtoField> map = new HashMap<>();
  
  ProtoHeader(Object pkg, String name, Supplier a, Function f)
  {
    this.pkg = pkg;
    this.id = -1;
    this.name = name;
    allocator =a;
    converter =f;
  }
  
}
