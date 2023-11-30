/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Utility to simplify the pattern for dumping a core file.
 *
 * To enable core dumps, specify the system property with ".dumpCore" appended
 * java.lang.System.setProperty('gov.llnl.rdak.dumpCore', 'true');
 *
 * <p>
 * Example:
 * <pre>
 * {@code
 *   package bar;
 *
 *   // Make sure the class and all members are serialiable
 *   class Foo implements Serializable
 *   {
 *      // Be sure to set the version UID so that the methods can be
 *      // altered without breaking the core file
 *      public static final long serialVersionUID=-123727878335L;
 *
 *      public void evaluate(Input input)
 *      {
 *        // Place a try block around the entire body of the function
 *        try {
 *          // something may go wrong here
 *          ...
 *        }
 *
 *        // Catch all exceptions that warrent a core or simply Exception
 *        catch (Exception ex)
 *        {
 *          // Create a core dump file condition on a System property.
 *          CoreDump dump = new CoreDump("bar.Foo", true);
 *
 *          // Add each of the required items to produce the problem
 *          dump.add("Foo", this);
 *          dump.add("input", this.input);
 *          dump.add("exception", ex);
 *
 *          // Each of the items will be saved in the core file
 *          dump.write("foo");
 *
 *          // Rethrow the exception to keep the control flow
 *          throw ex;
 *         }
 *       }
 *    }
 * }
 * </pre>
 *
 * @author nelson85
 */
public class CoreDump
{
  final boolean shouldDump;
  Path coreDirectory = null;
  HashMap<String, Object> core;
  private Path result;

  /**
   * Create a core dump controlled by a system property. Defaults to false if
   * the property is not found.
   *
   * @param property is the system property that should be set to true to enable
   * core dumps.
   */
  public CoreDump(String property)
  {
    shouldDump = PropertyUtilities.get(property, false);
    if (shouldDump)
      core = new HashMap<>();
  }

  /**
   * Create a core dump controlled by a system property.
   *
   * @param property is the system property that should be set to true to enable
   * core dumps.
   * @param dumpDefault
   */
  public CoreDump(String property, boolean dumpDefault)
  {
    shouldDump = PropertyUtilities.get(property + ".dumpCore", dumpDefault);
    if (shouldDump)
    {
      core = new HashMap<>();
      coreDirectory = Paths.get(PropertyUtilities.get(property + ".coreDir", "."));
    }
  }

  /**
   * Adds a named object to the core dump. This must be called before the call
   * to {@code write}. If items to added to core after it is written, a warning
   * message will be produced.
   *
   * @param key is the name of object to appear in the core.
   * @param obj is the contents of the object.
   * @return the coredump object for argument chaining.
   */
  public CoreDump add(String key, Object obj)
  {
    if (shouldDump == false)
      return this;

    if (core == null)
      UtilityPackage.LOGGER.severe("Attempt to add object" + key + " to a core file after written");

    core.put(key, obj);
    return this;
  }

  /**
   * Write the core file if core dumps are enabled. This must be the last call
   * to the core dump object.
   *
   * @param corefile
   * @return true if a core was written.
   *
   */
  public boolean write(String corefile)
  {
    if (shouldDump == false)
      return false;

    int id = System.identityHashCode(core);
    Path file = coreDirectory.resolve(String.format("%s-%08x.ser.gz", corefile, id));
    this.result = file;

    Serializer serializer = new Serializer();
    try
    {
      if (!Files.exists(this.coreDirectory))
        Files.createDirectories(this.coreDirectory);
      serializer.save(file, core);
      UtilityPackage.LOGGER.severe("Wrote core dump file " + file);
    }
    catch (IOException ex)
    {
      throw new RuntimeException(ex);
    }
    core = null;
    return true;
  }

  /**
   * @return the result
   */
  public Path getResult()
  {
    return result;
  }

}
