/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix.io;

import gov.llnl.math.MathExceptions.SizeException;
import gov.llnl.math.matrix.Matrix;
import gov.llnl.math.matrix.MatrixFactory;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * This is a simple reader to extract the named DoubleMatrix from a Matlab .mat
 * file. 
 * 
 * Not every case may be handled properly as the data format is rather
 * vague on the details of padding and data conversions.
 * 
 * NOTICE  Matlab has changed format since this was last used.  But our use 
 * of Matlab has waned so we likely will simply discontinue this in the future.
 *
 * @author nelson85
 */
public class MatlabReader
{
  // Define the constants used for Data Elements
  static final int MI_INT8 = 1;
  static final int MI_UINT8 = 2;
  static final int MI_INT16 = 3;
  static final int MI_UINT16 = 4;
  static final int MI_INT32 = 5;
  static final int MI_UINT32 = 6;
  static final int MI_SINGLE = 7;
  static final int MI_DOUBLE = 9;
  static final int MI_MATRIX = 14;
  static final int MI_COMPRESSED = 15;

  // Define the constants used for Matrix types
  static final int MX_CELL_CLASS = 1;
  static final int MX_STRUCT_CLASS = 2;
  static final int MX_OBJECT_CLASS = 3;
  static final int MX_CHAR_CLASS = 4;
  static final int MX_SPARSE_CLASS = 5;
  static final int MX_DOUBLE_CLASS = 6;
  static final int MX_SINGLE_CLASS = 7;
  static final int MX_INT8_CLASS = 8;
  static final int MX_UINT8_CLASS = 9;
  static final int MX_INT16_CLASS = 10;
  static final int MX_UINT16_CLASS = 11;
  static final int MX_INT32_CLASS = 12;
  static final int MX_UINT32_CLASS = 13;
  static final int MX_INT64_CLASS = 14;
  static final int MX_UINT64_CLASS = 15;

  public static class MatlabReaderException extends Exception
  {
    public MatlabReaderException(String reason)
    {
      super(reason);
    }
  }

  // We will store the contents in a map that we can access
  HashMap<String, Object> map = new HashMap<>();

  // Fetch one of the named variables
  public Object get(String name)
  {
    return map.get(name);
  }

  // Load the data into the class
  public void load(File file)
          throws MatlabReaderException, FileNotFoundException,
          IOException, DataFormatException
  {
    map.clear();

    if (!file.canRead())
      throw new MatlabReaderException("Unable to read file");

    try (DataInputStream is = new DataInputStream(new FileInputStream(file)))
    {

      byte[] header = new byte[128];
      byte[] magic = new byte[116];
      is.readFully(header);
      ByteBuffer bb = ByteBuffer.wrap(header);
      bb.order(ByteOrder.LITTLE_ENDIAN);
      bb.get(magic);
      bb.getInt();  // subsystem
      bb.getInt();  // subsystem
      int version = bb.getShort();
      int endian = bb.getShort();

      String headerString = new String(magic);

      if (endian != 19785)
      {
        throw new MatlabReaderException("Header parsing error");
      }

      byte[] dataHeader = new byte[8];
      while (true)
      {
        try
        {
          is.readFully(dataHeader);
        }
        catch (EOFException ex)
        {
          break;
        }
        bb = ByteBuffer.wrap(dataHeader);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        int type = bb.getInt();
        int size = bb.getInt();

        byte data[] = new byte[size];
        is.readFully(data);
        ByteBuffer bb2 = ByteBuffer.wrap(data);
        bb2.order(ByteOrder.LITTLE_ENDIAN);

        Variable variable;
        if (type == MI_COMPRESSED)
        {
          variable = processCompressed(data);
        }
        else
        {
          variable = processVariable(type, size, bb2);
        }

        if (variable != null)
        {
          map.put(variable.name, variable.content);
        }
      }
    }
  }

  public static class Variable
  {
    String name;
    Object content;

    private Variable(String name, Object content)
    {
      this.name = name;
      this.content = content;
    }
  }

  // Most often the variable is compressed, so we must uncompress it first
  private Variable processCompressed(byte[] buffer)
          throws DataFormatException, MatlabReaderException
  {
    // Matlab stores in GZIP compressed format by variable, use inflater
    byte[] header = new byte[8];
    Inflater infl = new Inflater();
    infl.setInput(buffer);

    // First get the header which tells us how much data to expect
    infl.inflate(header);
    ByteBuffer bb = ByteBuffer.wrap(header);
    bb.order(ByteOrder.LITTLE_ENDIAN);
    int type = bb.getInt();
    int size = bb.getInt();

    // Get the data portion
    byte[] data = new byte[size];
    infl.inflate(data);
    ByteBuffer bb2 = ByteBuffer.wrap(data);
    bb2.order(ByteOrder.LITTLE_ENDIAN);

    // Process the variable 
    return processVariable(type, size, bb2);
  }

  // Load an individual Data element from matlab into memory
  private Variable processVariable(int type, int size, ByteBuffer bb)
          throws MatlabReaderException
  {
    if (type == MI_MATRIX)
    {
      Element elem = new Element();

      int subelementType;
      //  int subelementSize;
      // 4 Subselements are expected (+1 optional)
      //   Array Flages (UINT32)
      //   Dimensions 
      //   Array Name
      //   Real
      //   Image

      // First sub element is the array flags
      subelementType = fetch(elem, bb);
      if (subelementType != MI_UINT32 || elem.contentInt32.length != 2)
      {
        throw new MatlabReaderException("Matrix array flags invalid");
      }
      int arrayFlags1 = elem.contentInt32[0];
      int arrayClass = arrayFlags1 & 0xf;
      boolean hasComplex = (arrayFlags1 & 0x20) == 0x20;

      // For now we are limited in support
      if (arrayClass == MatlabReader.MX_DOUBLE_CLASS)
      {
        return processDoubleMatrix(bb, hasComplex);
      }

      if (arrayClass == MatlabReader.MX_CELL_CLASS)
      {
        return processCell(bb);
      }

      throw new MatlabReaderException("Unhandled matlab type " + type);
    }
    return null;
  }

  private Variable processDoubleMatrix(ByteBuffer bb, boolean hasComplex)
          throws MatlabReaderException
  {
    // Read the next subselement
    Element elem = new Element();
    Matrix matrix = null;
    double[] v = null;

    // Retrieve the dimensions
    int subelementType = fetch(elem, bb);
    if (subelementType != MI_INT32)
    {
      throw new MatlabReaderException("Matrix dimensions invalid");
    }
    int dims[] = elem.contentInt32;

    // Retrieve the name
    subelementType = fetch(elem, bb);
    if (subelementType != MI_INT8)
    {
      throw new MatlabReaderException("Bad matrix name");
    }
    String name = new String(elem.contentInt8);

    // Though the type is a double array, the actual contents can be
    // any of the Matlab supported types.  Thus we will need to convert
    // the types to double on the fly here.
    subelementType = fetch(elem, bb);
    switch (subelementType)
    {
      case MatlabReader.MI_DOUBLE:
        v = elem.contentDouble;
        break;

      case MatlabReader.MI_SINGLE:
        v = new double[dims[0] * dims[1]];
        for (int i = 0; i < elem.contentSingle.length; ++i)
        {
          v[i] = elem.contentSingle[i];
        }
        break;

      case MatlabReader.MI_INT32:
        v = new double[dims[0] * dims[1]];
        for (int i = 0; i < elem.contentInt32.length; ++i)
        {
          v[i] = elem.contentInt32[i];
        }
        break;

      case MatlabReader.MI_INT16:
        v = new double[dims[0] * dims[1]];
        for (int i = 0; i < elem.contentInt16.length; ++i)
        {
          v[i] = elem.contentInt16[i];
        }
        break;

      case MatlabReader.MI_INT8:
        v = new double[dims[0] * dims[1]];
        for (int i = 0; i < elem.contentInt8.length; ++i)
        {
          v[i] = elem.contentInt8[i];
        }
        break;

      case MatlabReader.MI_UINT32:
        v = new double[dims[0] * dims[1]];
        for (int i = 0; i < elem.contentInt32.length; ++i)
        {
          v[i] = convertUnsignedToDouble(elem.contentInt32[i]);
        }
        break;

      case MatlabReader.MI_UINT16:
        v = new double[dims[0] * dims[1]];
        for (int i = 0; i < elem.contentInt16.length; ++i)
        {
          v[i] = convertUnsignedToDouble(elem.contentInt16[i]);
        }
        break;

      case MatlabReader.MI_UINT8:
        v = new double[dims[0] * dims[1]];
        for (int i = 0; i < elem.contentInt8.length; ++i)
        {
          v[i] = convertUnsignedToDouble(elem.contentInt8[i]);
        }
        break;
    }
    try
    {
      matrix = MatrixFactory.wrapArray(v, dims[0], dims[1]);
    }
    catch (SizeException ex)
    {
      throw new RuntimeException(ex);
    }

//    System.out.println("Found matrix "+name);
    return new Variable(name, matrix);
  }

  private Variable processCell(ByteBuffer bb) throws MatlabReaderException
  {
    Element elem = new Element();

    // Retrieve the dimensions
    int subelementType = fetch(elem, bb);
    if (subelementType != MI_INT32)
    {
      throw new MatlabReaderException("Matrix dimensions invalid");
    }
    int dims[] = elem.contentInt32;

    // Retrieve the name
    subelementType = fetch(elem, bb);
    if (subelementType != MI_INT8)
    {
      throw new MatlabReaderException("Bad matrix name");
    }
    String name = new String(elem.contentInt8);

    LinkedList content = new LinkedList();
    for (int i = 0; i < dims[0] * dims[1]; ++i)
    {
      int type = bb.getInt();
      int size = bb.getInt();
      Variable variable = processVariable(type, size, bb);
      content.add(variable.content);
    }
//    System.out.println("Found cell "+name);

    return new Variable(name, content);
  }

  // Java lacks unsigned types and is thus the ban of any programer that
  // has to deal with them.  Thus when matlab stores in an unsigned type
  // we must be careful how we process them
  private static double convertUnsignedToDouble(byte i)
  {
    if (i < 0)
    {
      return (double) i + 256.0;
    }
    return i;
  }

  private static double convertUnsignedToDouble(short i)
  {
    if (i < 0)
    {
      return (double) i + 65536.0;
    }
    return i;
  }

  private static double convertUnsignedToDouble(int i)
  {
    if (i < 0)
    {
      return (double) i + 4294967296.0;
    }
    return i;
  }

  // Helper class for dealing with the many data types that can be stored
  public static class Element
  {
    public byte[] contentInt8;
    public short[] contentInt16;
    public int[] contentInt32;
    public float[] contentSingle;
    public double[] contentDouble;
  }

  // method to conver a subelement of a matrix record
  private static int fetch(Element elem, ByteBuffer bb) throws MatlabReaderException
  {
    int type = bb.getInt();
    int length;
    if ((type & 0xffff0000) == 0)
    {
      length = bb.getInt();
    }
    else
    {
      // Short type
      length = (type & 0xffff0000) >> 16;
      type = type & 0xffff;
    }
//    System.out.println("type=" + type + " length=" + length);

    switch (type)
    {
      case MatlabReader.MI_INT8:
      case MatlabReader.MI_UINT8:
        elem.contentInt8 = new byte[length];
        bb.get(elem.contentInt8);
        handlePadding(bb, length);

        break;
      case MatlabReader.MI_INT16:
      case MatlabReader.MI_UINT16:
        elem.contentInt16 = new short[length / 2];
        for (int i = 0; i < length / 2; ++i)
        {
          elem.contentInt16[i] = bb.getShort();
        }
        break;
      case MatlabReader.MI_INT32:
      case MatlabReader.MI_UINT32:
        elem.contentInt32 = new int[length / 4];
        for (int i = 0; i < length / 4; ++i)
        {
          elem.contentInt32[i] = bb.getInt();
        }
        break;
      case MatlabReader.MI_SINGLE:
        elem.contentSingle = new float[length / 4];
        for (int i = 0; i < length / 4; ++i)
        {
          elem.contentSingle[i] = bb.getFloat();
        }
        break;
      case MatlabReader.MI_DOUBLE:
        elem.contentDouble = new double[length / 8];
        for (int i = 0; i < length / 8; ++i)
        {
          elem.contentDouble[i] = bb.getDouble();
        }
        break;
      default:
        throw new MatlabReaderException("Unhandled type " + type);
    }
    return type;
  }

  private static void handlePadding(ByteBuffer bb, int length)
  {
    int pad;
    if (length < 8)
    {
      pad = (4 - (length & 0x3)) & 0x3;
    }
    else
    {
      pad = (8 - (length & 0x7)) & 0x7;
    }
    if (pad != 0)
    {
      for (int i = 0; i < pad; ++i)
      {
        bb.get();
      }
    }
  }
}
