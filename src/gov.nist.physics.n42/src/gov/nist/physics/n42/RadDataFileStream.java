/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42;

import gov.llnl.utility.IteratorInputStream;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.DocumentReader;
import gov.nist.physics.n42.data.RadInstrumentData;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.xml.sax.InputSource;

/**
 *
 * @author nelson85
 */
public final class RadDataFileStream implements Iterator<RadInstrumentData>, Closeable
{
  private BufferedReader reader;
  public RadInstrumentData datum = null;
  private Path file;
  String first = null;

  public RadDataFileStream()
  {
  }

  public RadDataFileStream(Path file) throws IOException
  {
    open(file);
  }

  public void open(Path file) throws IOException
  {
    this.file = file;
    this.reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
  }

  @Override
  public void close() throws IOException
  {
    if (reader != null)
      reader.close();
    reader = null;
  }

  @Override
  public boolean hasNext()
  {
    if (datum != null)
      return true;

    try
    {
      // Otherwise fetch a new buffer
      List<String> lines = this.getNextBuffer();
      if (lines.isEmpty())
      {
        return false;
      }
      InputStream is = IteratorInputStream.create(lines);
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      InputSource source = new InputSource();
      source.setEncoding("UTF-8");
      source.setSystemId(file.toUri().toString());
      source.setByteStream(is);
      this.datum = dr.loadSource(source);
      return true;
    }
    catch (IOException ex)
    {
      return false;
    }
    catch (ReaderException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public RadInstrumentData next()
  {
    if (datum == null && !hasNext())
      throw new NoSuchElementException();
    RadInstrumentData out = this.datum;
    datum = null;
    return out;
  }

  /**
   * Fetch a group of lines the cover one document.
   *
   * @return
   * @throws IOException
   */
  public List<String> getNextBuffer() throws IOException
  {
    LinkedList<String> lines = new LinkedList<>();
    if (first != null)
      lines.add(first);
    first = null;
    for (String line; (line = reader.readLine()) != null;)
    {
      if (!lines.isEmpty() && line.startsWith("<?xml version"))
      {
        first = line;
        return lines;
      }
      lines.add(line);
    }
    return lines;
  }

}
