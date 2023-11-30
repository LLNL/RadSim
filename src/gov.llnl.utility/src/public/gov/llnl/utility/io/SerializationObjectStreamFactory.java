/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.io;

import gov.llnl.utility.PathUtilities;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author seilhan3
 */
public class SerializationObjectStreamFactory
{

  public static <T> InputSerializationObjectStream<T> createInputStream(Path path, Class<T> kls) throws IOException
  {
    return new InputSerializationObjectStream<>(path, kls);
  }

  public static <T> OutputSerializationObjectStream<T> createOutputStream(Path path, Class<T> kls) throws IOException
  {
    return new OutputSerializationObjectStream<>(path);
  }

  //<editor-fold desc="Stream Types" defaultstate="collapsed">
  public static class InputSerializationObjectStream<T> implements Closeable, Iterable<T>
  {
    private ObjectInputStream ois;
    private boolean endOfStream = false;
    private Class type;

    InputSerializationObjectStream(Path path, Class<T> kls) throws IOException
    {
      InputStream os = new BufferedInputStream(Files.newInputStream(path));
      if (PathUtilities.isGzip(path))
      {
        os = new GZIPInputStream(os);
      }
      ois = new ObjectInputStream(os);
      type = kls;
    }

    public T get()
    {
      if (endOfStream)
        return null;
      try
      {
        Object o = ois.readObject();
        if (o == null)
          return null;
        if (o instanceof StreamMarker)
        {
          StreamMarker marker = (StreamMarker) o;
          if (marker.isEnd())
          {
            endOfStream = true;
          }
          return get();
        }
        if (!type.isInstance(o))
        {
          throw new RuntimeException("Unknown Object Type " + o.getClass());
        }
        return (T) o;
      }
      catch (IOException | ClassNotFoundException ex)
      {
        throw new RuntimeException(ex);
      }

    }

    @Override
    public void close() throws IOException
    {
      ois.close();
    }

    @Override
    public Iterator<T> iterator()
    {
      return new Iterator<T>()
      {
        T next = get();

        @Override
        public boolean hasNext()
        {
          return !endOfStream;
        }

        @Override
        public T next()
        {
          T out = next;
          next = get();
          return out;
        }
      };
    }

  }

  public static class OutputSerializationObjectStream<T> implements Closeable
  {
    ObjectOutputStream oos;

    OutputSerializationObjectStream(Path os) throws IOException
    {
      OutputStream bos = new BufferedOutputStream(Files.newOutputStream(os));
      OutputStream gzs = new GZIPOutputStream(bos);
      oos = new ObjectOutputStream(gzs);
      oos.writeObject(StreamMarker.Start());
    }

    public void put(T o) throws IOException
    {
      oos.writeObject(o);
    }

    @Override
    public void close() throws IOException
    {
      oos.writeObject(StreamMarker.End());
      oos.flush();
      oos.close();
    }
  }
  //</editor-fold>

  //<editor-fold desc="Internal" defaultstate="collapsed">
  public static class StreamMarker implements Serializable
  {
    public static enum Type
    {
      END_OF_FILE,
      START_OF_FILE
    }

    private final Type t;

    public StreamMarker(Type t)
    {
      this.t = t;
    }

    public static StreamMarker Start()
    {
      return new StreamMarker(Type.START_OF_FILE);
    }

    public static StreamMarker End()
    {
      return new StreamMarker(Type.END_OF_FILE);
    }

    public boolean isEnd()
    {
      return t == Type.END_OF_FILE;
    }

    public boolean isStart()
    {
      return t == Type.START_OF_FILE;
    }

  }
  //</editor-fold>
}
