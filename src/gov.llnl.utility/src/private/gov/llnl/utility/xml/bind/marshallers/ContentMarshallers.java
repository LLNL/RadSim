/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind.marshallers;

import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.xml.bind.Marshaller;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author nelson85
 */
@Internal
public class ContentMarshallers
{
  public static List<Marshaller> getDefault()
  {
    Marshaller[] marshallers = new Marshaller[]
    {
      new StringMarshaller(),
      new BooleanMarshaller(),
      new IntegerMarshaller(),
      new IntegerArrayMarshaller(),
      new LongMarshaller(),
      new LongArrayMarshaller(),
      new FloatMarshaller(),
      new FloatArrayMarshaller(),
      new FloatsArrayMarshaller(),
      new DoubleMarshaller(),
      new DoubleArrayMarshaller(),
      new DoublesArrayMarshaller(),
      new UUIDMarshaller(),
      new InstantMarshaller()
    };
    return Arrays.asList(marshallers);
  }














}
