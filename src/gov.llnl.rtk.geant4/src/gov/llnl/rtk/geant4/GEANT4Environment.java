/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.llnl.rtk.geant4;

import java.util.ArrayList;
import java.util.List;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import gov.llnl.rtk.flux.*;
import gov.llnl.rtk.physics.Component;
import gov.llnl.rtk.physics.Section;
import gov.llnl.rtk.physics.SphericalSection;
import gov.llnl.rtk.physics.CylindricalSection;
import gov.llnl.rtk.physics.ConicalSection;
import gov.llnl.rtk.physics.Material;

import java.net.URL;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author cheung27
 */
public class GEANT4Environment {

  public String sourceParticle;
  public int numberOfParticles;
  public boolean isSpherical;
  public double sourceRadius;
  private List<Section> shieldingSections = new ArrayList<>();
  public List<double[]> distribution;
  public List<String> defaultLines = new ArrayList<>();
  public List<String> materialLines = new ArrayList<>();
  public List<String> beamLines = new ArrayList<>();
  public List<FluxBinned> results = new ArrayList<>();

  public GEANT4Environment(List<double[]> distribution, String sourceParticle, int numberOfParticles, boolean isSpherical, double sourceRadius, List<Section> shieldingSections) {
    this.distribution = distribution;
    this.sourceParticle = sourceParticle;
    this.numberOfParticles = numberOfParticles;
    this.isSpherical = isSpherical;
    this.sourceRadius = sourceRadius;
    this.shieldingSections = shieldingSections;
    writeDistributionToFile();
    writeDefaultLines();
  }

  public void setSourceParticle(String particleType) {
    this.sourceParticle = particleType;
  }

  public void setNumberOfParticle(int number) {
    this.numberOfParticles = number;
  }
  
  public void setDistribution(List<double[]> distribution) {
    this.distribution = distribution;
  }
  
  public void writeDistributionToFile() {
    String filename = "PdfCDF.csv";
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
      for (double[] pdfCDF : this.distribution) {
        for (int i = 0; i < pdfCDF.length; i++) {
          writer.write(String.valueOf(pdfCDF[i]));
          if (i < pdfCDF.length - 1) {
            writer.write(",");
          }
        }
        writer.newLine();
      }
    } catch (IOException e) {
      System.err.println("Error writing to csv file: " + e.getMessage());
    }
  }

  void writeDefaultLines() {
    this.defaultLines.add("/control/verbose 2");
    this.defaultLines.add("/control/saveHistory");
    this.defaultLines.add("/run/numberOfThreads 8");
    this.defaultLines.add("/run/verbose 2");
    this.defaultLines.add("");
  }

  public void processShieldingSections() {
    for (Section section : this.shieldingSections) {
      if (section instanceof SphericalSection) {
        SphericalSection sphericalSection = (SphericalSection) section;

        Material material = sphericalSection.getMaterial();
        double density = material.getDensity();
        double innerRadius = sphericalSection.getInnerRadius();
        double outerRadius = sphericalSection.getOuterRadius();
        double startPhi = sphericalSection.getPhi1();
        double deltaPhi = sphericalSection.getDeltaPhi();
        double startTheta = sphericalSection.getTheta1();
        double deltaTheta = sphericalSection.getDeltaTheta();
        double rotationX = Math.asin(sphericalSection.getAxis().unitVector().getY());
        double rotationY = Math.atan2(sphericalSection.getAxis().unitVector().getX(), sphericalSection.getAxis().unitVector().getZ());
        double positionX = sphericalSection.getOrigin().getX();
        double positionY = sphericalSection.getOrigin().getY();
        double positionZ = sphericalSection.getOrigin().getZ();

        for (Component component : material) {
          this.materialLines.add("/source/mat/addElement " + component.getNuclide().getName());
          this.materialLines.add("/source/mat/addMultiplier " + String.valueOf(component.getMassFraction()));
        }
        this.materialLines.add("/source/mat/setDensity " + String.valueOf(density) + " g/cm3");
        this.materialLines.add("/source/mat/setGeometry spherical");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(innerRadius) + " cm  // Inner radius");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(outerRadius) + " cm  // Outer radius");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(startPhi) + " deg  // Starting phi angle");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(deltaPhi) + " deg  // Delta phi angle");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(startTheta) + " deg  // Starting theta angle");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(deltaTheta) + " deg  // Delta theta angle");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(rotationY) + " deg  // Rotation angle Y");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(rotationX) + " deg  // Rotation angle X");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(positionX) + " cm  // Position X");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(positionY) + " cm // Position Y");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(positionZ) + " cm  //Position Z");
        this.materialLines.add("/source/mat/endMaterial");
        this.materialLines.add("");
      } else if (section instanceof CylindricalSection) {
        CylindricalSection cylindricalSection = (CylindricalSection) section;

        Material material = cylindricalSection.getMaterial();
        double density = material.getDensity();
        double innerRadius = cylindricalSection.getInnerRadius();
        double outerRadius = cylindricalSection.getOuterRadius();
        double halfZ = cylindricalSection.getDepth()/2.;
        double startPhi = cylindricalSection.getPhi1();
        double deltaPhi = cylindricalSection.getDeltaPhi();
        double rotationX = Math.asin(cylindricalSection.getAxis().unitVector().getY());
        double rotationY = Math.atan2(cylindricalSection.getAxis().unitVector().getX(), cylindricalSection.getAxis().unitVector().getZ());
        double positionX = cylindricalSection.getOrigin().getX();
        double positionY = cylindricalSection.getOrigin().getY();
        double positionZ = cylindricalSection.getOrigin().getZ();

        for (Component component : material) {
          this.materialLines.add("/source/mat/addElement " + component.getNuclide().getName());
          this.materialLines.add("/source/mat/addMultiplier " + String.valueOf(component.getMassFraction()));
        }
        this.materialLines.add("/source/mat/setDensity " + String.valueOf(density) + " g/cm3");
        this.materialLines.add("/source/mat/setGeometry cylindrical");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(innerRadius) + " cm  // Inner radius");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(outerRadius) + " cm  // Outer radius");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(halfZ) + " cm  // Half z");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(startPhi) + " deg  // Starting phi angle");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(deltaPhi) + " deg  // Delta phi angle");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(rotationY) + " deg  // Rotation angle Y");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(rotationX) + " deg  // Rotation angle X");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(positionX) + " cm  // Position X");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(positionY) + " cm // Position Y");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(positionZ) + " cm  //Position Z");
        this.materialLines.add("/source/mat/endMaterial");
        this.materialLines.add("");
      } else if (section instanceof ConicalSection) {
        ConicalSection conicalSection = (ConicalSection) section;

        Material material = conicalSection.getMaterial();
        double density = material.getDensity();
        double innerRadiusMinusDz = conicalSection.getInnerRadiusMinusDz();
        double outerRadiusMinusDz = conicalSection.getOuterRadiusMinusDz();
        double innerRadiusPlusDz = conicalSection.getInnerRadiusPlusDz();
        double outerRadiusPlusDz = conicalSection.getOuterRadiusPlusDz();
        double halfZ = ( conicalSection.getStartLength() - conicalSection.getEndLength() ) / 2.0;
        double startPhi = conicalSection.getPhi1();
        double deltaPhi = conicalSection.getDeltaPhi();
        double rotationX = Math.asin(conicalSection.getAxis().unitVector().getY());
        double rotationY = Math.atan2(conicalSection.getAxis().unitVector().getX(), conicalSection.getAxis().unitVector().getZ());
        double positionX = conicalSection.getOrigin().getX();
        double positionY = conicalSection.getOrigin().getY();
        double positionZ = conicalSection.getOrigin().getZ();

        for (Component component : material) {
          this.materialLines.add("/source/mat/addElement " + component.getNuclide().getName());
          this.materialLines.add("/source/mat/addMultiplier " + String.valueOf(component.getMassFraction()));
        }
        this.materialLines.add("/source/mat/setDensity " + String.valueOf(density) + " g/cm3");
        this.materialLines.add("/source/mat/setGeometry conical");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(innerRadiusMinusDz) + " cm  // Inner radius at -dz");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(outerRadiusMinusDz) + " cm  // Outer radius at -dz");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(innerRadiusPlusDz) + " cm  // Inner radius at +dz");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(outerRadiusPlusDz) + " cm  // Outer radius at +dz");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(halfZ) + " cm  // Half z");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(startPhi) + " deg  // Starting phi angle");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(deltaPhi) + " deg  // Delta phi angle");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(rotationY) + " deg  // Rotation angle Y");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(rotationX) + " deg  // Rotation angle X");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(positionX) + " cm  // Position X");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(positionY) + " cm // Position Y");
        this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(positionZ) + " cm  //Position Z");
        this.materialLines.add("/source/mat/endMaterial");
        this.materialLines.add("");
      }
    }
  }

  public void addSphericalObject(ArrayList elements, ArrayList multipliers, double density, ArrayList geometries) {
    if (elements.size() != multipliers.size()) {
      System.err.println("Error creating this type of molecules: Number of elements must match the number of multipliers!");
    }

    if (geometries.size() != 12) {
      System.err.println("Error creating this spherical object: Provide exactly 12 parameters!");
    }

    for (int i = 0; i < elements.size(); i++) {
      this.materialLines.add("/source/mat/addElement " + elements.get(i));
      this.materialLines.add("/source/mat/addMultiplier " + multipliers.get(i));
    }
    this.materialLines.add("/source/mat/setDensity " + String.valueOf(density) + " g/cm3");
    this.materialLines.add("/source/mat/setGeometry spherical");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(0)) + " cm  // Inner radius");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(1)) + " cm  // Outer radius");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(2)) + " deg  // Starting phi angle");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(3)) + " deg  // Delta phi angle");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(4)) + " deg  // Starting theta angle");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(5)) + " deg  // Delta theta angle");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(6)) + " deg  // Rotation angle X");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(7)) + " deg  // Rotation angle Y");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(8)) + " deg  // Rotation angle Z");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(9)) + " cm  // Position X");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(10)) + " cm // Position Y");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(11)) + " cm  //Position Z");
    this.materialLines.add("/source/mat/endMaterial");
    this.materialLines.add("");
  }

  public void addCylindricalObject(ArrayList elements, ArrayList multipliers, double density, ArrayList geometries) {
    if (elements.size() != multipliers.size()) {
      System.err.println("Error creating this type of molecules: Number of elements must match the number of multipliers!");
    }

    if (geometries.size() != 11) {
      System.err.println("Error creating this cylindrical object: Provide exactly 11 parameters!");
    }

    for (int i = 0; i < elements.size(); i++) {
      this.materialLines.add("/source/mat/addElement " + elements.get(i));
      this.materialLines.add("/source/mat/addMultiplier " + multipliers.get(i));
    }
    this.materialLines.add("/source/mat/setDensity " + String.valueOf(density) + " g/cm3");
    this.materialLines.add("/source/mat/setGeometry cylindrical");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(0)) + " cm  // Inner radius");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(1)) + " cm  // Outer radius");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(2)) + " cm  // Half z");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(3)) + " deg  // Starting phi angle");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(4)) + " deg  // Delta phi angle");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(5)) + " deg  // Rotation angle X");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(6)) + " deg  // Rotation angle Y");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(7)) + " deg  // Rotation angle Z");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(8)) + " cm  // Position X");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(9)) + " cm // Position Y");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(10)) + " cm  //Position Z");
    this.materialLines.add("/source/mat/endMaterial");
    this.materialLines.add("");
  }

  public void addConicalObject(ArrayList elements, ArrayList multipliers, double density, ArrayList geometries) {
    if (elements.size() != multipliers.size()) {
      System.err.println("Error creating this type of molecules: Number of elements must match the number of multipliers!");
    }

    if (geometries.size() != 13) {
      System.err.println("Error creating this conical object: Provide exactly 13 parameters!");
    }

    for (int i = 0; i < elements.size(); i++) {
      this.materialLines.add("/source/mat/addElement " + elements.get(i));
      this.materialLines.add("/source/mat/addMultiplier " + multipliers.get(i));
    }
    this.materialLines.add("/source/mat/setDensity " + String.valueOf(density) + " g/cm3");
    this.materialLines.add("/source/mat/setGeometry conical");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(0)) + " cm  // Inner radius at -dz");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(1)) + " cm  // Outer radius at -dz");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(2)) + " cm  // Inner radius at +dz");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(3)) + " cm  // Outer radius at +dz");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(4)) + " cm  // Half z");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(5)) + " deg  // Starting phi angle");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(6)) + " deg  // Delta phi angle");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(7)) + " deg  // Rotation angle X");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(8)) + " deg  // Rotation angle Y");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(9)) + " deg  // Rotation angle Z");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(10)) + " cm  // Position X");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(11)) + " cm // Position Y");
    this.materialLines.add("/source/mat/setGeometryParameter " + String.valueOf(geometries.get(12)) + " cm  //Position Z");
    this.materialLines.add("/source/mat/endMaterial");
    this.materialLines.add("");
  }

  public void prepareBeamLines() {
    this.beamLines.add("/run/initialize");
    this.beamLines.add("/generation/beamDef/setBeamType " + sourceParticle);
    this.beamLines.add("/generation/beamDef/isSpherical " + String.valueOf(isSpherical));
    this.beamLines.add("/generation/beamDef/RSphere " + String.valueOf(sourceRadius) + " cm");
    this.beamLines.add("/run/beamOn " + numberOfParticles);
  }

  public void writeSettingsToMacro() {
    Path cliMacro = Paths.get("rtk.mac");
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(cliMacro.toFile()))) {
      for (String line : this.defaultLines) {
        writer.write(line);
        writer.newLine();
      }

      for (String line : this.materialLines) {
        writer.write(line);
        writer.newLine();
      }

      for (String line : this.beamLines) {
        writer.write(line);
        writer.newLine();
      }
    } catch (IOException e) {
      System.err.println("Error writing macro: " + e.getMessage());
    }

    Path guiMacro = Paths.get("init_vis.mac");
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(guiMacro.toFile()))) {
      for (String line : this.defaultLines) {
        writer.write(line);
        writer.newLine();
      }

      for (String line : this.materialLines) {
        writer.write(line);
        writer.newLine();
      }

      for (int i = 0; i < this.beamLines.size() - 1; i++) { // beam will not turn on (action nof the last line) with GUI macro
        writer.write(this.beamLines.get(i));
        writer.newLine();
      }

      writer.write("/control/execute vis.mac");
    } catch (IOException e) {
      System.err.println("Error writing macro: " + e.getMessage());
    }
  }

  public void launchGEANT4() {
    URL geant4ProgramURL = GEANT4Environment.class.getClassLoader().getResource("gov/llnl/rtk/geant4/resources/RadSimBetaConverter");
    System.out.println(geant4ProgramURL);
    try {
      File exeFile = new File(geant4ProgramURL.getFile());
      exeFile.setExecutable(true);
      Process process = Runtime.getRuntime().exec("./RadSimBetaConverter");
//      Process process = Runtime.getRuntime().exec(exeFile.getPath() + " -m rtk.mac");
      int exitCode = process.waitFor();
      if (exitCode != 0) {
        System.err.println("GEANT4 launch failed with exit code: " + exitCode);
      }
    } catch (IOException | InterruptedException e) {
      System.err.println("Error launching GEANT4 " + e.getMessage());
    }
  }

  public void parseResults() {
    String gammaSpectrumFile = "RadSim_h1_GammaEnergy_escape.csv";
    FluxBinned gammaSpectrum = parseHistogramFromFile(gammaSpectrumFile);
    this.results.add(gammaSpectrum);

    String electronSpectrumFile = "RadSim_h1_ElectronEnergy_escape.csv";
    FluxBinned electronSpectrum = parseHistogramFromFile(electronSpectrumFile);
    this.results.add(electronSpectrum);

    String positronSpectrumFile = "RadSim_h1_PositronEnergy_escape.csv";
    FluxBinned positronSpectrum = parseHistogramFromFile(positronSpectrumFile);
    this.results.add(positronSpectrum);
  }

  public FluxBinned getGammaSpectrum() {
    return this.results.get(0);
  }

  public FluxBinned getElectronSpectrum() {
    return this.results.get(1);
  }

  public FluxBinned getPositronSpectrum() {
    return this.results.get(2);
  }

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    String dataFile = Paths.get("py/RadSim_h1_GammaEnergy_escape.csv").toString();
    FluxBinned gammaSpectrum = parseHistogramFromFile(dataFile);
  }

  private static FluxBinned parseHistogramFromFile(String filename) {
    String line;
    int nBins = 0;
    double Emin = 0.0;
    double Emax = 0.0;

    List<double[]> binContents = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      int rowCount = 0;
      while ((line = br.readLine()) != null) {
        rowCount++; // ignore the first three rows
        if (rowCount == 4) {
          String[] parts = line.split("\\s+");
          nBins = Integer.parseInt(parts[2]);
          Emax = Double.parseDouble(parts[4]);
        } else if (rowCount >= 8 && rowCount < 8 + nBins + 2) { // data starts in row 8 and have nBins + 2 rows
          String[] parts = line.split(",");
          double[] binContent = new double[5];
          for (int i = 0; i < 5; i++) {
            binContent[i] = Double.parseDouble(parts[i].trim());
          }
          binContents.add(binContent);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    FluxBinned spectrum = new FluxBinned();
    for (int i = 1; i < binContents.size() - 1; i++) {
      double lowerEnergyBin = Emin + (Emax - Emin) / nBins * (i - 1);
      double upperEnergyBin = lowerEnergyBin + (Emax - Emin) / nBins;
      double counts = binContents.get(i)[0];
      double uncertainty = Math.sqrt(counts);  // currently unused
      spectrum.addPhotonGroup(new FluxGroupBin(Math.round(1000 * lowerEnergyBin), Math.round(1000 * upperEnergyBin), counts));
    }

    return spectrum;
  }

  void compileGEANT4() {

  }

  void launchVisualization(String filename) {

  }

}
