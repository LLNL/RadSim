
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
/**
 * Somehow I screwed up the file format for the spectra file. It is not hard to
 * regenerate, but very time consuming. Thus I will patch the existing files for
 * now.
 *
 * @author nelson85
 */
public class FixSpectraList
{
  public static List<Path> findAll(Path root, String pattern) throws IOException
  {
    LinkedList<Path> dirs = new LinkedList<>();
    LinkedList<Path> out = new LinkedList<>();
    dirs.add(root);

    PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
    while (!dirs.isEmpty())
    {
      Path dir = dirs.removeFirst();
      System.out.println("Walk " + dir);
      try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir))
      {
        for (Path file : stream)
        {
          System.out.println("check " + file);
          if (Files.isDirectory(file))
            dirs.add(file);

          if (matcher.matches(file))
          {
            System.out.println("Match");
            out.add(file);
          }
        }
      }
    }
    return out;
  }

  static public void main(String[] args) throws IOException
  {
    Path base = Paths.get("../gov.llnl.rnak.mjx/example");
    List<Path> files = findAll(base, "**/source.xml.gz");
    for (Path file : files)
    {
      System.out.println(file);
    }

    System.setProperty("line.separator", "\n");
    for (Path file : files)
    {
      System.out.println("Fix " + file);
      if (!Files.exists(file))
      {
        throw new RuntimeException("Unable to find file " + file.toAbsolutePath());
      }
      Path outputFile = file.getParent().resolve("tmp");
      try (InputStream is = Files.newInputStream(file);
              GZIPInputStream gis = new GZIPInputStream(is);
              InputStreamReader isr = new InputStreamReader(gis);
              BufferedReader br = new BufferedReader(isr);
              OutputStream os = Files.newOutputStream(outputFile);
              GZIPOutputStream gos = new GZIPOutputStream(os);
              OutputStreamWriter osw = new OutputStreamWriter(gos);
              PrintWriter pw = new PrintWriter(osw))
      {
        while (true)
        {
          String line = br.readLine();
          if (line == null)
            break;
          if ("</spectra>".equals(line))
          {
            pw.println("</spectraList>");
            continue;
          }
          if (line.startsWith("<spectraList") && line.contains("gadras"))
          {
            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
            pw.println("<spectraList xmlns='http://rtk.llnl.gov'\n"
                    + "  xmlns:gadras='http://rtk.llnl.gov/gadras'\n"
                    + "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                    + "  xsi:schemaLocation=\n"
                    + "  \"http://rtk.llnl.gov/gadras http://rtk.llnl.gov/gadras/schema/gadras.xsd\"\n"
                    + "  >");
            continue;
          }
          pw.println(line);
        }
      }

      // Replace
      Files.move(file, file.getParent().resolve("old.tar.gz"));
      Files.move(outputFile, file);
    }
  }
}
