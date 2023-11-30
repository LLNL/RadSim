/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

import java.io.ByteArrayOutputStream;

/**
 * Base class for all ProtoBuf message and field types.
 *
 * @author nelson85
 * @param <T>
 */
public interface ProtoEncoding<T>
{

  /**
   * Common types used by the proto encoder.
   *
   * Uses these types when defining types with a builder.
   */
  public static class Type
  {
    /**
     * Encoding for boolean value.
     */
    public static final BooleanEncoding Bool = BoolEncodingImpl.INSTANCE;
    /**
     * Encoding for integer value with variable encoding.
     */
    public static final IntEncoding Int32 = Int32Encoding.INSTANCE;
    /**
     * Encoding for unsigned integer value with variable encoding.
     */
    public static final IntEncoding UInt32 = UInt32Encoding.INSTANCE;
    /**
     * Encoding for scissor integer value with variable encoding.
     */
    public static final IntEncoding SInt32 = SInt32Encoding.INSTANCE;
    /**
     * Encoding for integer value with fixed encoding.
     */
    public static final IntEncoding FixedInt32 = FixedInt32Encoding.INSTANCE;
    /**
     * Encoding for long value with variable encoding.
     */
    public static final LongEncoding Int64 = Int64Encoding.INSTANCE;
    /**
     * Encoding for unsigned long value with variable encoding.
     */
    public static final LongEncoding UInt4 = UInt64Encoding.INSTANCE;
    /**
     * Encoding for scissor long value with variable encoding.
     */
    public static final LongEncoding SInt64 = SInt64Encoding.INSTANCE;
    /**
     * Encoding for long value with fixed encoding.
     */
    public static final LongEncoding FixedInt64 = FixedInt64Encoding.INSTANCE;
    /**
     * Encoding for float value with fixed encoding.
     */
    public static final FloatEncoding Float = FloatEncodingImpl.INSTANCE;
    /**
     * Encoding for double value with fixed encoding.
     */
    public static final DoubleEncoding Double = DoubleEncodingImpl.INSTANCE;
    /**
     * Encoding for arbitrary bytes.
     */
    public static final MessageEncoding<byte[]> Bytes = BytesEncoding.INSTANCE;
    /**
     * Encoding for UTF-8 string.
     */
    public static final MessageEncoding<String> String = StringEncoding.INSTANCE;

    public static final MessageEncoding<double[]> NetworkDoubles = NetworkDoublesEncoding.INSTANCE;
    public static final MessageEncoding<double[][]> NetworkDoubles2 = NetworkDoubles2Encoding.INSTANCE;
    public static final MessageEncoding<double[][][]> NetworkDoubles3 = NetworkDoubles3Encoding.INSTANCE;

  }

  /**
   * Parse a field from message.
   *
   * Removes size and contents. The tag was removed by the caller. This method
   * is required to verify the wire type before proceding.
   *
   * @param context holds state data used to unpack repeated fields.
   * @param field is the field description such the field id, getter, and
   * setter.
   * @param type is the wire type used.
   * @param obj is the parent what will hold this field.
   * @param bs is the byte stream for this proto.
   * @throws ProtoException if there is an issue.
   */
  void parseField(ProtoContext context, ProtoField field, int type, Object obj, ByteSource bs)
          throws ProtoException;

  /**
   * Serialize a field into a message.
   *
   * Adds tag and size.
   *
   * @param field
   * @param baos
   * @param obj
   */
  void serializeField(ProtoField field, ByteArrayOutputStream baos, Object obj)
          throws ProtoException;

  default void parseFinish(ProtoContext context, ProtoField field, Object obj)
          throws ProtoException
  {
    // Do nothing
  }

  String getSchemaName();

  default String getSchemaOptions()
  {
    return "";
  }
}
