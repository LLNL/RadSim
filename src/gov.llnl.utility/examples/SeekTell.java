/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 *
 * @author nelson85
 */
public class SeekTell
{
  static public void main(String[] args) throws IOException
  {
    Path file = Paths.get("test.bin");
    try (
            SeekableByteChannel bc = Files.newByteChannel(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            OutputStream os = Channels.newOutputStream(bc);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            PrintStream ps = new PrintStream(bos))
    {
      ps.println("hello world.");
      ps.flush();
      long prev = bc.position();
      System.out.println(bc.position());
      ps.println("Write some more.");
      ps.flush();
      System.out.println(bc.position());
      bc.position(prev);
      ps.print("Read ");
    }

  }
}
