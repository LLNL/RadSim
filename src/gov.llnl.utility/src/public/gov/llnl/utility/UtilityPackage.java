/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomUtilities;
import gov.llnl.utility.xml.bind.Schema;
import gov.llnl.utility.xml.bind.SchemaBuilder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Schema resource for the ObjectReaders in the gov.llnl.utility package.
 */
@Schema(namespace = "http://utility.llnl.gov",
        schema = "http://utility.llnl.gov/schema/utility.xsd",
        prefix = "util")
public class UtilityPackage extends PackageResource
{
  public static final UtilityPackage SELF
          = new UtilityPackage();
  public static final Logger LOGGER = SELF.getLogger();
//  Logger.getLogger("util");

  private UtilityPackage()
  {
    // Set up console logging
    Logger root = Logger.getLogger("");
    for (Handler handler : root.getHandlers())
    {
      if (handler instanceof ConsoleHandler)
      {
        // java.util.logging.ConsoleHandler.level = ALL
        handler.setLevel(Level.ALL);
        // This code was removed because it is affecting all of the log messages
        // handler.setFormatter(new LogFormatter());
      }
    }

    // Set up package level logging
    String shouldLog = System.getProperty("utility.log");
    if (shouldLog != null)
    {
      Logger.getLogger("util").setLevel(Level.parse(shouldLog));
    }
  }

  public static UtilityPackage getInstance()
  {
    return SELF;
  }

  public static void main(String[] args) throws ReaderException, FileNotFoundException, IOException, URISyntaxException
  {
    UtilityPackage.getInstance().enableLog(Level.ALL);
    // Compile the schema for this package
    SchemaBuilder sb = new SchemaBuilder();
    sb.setTargetNamespace(SELF);
    sb.include("http://utility.llnl.gov/schema/utility-decl.xsd");
    sb.scanForReaders(Paths.get("src/public"));
    sb.scanForReaders(Paths.get("src/private"));

    // Write it back to the source
    Path file = Paths.get(args[0], args[1]);
    Files.createDirectories(file.getParent());
    UtilityPackage.LOGGER.fine("Write " + file.toAbsolutePath());
    try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(file)))
    {
      DomUtilities.printXml(os, sb.getDocument());
    }
    System.exit(sb.getError());
  }
}
