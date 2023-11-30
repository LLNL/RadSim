/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.llnl.ensdf.decay;

import gov.llnl.ensdf.EnsdfDataSet;
import gov.llnl.ensdf.EnsdfParser;
import gov.llnl.rtk.physics.DecayLibrary;
import gov.llnl.rtk.physics.DecayTransition;
import gov.llnl.rtk.physics.Emission;
import gov.llnl.rtk.physics.Nuclide;
import gov.llnl.rtk.physics.Nuclides;
import gov.nist.xray.NISTLibrary;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author nelson85 + cheung27
 */
public class BNLDecayLibrary implements DecayLibrary
{
  HashMap<Nuclide, List<Long>> transitionFromIndexMap = new HashMap<>();
  HashMap<Nuclide, List<Long>> transitionToIndexMap = new HashMap<>();
  String decayLibraryFileName;
  String indexFileName;
  String decayTransitionsFileName;
  
  void setFileNames(Path fileName) {
    decayLibraryFileName = fileName.toString();
    indexFileName = fileName.toString() + ".index_map";
    decayTransitionsFileName = fileName.toString() + ".dts_map";
  }

  @Override
  public List<DecayTransition> getTransitionsFrom(Nuclide nuclide)
  {
    List<Long> indices = transitionFromIndexMap.get(nuclide);
    if(indices == null) {
      return Collections.emptyList();
    }
    List<DecayTransitionImpl> shortlistedDTs = loadDTsFromFile(indices);
    
    List<DecayTransition> transitionFrom = new ArrayList<>();
    for (DecayTransition dt : shortlistedDTs) {
      if (dt.getParent().equals(nuclide)) {
        transitionFrom.add(dt);
      }
    }
    return transitionFrom;
  }

  @Override
  public List<DecayTransition> getTransitionsTo(Nuclide nuclide)
  {
    List<Long> indices = transitionToIndexMap.get(nuclide);
    if(indices == null) {
      return Collections.emptyList();
    }
    List<DecayTransitionImpl> shortlistedDTs = loadDTsFromFile(indices);
    
    List<DecayTransition> transitionTo = new ArrayList<>();
    for (DecayTransition dt : shortlistedDTs) {
      if (dt.getParent().equals(nuclide)) {
        transitionTo.add(dt);
      }
    }
    return transitionTo;
  }

  public static void main(String[] args) throws IOException, ClassNotFoundException
  {
    BNLDecayLibrary library = new BNLDecayLibrary();
    library.loadFile(Paths.get("data/BNL2023.txt"));
//    library.loadFile(Paths.get("data/Cs-137-BNL.txt"));
    List<DecayTransition> out = new ArrayList<>();
    Nuclide parentNuclide = Nuclides.get("Cs137");
    out = library.getTransitionsFrom(parentNuclide);
    for (DecayTransition dt : out)
    {
      for (Emission e : dt.getEmissions())
      System.out.println(e.toString());
    }
}

  public void loadFile(Path fileName) throws IOException
  {
    setFileNames(fileName);
    File indexFile = new File(indexFileName);
    File dtFile = new File(decayTransitionsFileName);
    
    if (indexFile.exists() && dtFile.exists()) {
      loadIndexFromFile();
    } else {
      indexFile.delete();
      dtFile.delete();
      List<EnsdfDataSet> records = EnsdfParser.parseFile(fileName);
      ArrayList<DecayTransitionImpl> dts = new ArrayList<>();
      // Convert records
      for (EnsdfDataSet ds : records) {
        SplitIsomers si = new SplitIsomers(ds);
        si.setXrayLibrary(NISTLibrary.getInstance());
        for (DecayTransitionImpl dt : si.execute()) {
          dts.add(dt);
        }
      }
      saveDTsToFile(dts);
    }
  }
  
  void saveDTsToFile(List<DecayTransitionImpl> dts) {
    try (RandomAccessFile file = new RandomAccessFile(decayTransitionsFileName, "rw")) {
      
      transitionFromIndexMap.clear();
      transitionToIndexMap.clear();
      long currentPosition = 0;
      
      for (DecayTransitionImpl dt : dts) {
        Nuclide parent = dt.getParent();
        Nuclide child = dt.getChild();
                
        transitionFromIndexMap.computeIfAbsent(parent, k -> new ArrayList<>()).add(currentPosition);
        transitionToIndexMap.computeIfAbsent(child, k -> new ArrayList<>()).add(currentPosition);
        
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ObjectOutputStream objectOut = new ObjectOutputStream(byteOut)) {
          
          objectOut.writeObject(dt);
          byte[] data = byteOut.toByteArray();
          file.writeInt(data.length);
          file.write(data);
          currentPosition = file.getFilePointer();
        }
      }
      saveIndexToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  List<DecayTransitionImpl> loadDTsFromFile(List<Long> indices) {
    try (RandomAccessFile file = new RandomAccessFile(decayTransitionsFileName, "r")) {
      List<DecayTransitionImpl> dts = new ArrayList<>();
      for (long index : indices) {
        file.seek(index);
        int recordSize = file.readInt();
        byte[] data = new byte[recordSize];
        file.readFully(data);
        
        try (ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(data))) {
          DecayTransitionImpl dt = (DecayTransitionImpl) objectIn.readObject();
          dts.add(dt);
        }
      }
        return dts;
      } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }
  
  void saveIndexToFile() {
    try (FileOutputStream fileOut = new FileOutputStream(indexFileName);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
      objectOut.writeObject(transitionFromIndexMap);
      objectOut.writeObject(transitionToIndexMap);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  void loadIndexFromFile() {
    try (FileInputStream fileIn = new FileInputStream(indexFileName);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
      
      transitionFromIndexMap = (HashMap<Nuclide, List<Long>>) objectIn.readObject();
      transitionToIndexMap = (HashMap<Nuclide, List<Long>>) objectIn.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
