
import static gov.llnl.utility.ArrayEncoding.encodeDoublesArray;

/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
/**
 *
 * @author nelson85
 */
public class ArrayTest
{
  static public void main(String[] args)
  {
    double[][] out = new double[][]
    {
      {
        1. / 11., 1. / 13., 1. / 9.
      },
      {
        5. / 7., 6. / 7.,
      },
      {
        9. / 17., 8. / 3., 10. / 7.
      }
    };
    System.out.println(encodeDoublesArray(out, true));
  }

  /*
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
    Base64.Encoder encoder = Base64.getEncoder();
    ByteBuffer bb = ByteBuffer.allocate(memorySize);
    bb.order(ByteOrder.BIG_ENDIAN);
    bb.put(memberType.getBytes());
    bb.putInt(cols);
    System.out.println("member size "+memorySize+" "+(12+4*9));
          System.out.println("remaining "+bb.remaining());
    if (!variableLayout)
    {
      bb.putInt(rows);
      if (!compact)
      {
        for (double[] v1 : values)
        {
          for (double v : v1)
          {
            bb.putDouble(v);
          }
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
      return encoder.encodeToString(ArrayEncoding.deflate(bb.array()));
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
        for (double v : v1)
        {
          bb.putDouble(v);
        }
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
    return encoder.encodeToString(ArrayEncoding.deflate(bb.array()));
  }

  public static double[][] decodeDoublesArray(String contents)
  {
    Base64.Decoder decoder = Base64.getDecoder();
    byte[] bytes = ArrayEncoding.inflate(decoder.decode(contents));
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
   */
}
