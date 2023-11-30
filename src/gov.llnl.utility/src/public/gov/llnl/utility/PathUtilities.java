/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import gov.llnl.utility.annotation.Matlab;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nelson85
 */
public class PathUtilities
{
  public static String joinStrings(Collection<Path> collection)
  {
    /*
     When we get to Java 8 this will get easier, until then suck it Matlab

     List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
     String commaSeparatedNumbers = numbers.stream()
       .map(i -> i.toString())
       .collect(Collectors.joining(", "));
     */
    String[] out = new String[collection.size()];
    int i = 0;
    for (Path path : collection)
    {
      out[i++] = path.toString();
    }
    return StringUtilities.join(out, ", ");
  }

  public static List<String> getComponents(Path p)
  {
    ArrayList<String> out = new ArrayList<>();
    while (p != null)
    {
      Path base = p.getFileName();
      if (base != null)
        out.add(base.toString());
      p = p.getParent();
    }
    Collections.reverse(out);
    return out;
  }

  /**
   * Search a set of paths for a file.
   *
   * Added to support loading files from python.
   *
   * @param file
   * @param search
   * @return
   * @throws FileNotFoundException
   */
  public static Path resolve(Path file, Path... search) throws FileNotFoundException
  {
    // Check file itself
    if (Files.exists(file))
    {
      return file;
    }

    // Check each search path
    if (search != null)
    {
      for (Path path : search)
      {
        Path candidate = path.resolve(file);
        if (Files.exists(candidate))
          return candidate;
      }
    }

    // File not found
    throw new FileNotFoundException(file.toString());
  }

  /**
   * Check the magic number for a gzip file.
   *
   * @param file
   * @return
   * @throws IOException
   */
  public static boolean isGzip(Path file) throws IOException
  {
    try (InputStream is = Files.newInputStream(file))
    {
      int byte0 = is.read();
      int byte1 = is.read();
      return byte0 == 0x1f && byte1 == 0x8b;
    }
  }

  /**
   * Gets the index of the dot in the file extension
   *
   * @param path
   * @return the index of the dot for the extension or -1 if not found.
   */
  public static final int getFileExtensionPosition(Path path)
  {
    Path file = path.getFileName();
    String name = file.toString();
    // Need to update pattern for cases where the filename has a 
    // two part extension and there are more than one period char in the name.
    Pattern pattern = Pattern.compile("\\.[^.]{1,6}(\\.[^.]{1,4})*$");
    Matcher matcher = pattern.matcher(name);
    int index = 1;
    while (true)
    {
      index = name.indexOf(".", index + 1);
      if (index == -1)
        return -1;
      matcher.region(index, name.length());
      // Works correctly for one or two part extension 
      if (matcher.lookingAt())
      {
        // Check for special case where the file has a two part extension and there's
        // a period char in the filename. 
        // For example: "test.myname.tar.gz"
        long numDotOccur = name.substring(index, name.length()).chars().filter(ch -> ch == '.').count();
        if (numDotOccur >= 3)
        {
          continue;
        }

        return index;
      }
    }
  }

  /**
   * Get the extension for a File.
   *
   * @param path
   * @return the file exception or an empty string.
   */
  public static final String getFileExtension(Path path)
  {
    Path file = path.getFileName();
    int dotIndex = getFileExtensionPosition(file);
    if (dotIndex < 0)
      return "";
    return file.toString().substring(dotIndex + 1);
  }

  /**
   * Change the extension on a file. The new extension should not start with a
   * ".".
   *
   * @param path
   * @param ext is the extension to use or null to remove.
   * @return a path with the requested extension.
   */
  public static Path changeExtension(Path path, String ext)
  {
    Path file = path.getFileName();
    Path parent = path.getParent();
    if (parent == null)
      parent = Paths.get(".");
    int dotIndex = getFileExtensionPosition(file);
    if (dotIndex < 0)
      return path;
    if (ext == null)
      return parent.resolve(file.toString().substring(0, dotIndex)).normalize();
    String base = file.toString().substring(0, dotIndex + 1);
    if (ext.startsWith("."))
      ext = ext.substring(1);
    if (parent == null)
      return Paths.get(base + ext);
    return parent.resolve(base + ext).normalize();
  }

  public static String fileBasename(Path path)
  {
    String out = changeExtension(path, "").getFileName().toString();
    if (out.endsWith("."))
      out = out.substring(0, out.length() - 1);
    return out;
  }

  /**
   * Determine the checksum for a File.
   *
   * @param file
   * @return the checksum of the file.
   * @throws FileNotFoundException if the file is not readable.
   * @throws IOException if the read fails while processing the file.
   */
  public static String md5Checksum(Path file) throws FileNotFoundException, IOException
  {
    if (!Files.isReadable(file))
    {
      throw new FileNotFoundException("%s is unreadable " + file.toString());
    }
    try (InputStream is = Files.newInputStream(file))
    {
      return InputStreamUtilities.md5Checksum(is);
    }
  }

  @Matlab
  public Path newPath(String path)
  {
    return Paths.get(path);
  }

  @Matlab
  public Path newPath(String directory, String filename)
  {
    return Paths.get(directory, filename);
  }

  /**
   * Change the base directory for the virtual machine. This can only be called
   * prior to the first java.nio.file operation. After that the base directory
   * is cached and cannot be changed again.
   *
   * @param dir
   */
  static public void chdir(String dir)
  {
    File file2 = new File(dir);
    System.setProperty("user.dir", file2.getAbsolutePath());

    // Test if the change was successful
    Path file4 = Paths.get(".");
    String path1 = file4.toFile().getAbsolutePath();
    String path2 = file4.toAbsolutePath().toFile().toString();
    if (!path1.equals(path2))
      throw new RuntimeException("unable to change path");

  }

  /*  New API makes this easier
  static public List<File> findIn(Path dir, String pattern) throws IOException
  {
    DirectoryStream<Path> stream = Files.newDirectoryStream(dir, pattern);
    LinkedList<File> out = new LinkedList<>();
    for (Path entry : stream)
    {
      out.add(entry.toFile());
    }
    return out;
  }
   */
  /**
   * Find all of the files in a directory matching the given pattern.
   *
   * @param directory
   * @param pattern
   * @return
   * @throws java.io.IOException
   */
  public static Collection<Path> findPaths(Path directory, String pattern) throws IOException
  {
    return findPaths(directory, pattern, false);
  }

  /**
   * Find all of the files in a directory matching the given pattern.
   *
   * @param directory
   * @param pattern
   * @return
   * @throws java.io.IOException
   */
  public static Collection<Path> findPaths(Path directory, String pattern, boolean recursive) throws IOException
  {
    ArrayList<Path> fileList = new ArrayList<>();

    PathMatcher matcher = FileSystems.getDefault()
            .getPathMatcher("glob:" + pattern);

    try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory))
    {
      for (Path path : directoryStream)
      {
        if (recursive && Files.isDirectory(path))
          fileList.addAll(findPaths(path, pattern, recursive));
        if (matcher.matches(path))
          fileList.add(path);
      }
    }
    return fileList;
  }

  public static Path findFileRecursive(Path directory, Path filename) throws IOException
  {
    try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory))
    {
      Path out = directory.resolve(filename);
      if (Files.exists(out))
        return out;
      for (Path path : directoryStream)
      {
        if (Files.isDirectory(path))
        {
          out = findFileRecursive(path, filename);
          if (out != null)
            return out;
        }
      }
    }
    return null;
  }
}
