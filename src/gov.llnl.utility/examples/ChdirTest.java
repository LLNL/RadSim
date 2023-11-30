
import gov.llnl.utility.PathUtilities;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
public class ChdirTest
{
  static public void main(String[] args)
  {
//    Path file;
//    file = Paths.get("build");
//    System.out.println(Files.exists(file));
//    System.out.println(file.toFile().getAbsolutePath());
    PathUtilities.chdir("test");
    Path file = Paths.get("build");
    System.out.println(Files.exists(file));
    System.out.println(file.toFile().getAbsolutePath());
    System.out.println(file.toAbsolutePath().toFile());
  }
}
