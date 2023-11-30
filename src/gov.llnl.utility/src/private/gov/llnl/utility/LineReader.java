/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

/**
 * Buffered Reader that reads lines including deliminators.
 *
 * @author nelson85
 */
class LineReader implements Closeable, Iterable<String>
{
  Reader stream;
  char[] buffer = new char[4096];
  int start = 0;
  int end = 0;
  StringBuilder sb = new StringBuilder();

  public LineReader(Reader stream)
  {
    this.stream = stream;
  }

  public LineReader(InputStream stream)
  {
    this.stream = new InputStreamReader(stream);
  }

  /**
   * Get the readLine line. Lines are denoted by either CR, LF, or CRLF.
   *
   * @return the readLine line including delimiters or null if no lines exist.
   * @throws IOException
   */
  String readLine() throws IOException
  {
    sb.setLength(0);
    boolean checkLF = false;
    while (true)
    {
      if (end == -1)
        return null;

      for (int i = start; i < end; ++i)
      {
        // If we already have \r then we will need to check for \n
        if (checkLF == true)
        {
          if (buffer[i] == '\n')
          {
            sb.append(buffer, start, i - start + 1);
            start = i + 1;
          }
          return sb.toString();
        }

        // \r is first hit
        if (buffer[i] == '\r')
        {
          sb.append(buffer, start, i - start + 1);
          start = i + 1;
          checkLF = true;
        }

        // \n is first hit
        if (buffer[i] == '\n')
        {
          sb.append(buffer, start, i - start + 1);
          start = i + 1;
          return sb.toString();
        }
      }

      // Okay we are out of buffer, so the rest is text 
      if (start < end)
        sb.append(buffer, start, end - start);
      fill();
      if (end == -1)
        return sb.toString();
    }
  }

  /**
   * Refill the buffer.
   *
   * @throws IOException
   */
  private void fill() throws IOException
  {
    start = 0;
    end = stream.read(buffer);
  }

  /**
   * Close the underlying stream.
   *
   * @throws IOException
   */
  @Override
  public void close() throws IOException
  {
    stream.close();
  }

  @Override
  public Iterator<String> iterator()
  {
    return new LineIterator();
  }

  class LineIterator implements Iterator<String>
  {
    String line;

    LineIterator()
    {
      try
      {
        line = LineReader.this.readLine();
      }
      catch (IOException ex)
      {
        throw new RuntimeException(ex);
      }
    }

    @Override
    public boolean hasNext()
    {
      return (line != null);
    }

    @Override
    public String next()
    {
      String out = line;
      try
      {
        line = LineReader.this.readLine();
      }
      catch (IOException ex)
      {
        throw new RuntimeException(ex);
      }
      return out;
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException("Not supported.");
    }

  }
}
