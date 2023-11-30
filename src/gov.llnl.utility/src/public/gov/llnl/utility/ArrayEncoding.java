/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.DeflaterInputStream;
import java.util.zip.InflaterInputStream;

/**
 * Utility for encoding arrays of numbers in XML files.
 *
 * @author nelson85
 */
public class ArrayEncoding
{

  private static String base64Encode(byte[] in)
  {
    return base64Encode(in, 0, in.length);
  }

  private static String base64Encode(byte[] in, int start, int end)
  {
    Base64.Encoder encoder = Base64.getEncoder();
    ByteBuffer bb = ByteBuffer.wrap(in, start, end);
    ByteBuffer out = encoder.encode(bb);
    return new String(out.array());
  }

  private static byte[] base64Decode(String in) throws IllegalArgumentException
  {
    Base64.Decoder encoder = Base64.getDecoder();
    return encoder.decode(in.getBytes(UTF_8));
  }

  /**
   * Convert an array of shorts into an encoded string.
   *
   * @param values is a list of values, a null produces an empty string.
   * @return a string describing the contents.
   */
  static public String encodeShorts(short[] values)
  {
    if (values == null)
      return "";

    // Pack in a byte buffer
    int length = values.length;
    byte[] typeB = "[S;".getBytes(UTF_8);
    ByteBuffer bb = ByteBuffer.allocate(typeB.length + 4 + length * 2);
    bb.order(ByteOrder.BIG_ENDIAN);
    bb.put(typeB);
    bb.putInt(values.length);
    for (int i = 0; i < length; i++)
    {
      bb.putShort(values[i]);
    }
    int outSize = bb.capacity();
    byte[] out = bb.array();

    out = deflate(out);
    outSize = out.length;

    return base64Encode(out, 0, outSize);
  }

  /**
   * Convert an array of integers into an encoded string.
   *
   * @param values is a list of values, a null produces an empty string.
   * @return a string describing the contents.
   */
  static public String encodeIntegers(int[] values)
  {
    if (values == null)
      return "";

    // Pack in a byte buffer
    int length = values.length;
    byte[] typeB = "[I;".getBytes(UTF_8);
    ByteBuffer bb = ByteBuffer.allocate(typeB.length + 4 + length * 4);
    bb.order(ByteOrder.BIG_ENDIAN);
    bb.put(typeB);
    bb.putInt(values.length);
    for (int i = 0; i < length; i++)
    {
      bb.putInt(values[i]);
    }
    int outSize = bb.capacity();
    byte[] out = bb.array();

    out = deflate(out);
    outSize = out.length;

    return base64Encode(out, 0, outSize);
  }

  /**
   * Convert an array of integers into an encoded string.
   *
   * @param values is a list of values, a null produces an empty string.
   * @return a string describing the contents.
   */
  static public String encodeLongs(long[] values)
  {
    if (values == null)
      return "";

    // Pack in a byte buffer
    int length = values.length;
    byte[] typeB = "[L;".getBytes(UTF_8);
    ByteBuffer bb = ByteBuffer.allocate(typeB.length + 4 + length * 8);
    bb.order(ByteOrder.BIG_ENDIAN);
    bb.put(typeB);
    bb.putInt(values.length);
    for (int i = 0; i < length; i++)
    {
      bb.putLong(values[i]);
    }
    int outSize = bb.capacity();
    byte[] out = bb.array();

    out = deflate(out);
    outSize = out.length;

    return base64Encode(out, 0, outSize);
  }

  /**
   * Convert an array of floats into an encoded string.
   *
   * @param values is a list of values, a null produces an empty string.
   * @return a string describing the contents.
   */
  static public String encodeFloats(float[] values)
  {
    if (values == null)
      return "";

    // Pack in a byte buffer
    int length = values.length;
    byte[] typeB = "[F;".getBytes(UTF_8);
    ByteBuffer bb = ByteBuffer.allocate(typeB.length + 4 + length * 4);
    bb.order(ByteOrder.BIG_ENDIAN);
    bb.put(typeB);
    bb.putInt(values.length);
    for (int i = 0; i < length; i++)
    {
      bb.putFloat(values[i]);
    }
    int outSize = bb.capacity();
    byte[] out = bb.array();

    out = deflate(out);
    outSize = out.length;

    return base64Encode(out, 0, outSize);
  }

  /**
   * Convert an array of double into an encoded string.
   *
   * @param values is a list of values, a null produces an empty string.
   * @return a string describing the contents.
   */
  static public String encodeDoubles(double[] values)
  {
    if (values == null)
      return "";

    // Pack in a byte buffer
    int length = values.length;
    byte[] typeB = "[D;".getBytes(UTF_8);
    ByteBuffer bb = ByteBuffer.allocate(typeB.length + 4 + length * 8);
    bb.order(ByteOrder.BIG_ENDIAN);
    bb.put(typeB);
    bb.putInt(values.length);
    for (int i = 0; i < length; i++)
    {
      bb.putDouble(values[i]);
    }
    int outSize = bb.capacity();
    byte[] out = bb.array();

    out = deflate(out);
    outSize = out.length;

    return base64Encode(out, 0, outSize);
  }

  public static String encodeDoublesAsFloats(double[] data)
  {
    return encodeFloats(convertToFloats(data));
  }

  public static float[] convertToFloats(double[] values)
  {
    float[] out = new float[values.length];
    for (int i = 0; i < out.length; i++)
    {
      out[i] = (float) values[i];
    }
    return out;
  }

  public static int[] convertToInts(double[] values) throws DataFormatException
  {
    int[] out = new int[values.length];
    for (int i = 0; i < out.length; i++)
    {
      int value = (int) values[i];
      if (value != values[i])
        throw new DataFormatException("Data precision loss");
      out[i] = value;
    }
    return out;
  }

  static public byte[] deflate(byte[] contents)
  {
    try
    {
      ByteArrayInputStream input = new ByteArrayInputStream(contents);
      DeflaterInputStream dis = new DeflaterInputStream(input);
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      byte[] data = new byte[256];
      int nread;
      while ((nread = dis.read(data, 0, data.length)) != -1)
      {
        output.write(data, 0, nread);
      }
      return output.toByteArray();
    }
    catch (IOException ex)
    {
      throw new RuntimeException();
    }
  }

  static public byte[] inflate(byte[] contents)
  {
    try
    {
      ByteArrayInputStream input = new ByteArrayInputStream(contents);
      InflaterInputStream dis = new InflaterInputStream(input);
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      byte[] data = new byte[256];
      int nread;
      while ((nread = dis.read(data, 0, data.length)) != -1)
      {
        output.write(data, 0, nread);
      }
      return output.toByteArray();
    }
    catch (IOException ex)
    {
      throw new RuntimeException();
    }
  }

  static public float[] decodeFloats(String contents) throws ParseException
  {
    if (!contents.startsWith("e") && !contents.startsWith("W"))
    {
      if (contents.isEmpty())
        return null;
      if (isDouble(contents))
        return parseFloats(contents);
      throw new ParseException("unknown format", 1);
    }
    byte[] in = base64Decode(contents);
    //byte[] in = DatatypeConverter.parseBase64Binary(contents);
    if (in[0] != '[' && in[0] == 0x78)
    {
      in = inflate(in);
    }

    ByteBuffer bb = ByteBuffer.wrap(in);
    bb.order(ByteOrder.BIG_ENDIAN);
    byte[] typeB = new byte[3];
    bb.get(typeB);
    String type = new String(typeB);
    if ("[F;".equals(type))
    {
      int size = bb.getInt();
      int available = Math.min(size, bb.remaining() / 4);
      FloatBuffer ib = bb.asFloatBuffer();
      float[] tmp = new float[available];
      ib.get(tmp);
      float[] out = new float[size];
      for (int i = 0; i < available; i++)
      {
        out[i] = tmp[i];
      }
      return out;
    }
    if ("[D;".equals(type))
    {
      int size = bb.getInt();
      int available = Math.min(size, bb.remaining() / 8);
      DoubleBuffer ib = bb.asDoubleBuffer();
      double[] tmp = new double[available];
      ib.get(tmp);
      float[] out = new float[size];
      for (int i = 0; i < available; i++)
      {
        out[i] = (float) tmp[i];
      }
      return out;
    }
    if ("[I;".equals(type))
    {
      int size = bb.getInt();
      int available = Math.min(size, bb.remaining() / 4);
      IntBuffer ib = bb.asIntBuffer();
      int[] tmp = new int[available];
      ib.get(tmp);
      float[] out = new float[size];
      for (int i = 0; i < available; i++)
      {
        out[i] = tmp[i];
      }
      return out;
    }
    if ("[S;".equals(type))
    {
      int size = bb.getInt();
      int available = Math.min(size, bb.remaining() / 2);

      ShortBuffer ib = bb.asShortBuffer();
      short[] tmp = new short[available];
      ib.get(tmp);
      float[] out = new float[size];
      for (int i = 0; i < available; i++)
      {
        out[i] = tmp[i];
      }
      return out;
    }
    throw new ParseException("Unknown data format", 1);
  }

  static public double[] decodeDoubles(String contents) throws ParseException
  {
    if (!contents.startsWith("e") && !contents.startsWith("W"))
    {
      if (contents.isEmpty())
        return null;
      if (isDouble(contents))
        return parseDoubles(contents);
      throw new ParseException("unknown format", 1);
    }
    byte[] in = base64Decode(contents);
    //byte[] in = DatatypeConverter.parseBase64Binary(contents);
    if (in[0] != '[' && in[0] == 0x78)
    {
      in = inflate(in);
    }

    ByteBuffer bb = ByteBuffer.wrap(in);
    bb.order(ByteOrder.BIG_ENDIAN);
    byte[] typeB = new byte[3];
    bb.get(typeB);
    String type = new String(typeB);
    if ("[F;".equals(type))
    {
      int size = bb.getInt();
      int available = Math.min(size, bb.remaining() / 4);
      FloatBuffer ib = bb.asFloatBuffer();
      float[] tmp = new float[available];
      ib.get(tmp);
      double[] out = new double[size];
      for (int i = 0; i < available; i++)
      {
        out[i] = tmp[i];
      }
      return out;
    }
    if ("[D;".equals(type))
    {
      int size = bb.getInt();
      int available = Math.min(size, bb.remaining() / 8);
      double[] out = new double[size];
      bb.asDoubleBuffer().get(out, 0, available);
      return out;
    }
    if ("[I;".equals(type))
    {
      int size = bb.getInt();
      int available = Math.min(size, bb.remaining() / 4);
      IntBuffer ib = bb.asIntBuffer();
      int[] tmp = new int[available];
      ib.get(tmp);
      double[] out = new double[size];
      for (int i = 0; i < available; i++)
      {
        out[i] = tmp[i];
      }
      return out;
    }
    if ("[S;".equals(type))
    {
      int size = bb.getInt();
      int available = Math.min(size, bb.remaining() / 2);

      ShortBuffer ib = bb.asShortBuffer();
      short[] tmp = new short[available];
      ib.get(tmp);
      double[] out = new double[size];
      for (int i = 0; i < available; i++)
      {
        out[i] = tmp[i];
      }
      return out;
    }
    throw new ParseException("Unknown data format", 1);
  }

  public static int[] decodeIntegers(String contents) throws ParseException
  {
    if (!contents.startsWith("e") && !contents.startsWith("W"))
    {
      if (contents.isEmpty())
        return null;
      if (isDouble(contents))
        return parseIntegers(contents);
      throw new ParseException("unknown format", 1);
    }

    byte[] in = null;
    try
    {
      in = base64Decode(contents);
    }
    catch (IllegalArgumentException ex)
    {
      throw new ParseException("Unable to parse encoded integers\n   contents " + contents, 0);
    }
    //byte[] in = DatatypeConverter.parseBase64Binary(contents);
    if (in[0] != '[' && in[0] == 0x78)
    {
      in = inflate(in);
    }

    ByteBuffer bb = ByteBuffer.wrap(in);
    bb.order(ByteOrder.BIG_ENDIAN);
    byte[] typeB = new byte[3];
    bb.get(typeB);
    String type = new String(typeB);
    if ("[I;".equals(type))
    {
      int size = bb.getInt();
      int available = Math.min(size, bb.remaining() / 4);
      int[] out = new int[size];
      for (int i = 0; i < available; i++)
      {
        out[i] = bb.getInt();
      }
      return out;
    }

    return null;
  }

  public static long[] decodeLongs(String contents) throws ParseException
  {
    if (!contents.startsWith("e") && !contents.startsWith("W"))
    {
      if (contents.isEmpty())
        return null;
      if (isDouble(contents))
        return parseLongs(contents);
      throw new ParseException("unknown format", 1);
    }

    byte[] in = null;
    try
    {
      in = base64Decode(contents);
    }
    catch (IllegalArgumentException ex)
    {
      throw new ParseException("Unable to parse encoded integers\n   contents " + contents, 0);
    }
    //byte[] in = DatatypeConverter.parseBase64Binary(contents);
    if (in[0] != '[' && in[0] == 0x78)
    {
      in = inflate(in);
    }

    ByteBuffer bb = ByteBuffer.wrap(in);
    bb.order(ByteOrder.BIG_ENDIAN);
    byte[] typeB = new byte[3];
    bb.get(typeB);
    String type = new String(typeB);
    if ("[L;".equals(type))
    {
      int size = bb.getInt();
      int available = Math.min(size, bb.remaining() / 4);
      long[] out = new long[size];
      for (int i = 0; i < available; i++)
      {
        out[i] = bb.getLong();
      }
      return out;
    }

    return null;
  }

//<editor-fold desc="double-array">
  public static String toStringDoubles(double[] values)
  {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (double i : values)
    {
      if (!first)
      {
        sb.append(" ");
      }
      else
      {
        first = false;
      }
      sb.append(Double.toString(i));
    }
    return sb.toString();
  }

  public static String toStringDoubles(double[] values, String format)
  {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (double i : values)
    {
      if (!first)
      {
        sb.append(" ");
      }
      else
      {
        first = false;
      }
      sb.append(String.format(format, i));
    }
    return sb.toString();
  }

  static public boolean isDouble(String str)
  {
    Matcher matcher = Pattern.compile("^\\s*[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?").matcher(str);
    return matcher.find();
  }

  /**
   * Convert a string with separated doubles into a double array. This method
   * uses regular expressions so it can be rather slow for long lists.
   *
   * @param str is a string with whitespace delimited doubles
   * @return the array of doubles converted
   * @throws NumberFormatException
   */
  public static float[] parseFloats(String str) throws NumberFormatException
  {
    ArrayList<Float> out = new ArrayList<>();
    Matcher matcher = Pattern.compile("[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?").matcher(str);

    while (matcher.find())
    {
      float element = Float.parseFloat(matcher.group());
      out.add(element);
    }
    float out2[] = new float[out.size()];
    int i = 0;
    for (float d : out)
    {
      out2[i++] = d;
    }
    return out2;
  }

  /**
   * Convert a string with separated doubles into a double array. This method
   * uses regular expressions so it can be rather slow for long lists.
   *
   * @param str is a string with whitespace delimited doubles
   * @return the array of doubles converted
   * @throws NumberFormatException
   */
  public static double[] parseDoubles(String str) throws NumberFormatException
  {
    ArrayList< Double> out = new ArrayList<>();
    Matcher matcher = Pattern.compile("[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?").matcher(str);

    while (matcher.find())
    {
      double element = Double.parseDouble(matcher.group());
      out.add(element);
    }
    double out2[] = new double[out.size()];
    int i = 0;
    for (double d : out)
    {
      out2[i++] = d;
    }
    return out2;
  }

  /**
   * Convert a string with separated doubles into a double array. This method
   * uses regular expressions so it can be rather slow for long lists.
   *
   * @param str is a string with whitespace delimited doubles
   * @return the array of doubles converted
   * @throws NumberFormatException
   */
  public static int[] parseIntegers(String str) throws NumberFormatException
  {
    ArrayList<Integer> out = new ArrayList<>();
    Matcher matcher = Pattern.compile("[-+]?\\d+").matcher(str);

    while (matcher.find())
    {
      int element = Integer.parseInt(matcher.group());
      out.add(element);
    }
    int out2[] = new int[out.size()];
    int i = 0;
    for (int d : out)
    {
      out2[i++] = d;
    }
    return out2;
  }

  /**
   * Convert a string with separated doubles into a double array. This method
   * uses regular expressions so it can be rather slow for long lists.
   *
   * @param str is a string with whitespace delimited doubles
   * @return the array of doubles converted
   * @throws NumberFormatException
   */
  public static long[] parseLongs(String str) throws NumberFormatException
  {
    ArrayList<Long> out = new ArrayList<>();
    Matcher matcher = Pattern.compile("[-+]?\\d+").matcher(str);

    while (matcher.find())
    {
      long element = Long.parseLong(matcher.group());
      out.add(element);
    }
    long out2[] = new long[out.size()];
    int i = 0;
    for (long d : out)
    {
      out2[i++] = d;
    }
    return out2;
  }
//</editor-fold>
//<editor-fold desc="array of arrays">  

  public static String encodeFloatsArray(float[][] values)
  {
    int memberSize = 4;
    String memberType = "[[F";

    // First find out how many elements we have
    int size = 0;
    int cols = values.length;
    int rows = 0;
    for (float[] v : values)
    {
      int r = (v != null) ? v.length : 0;
      size += r;
      rows = (r > rows ? r : rows);
    }

    // 2 possiblities here.  The array may have a constant number of rows or variable
    int memorySize;
    boolean variableLayout = false;
    if (rows * cols == size)
    {
      memberType = memberType + "C";
      memorySize = 4 + 2 * 4 + memberSize * size;
    }
    else
    {
      memberType = memberType + "V";
      memorySize = 4 + 4 + 4 * cols + memberSize * size;
      variableLayout = true;
    }

    // Allocate the memory
    //Base64.Encoder encoder = Base64.getEncoder();
    ByteBuffer bb = ByteBuffer.allocate(memorySize);
    bb.order(ByteOrder.BIG_ENDIAN);
    bb.put(memberType.getBytes());
    bb.putInt(cols);
    if (!variableLayout)
    {
      bb.putInt(rows);
      for (float[] v1 : values)
      {
        bb.asFloatBuffer().put(v1);
        bb.position(bb.position() + v1.length * 4);
      }
      return base64Encode(ArrayEncoding.deflate(bb.array()));
    }

    // Varible layouts
    for (float[] v1 : values)
    {
      if (v1 == null)
      {
        bb.putInt(-1);
        continue;
      }
      bb.putInt(v1.length);
      bb.asFloatBuffer().put(v1);
      bb.position(bb.position() + v1.length * 4);
    }
    return base64Encode(ArrayEncoding.deflate(bb.array()));
  }

  public static String encodeDoublesArray(double[][] values, boolean compact)
  {
    int memberSize = compact ? 4 : 8;
    String memberType = compact ? "[[F" : "[[D";

    // First find out how many elements we have
    int size = 0;
    int cols = values.length;
    int rows = 0;
    for (double[] v : values)
    {
      int r = (v != null) ? v.length : 0;
      size += r;
      rows = (r > rows ? r : rows);
    }

    // 2 possiblities here.  The array may have a constant number of rows or variable
    int memorySize;
    boolean variableLayout = false;
    if (rows * cols == size)
    {
      memberType = memberType + "C";
      memorySize = 4 + 2 * 4 + memberSize * size;
    }
    else
    {
      memberType = memberType + "V";
      memorySize = 4 + 4 + 4 * cols + memberSize * size;
      variableLayout = true;
    }

    // Allocate the memory
    //Base64.Encoder encoder = Base64.getEncoder();
    ByteBuffer bb = ByteBuffer.allocate(memorySize);
    bb.order(ByteOrder.BIG_ENDIAN);
    bb.put(memberType.getBytes());
    bb.putInt(cols);
    if (!variableLayout)
    {
      bb.putInt(rows);
      if (!compact)
      {
        for (double[] v1 : values)
        {
          bb.asDoubleBuffer().put(v1);
          bb.position(bb.position() + v1.length * 8);
        }
      }
      else
      {
        for (double[] v1 : values)
        {
          for (double v : v1)
          {
            bb.putFloat((float) v);
          }
        }
      }
      return base64Encode(ArrayEncoding.deflate(bb.array()));
    }

    // Varible layouts
    if (!compact)
    {
      for (double[] v1 : values)
      {
        if (v1 == null)
        {
          bb.putInt(-1);
          continue;
        }
        bb.putInt(v1.length);
        bb.asDoubleBuffer().put(v1);
        bb.position(bb.position() + v1.length * 8);
      }
    }
    else
    {
      for (double[] v1 : values)
      {
        if (v1 == null)
        {
          bb.putInt(-1);
          continue;
        }
        bb.putInt(v1.length);
        for (double v : v1)
        {
          bb.putFloat((float) v);
        }
      }
    }
    return base64Encode(ArrayEncoding.deflate(bb.array()));
  }

  public static float[][] decodeFloatsArray(String contents)
  {
    byte[] bytes = ArrayEncoding.inflate(base64Decode(contents));
    ByteBuffer bb = ByteBuffer.wrap(bytes);
    bb.order(ByteOrder.BIG_ENDIAN);
    byte[] headerBytes = new byte[4];
    bb.get(headerBytes);
    String header = new String(headerBytes);
    if (header.startsWith("[[FC"))
    {
      int cols = bb.getInt();
      int rows = bb.getInt();
      float[][] out = new float[cols][];
      for (int i = 0; i < cols; i++)
      {
        float[] v = new float[rows];
        for (int j = 0; j < rows; j++)
        {
          v[j] = bb.getFloat();
        }
        out[i] = v;
      }
      return out;
    }

    if (header.startsWith("[[DC"))
    {
      int cols = bb.getInt();
      int rows = bb.getInt();
      float[][] out = new float[cols][];
      for (int i = 0; i < cols; i++)
      {
        float[] v = new float[rows];
        for (int j = 0; j < rows; j++)
        {
          v[j] = (float) bb.getDouble();
        }
        out[i] = v;
      }
      return out;
    }

    if (header.startsWith("[[FV"))
    {
      int cols = bb.getInt();
      float[][] out = new float[cols][];
      for (int i = 0; i < cols; i++)
      {
        int rows = bb.getInt();
        if (rows < 0)
          continue;

        float[] v = new float[rows];
        for (int j = 0; j < rows; j++)
        {
          v[j] = bb.getFloat();
        }
        out[i] = v;
      }
      return out;
    }

    if (header.startsWith("[[DV"))
    {
      int cols = bb.getInt();
      float[][] out = new float[cols][];
      for (int i = 0; i < cols; i++)
      {
        int rows = bb.getInt();
        if (rows < 0)
          continue;
        float[] v = new float[rows];
        for (int j = 0; j < rows; j++)
        {
          v[j] = (float) bb.getDouble();
        }
        out[i] = v;
      }
      return out;
    }
    return null;
  }

  public static double[][] decodeDoublesArray(String contents)
  {
//    Base64.Decoder decoder = Base64.getDecoder();
    byte[] bytes = ArrayEncoding.inflate(base64Decode(contents));
    ByteBuffer bb = ByteBuffer.wrap(bytes);
    bb.order(ByteOrder.BIG_ENDIAN);
    byte[] headerBytes = new byte[4];
    bb.get(headerBytes);
    String header = new String(headerBytes);
    if (header.startsWith("[[FC"))
    {
      int cols = bb.getInt();
      int rows = bb.getInt();
      double[][] out = new double[cols][];
      for (int i = 0; i < cols; i++)
      {
        double[] v = new double[rows];
        for (int j = 0; j < rows; j++)
        {
          v[j] = bb.getFloat();
        }
        out[i] = v;
      }
      return out;
    }

    if (header.startsWith("[[DC"))
    {
      int cols = bb.getInt();
      int rows = bb.getInt();
      double[][] out = new double[cols][];
      for (int i = 0; i < cols; i++)
      {
        double[] v = new double[rows];
        for (int j = 0; j < rows; j++)
        {
          v[j] = bb.getDouble();
        }
        out[i] = v;
      }
      return out;
    }

    if (header.startsWith("[[FV"))
    {
      int cols = bb.getInt();
      double[][] out = new double[cols][];
      for (int i = 0; i < cols; i++)
      {
        int rows = bb.getInt();
        if (rows < 0)
          continue;

        double[] v = new double[rows];
        for (int j = 0; j < rows; j++)
        {
          v[j] = bb.getFloat();
        }
        out[i] = v;
      }
      return out;
    }

    if (header.startsWith("[[DV"))
    {
      int cols = bb.getInt();
      double[][] out = new double[cols][];
      for (int i = 0; i < cols; i++)
      {
        int rows = bb.getInt();
        if (rows < 0)
          continue;
        double[] v = new double[rows];
        for (int j = 0; j < rows; j++)
        {
          v[j] = bb.getDouble();
        }
        out[i] = v;
      }
      return out;
    }
    return null;
  }
//</editor-fold>
}
