/*
 * Copyright 2024, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.bnl.nndc.ensdf.decay;

import gov.bnl.nndc.ensdf.EnsdfDataSet;
import gov.bnl.nndc.ensdf.EnsdfParser;
import gov.llnl.rtk.physics.DecayLibrary;
import gov.llnl.rtk.physics.DecayTransition;
import gov.llnl.rtk.physics.Nuclide;
import gov.llnl.rtk.physics.XrayLibrary;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author nelson85 + cheung27
 */
public class BNLDecayLibrary implements DecayLibrary
{

  HashMap<Nuclide, List<Long>> transitionFromIndexMap = new HashMap<>();
  HashMap<Nuclide, List<Long>> transitionToIndexMap = new HashMap<>();
  Path indexFile;
  Path decayTransitionsFile;
  XrayLibrary xrayLibrary;

  /**
   * Set the source for xray data.
   *
   * @param library
   */
  public void setXrayLibrary(XrayLibrary library)
  {
    this.xrayLibrary = library;
  }

  @Override
  public List<DecayTransition> getTransitionsFrom(Nuclide nuclide)
  {
    List<Long> indices = transitionFromIndexMap.get(nuclide);
    if (indices == null)
    {
      return Collections.emptyList();
    }
    List<DecayTransitionImpl> shortlistedDTs = loadDTsFromFile(indices);

    List<DecayTransition> transitionFrom = new ArrayList<>();
    for (DecayTransition dt : shortlistedDTs)
    {
      if (dt.getParent().equals(nuclide))
      {
        transitionFrom.add(dt);
      }
    }
    return transitionFrom;
  }

  @Override
  public List<DecayTransition> getTransitionsTo(Nuclide nuclide)
  {
    // Look up the records for this nuclide
    List<Long> indices = transitionToIndexMap.get(nuclide);
    if (indices == null)
    {
      return Collections.emptyList();
    }
    
    // Retrieve the decay transition records for this nuclide
    List<DecayTransitionImpl> shortlistedDTs = loadDTsFromFile(indices);

    // Add the outgoing transitions
    List<DecayTransition> transitionTo = new ArrayList<>();
    for (DecayTransition dt : shortlistedDTs)
    {
      if (dt.getParent().equals(nuclide))
      {
        transitionTo.add(dt);
      }
    }
    return transitionTo;
  }

  /**
   * Load a decay file.
   *
   * This will create an index file for easy access the first time the BNL file
   * is loaded.
   *
   * @param fileName
   * @throws IOException
   */
  public void loadFile(Path fileName) throws IOException
  {
    setFileNames(fileName);

    if (Files.exists(this.indexFile) && Files.exists(this.decayTransitionsFile))
    {
      loadIndexFromFile();
      return;
    }
    
    if (this.xrayLibrary == null)
    {
      throw new RuntimeException("Xray library is not set");
    }

    // Recreate the index files
    Files.deleteIfExists(this.indexFile);
    Files.deleteIfExists(this.decayTransitionsFile);
    List<EnsdfDataSet> records = EnsdfParser.parseFile(fileName);
    ArrayList<DecayTransitionImpl> dts = new ArrayList<>();

    // Convert records
    for (EnsdfDataSet ds : records)
    {
      // To properly handle isomers we must split the decay chains.
      SplitIsomers si = new SplitIsomers(ds);
      si.setXrayLibrary(this.xrayLibrary);
      
      List<DecayTransitionImpl> result = si.execute();
      for (DecayTransitionImpl de : result)
      {
          if(de.getParent().equals(de.getChild()))
              throw new IllegalStateException("Circular transition "+de.getParent());
      }
      dts.addAll(result);
    }
    
    // Save the parsed records to file
    saveDTsToFile(dts);
  }

//<editor-fold desc="internal" defaultstate="collapsed">
  /**
   * Set the path to the data source.
   *
   * @param fileName
   */
  void setFileNames(Path fileName)
  {
    indexFile = Paths.get(fileName.toString() + ".index_map");
    decayTransitionsFile = Paths.get(fileName.toString() + ".dts_map");
  }

  void saveDTsToFile(List<DecayTransitionImpl> dts)
  {
    try (RandomAccessFile file = new RandomAccessFile(decayTransitionsFile.toFile(), "rw"))
    {
      transitionFromIndexMap.clear();
      transitionToIndexMap.clear();
      long currentPosition = 0;

      for (DecayTransitionImpl dt : dts)
      {
        Nuclide parent = dt.getParent();
        Nuclide child = dt.getChild();

        transitionFromIndexMap.computeIfAbsent(parent, k -> new ArrayList<>()).add(currentPosition);
        transitionToIndexMap.computeIfAbsent(child, k -> new ArrayList<>()).add(currentPosition);

        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream(); ObjectOutputStream objectOut = new ObjectOutputStream(byteOut))
        {

          objectOut.writeObject(dt);
          byte[] data = byteOut.toByteArray();
          file.writeInt(data.length);
          file.write(data);
          currentPosition = file.getFilePointer();
        }
      }
      saveIndexToFile();
    } catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  List<DecayTransitionImpl> loadDTsFromFile(List<Long> indices)
  {
    try (RandomAccessFile file = new RandomAccessFile(decayTransitionsFile.toFile(), "r"))
    {
      List<DecayTransitionImpl> dts = new ArrayList<>();
      for (long index : indices)
      {
        file.seek(index);
        int recordSize = file.readInt();
        byte[] data = new byte[recordSize];
        file.readFully(data);

        try (ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(data)))
        {
          DecayTransitionImpl dt = (DecayTransitionImpl) objectIn.readObject();
          dts.add(dt);
        }
      }
      return dts;
    } catch (IOException | ClassNotFoundException e)
    {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  void saveIndexToFile() throws IOException
  {
    try (OutputStream os = Files.newOutputStream(indexFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            ObjectOutputStream objectOut = new ObjectOutputStream(bos))
    {
      objectOut.writeObject(transitionFromIndexMap);
      objectOut.writeObject(transitionToIndexMap);
    }
  }

  void loadIndexFromFile() throws IOException
  {
    try (InputStream fileIn = Files.newInputStream(indexFile);
         BufferedInputStream bis = new BufferedInputStream(fileIn);
            ObjectInputStream objectIn = new ObjectInputStream(bis))
    {
      transitionFromIndexMap = (HashMap<Nuclide, List<Long>>) objectIn.readObject();
      transitionToIndexMap = (HashMap<Nuclide, List<Long>>) objectIn.readObject();
    } catch (ClassNotFoundException ex)
    {
      throw new RuntimeException(ex);
    } 
  }
//</editor-fold>
}
