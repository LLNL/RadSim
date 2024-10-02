//
// ********************************************************************
// * License and Disclaimer                                           *
// *                                                                  *
// * The  Geant4 software  is  copyright of the Copyright Holders  of *
// * the Geant4 Collaboration.  It is provided  under  the terms  and *
// * conditions of the Geant4 Software License,  included in the file *
// * LICENSE and available at  http://cern.ch/geant4/license .  These *
// * include a list of copyright holders.                             *
// *                                                                  *
// * Neither the authors of this software system, nor their employing *
// * institutes,nor the agencies providing financial support for this *
// * work  make  any representation or  warranty, express or implied, *
// * regarding  this  software system or assume any liability for its *
// * use.  Please see the license in the file  LICENSE  and URL above *
// * for the full disclaimer and the limitation of liability.         *
// *                                                                  *
// * This  code  implementation is the result of  the  scientific and *
// * technical work of the GEANT4 collaboration.                      *
// * By using,  copying,  modifying or  distributing the software (or *
// * any work based  on the software)  you  agree  to acknowledge its *
// * use  in  resulting  scientific  publications,  and indicate your *
// * acceptance of all terms of the Geant4 Software license.          *
// ********************************************************************
//
//
/// \file DetectorConstruction.cc
/// \brief Implementation of the DetectorConstruction class

#include "DetectorConstruction.hh"
#include "DetectorMessenger.hh"

#include "G4Element.hh"
#include "G4Material.hh"
#include "G4NistManager.hh"

#include "G4RotationMatrix.hh"
#include "G4Box.hh"
#include "G4Sphere.hh"
#include "G4Tubs.hh"
#include "G4Cons.hh"
#include "G4LogicalVolume.hh"
#include "G4PVPlacement.hh"
#include "G4PVReplica.hh"
#include "G4GlobalMagFieldMessenger.hh"
#include "G4AutoDelete.hh"

#include "G4GeometryManager.hh"
#include "G4PhysicalVolumeStore.hh"
#include "G4LogicalVolumeStore.hh"
#include "G4SolidStore.hh"

#include "G4VisAttributes.hh"
#include "G4Colour.hh"

#include "G4PhysicalConstants.hh"
#include "G4SystemOfUnits.hh"

namespace RadSim
{

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

G4ThreadLocal
G4GlobalMagFieldMessenger* DetectorConstruction::fMagFieldMessenger = nullptr;

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

DetectorConstruction::DetectorConstruction()
{
  fMessenger = new DetectorMessenger(this);
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

DetectorConstruction::~DetectorConstruction()
{
  delete fMessenger;
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

G4VPhysicalVolume* DetectorConstruction::Construct()
{
  // Define materials
  DefineMaterials();

  // Define volumes
  return DefineVolumes();
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

void DetectorConstruction::DefineMaterials()
{
  // Lead material defined using NIST Manager
  // auto nistManager = G4NistManager::Instance();

  // Liquid argon material
  G4double a;  // mass of a mole;
  G4double z;  // z=mean number of protons;
  G4double density;

  // Vacuum
  new G4Material("Galactic", z=1., a=1.01*g/mole,density= universe_mean_density,
                  kStateGas, 2.73*kelvin, 3.e-18*pascal);

  // G4int ncomp = felements.size();

  // if (ncomp == 0) {
  //   // Source (Static, For testing only)
  //   G4Element* elCs = new G4Element("Cesium", "Cs", z=55, a=132.90545*g/mole);
  //   G4Element* elO = new G4Element("Oxygen", "O", z=8, a=15.999*g/mole);
  //   G4int oldncomp;
  //   G4Material* CsO = new G4Material("sourceMaterial", density=4.65*g/cm3, oldncomp=2);
  //   G4int nAtoms;
  //   CsO->AddElement(elCs, nAtoms=2);
  //   CsO->AddElement(elO, nAtoms=1);  
  // } else {
  //   // Source (Customizable)
  //   std::vector<G4Element*> el(ncomp);
  //   for (G4int i = 0; i < ncomp; i++) {
  //       el[i] = nistManager->FindOrBuildElement(felements[i]);
  //   }

  //   G4String materialName = "sourceMaterial";
  //   G4Material* sourceMaterial = new G4Material(materialName, fdensity, ncomp);

  //   for (int i = 0; i < ncomp; i++) {
  //       sourceMaterial->AddElement(el[i], fnAtoms[i]);
  //   }
  // }


  // Print materials
  G4cout << *(G4Material::GetMaterialTable()) << G4endl;
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

void DetectorConstruction::AddElement(G4String newElement) {
  felements.push_back(newElement);
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

void DetectorConstruction::AddAtoms(G4int newAtoms) {
  fnAtoms.push_back(newAtoms);
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

void DetectorConstruction::SetDensity(G4double newDensity) {
  fdensity = newDensity;
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

void DetectorConstruction::SetGeometry(G4String newGeometry) {
  fGeometries.push_back(newGeometry);
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

void DetectorConstruction::SetGeometryParameter(G4double newParameter) {
  fGeometryParameter.push_back(newParameter);
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

void DetectorConstruction::EndMaterialConstruction() {
  std::vector<G4Element*> el(felements.size());
  auto nistManager = G4NistManager::Instance();

  for (size_t i = 0; i < felements.size(); i++) {
      el[i] = nistManager->FindOrBuildElement(felements[i]);
  }

  G4String materialName = "Material" + std::to_string(fMaterialCount);
  G4Material* userMaterial = new G4Material(materialName, fdensity, felements.size());

  for (size_t i = 0; i < felements.size(); i++) {
      userMaterial->AddElement(el[i], fnAtoms[i]);
  }

  fMaterials.push_back(userMaterial);
  fGeometryParameterSet.push_back(fGeometryParameter);

  felements.clear();
  fnAtoms.clear();
  fdensity = 0.0;
  fGeometryParameter.clear();
  ++fMaterialCount;
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

G4VPhysicalVolume* DetectorConstruction::DefineVolumes()
{
  // Geometry parameters
  G4double calorSizeXY  = 10.*m;

  auto worldSizeXY = 2.4 * calorSizeXY;
  auto worldSizeZ  = 2.4 * calorSizeXY;

  // Get materials
  auto defaultMaterial = G4Material::GetMaterial("Galactic");
  // auto sourceMaterial = G4Material::GetMaterial("sourceMaterial");


  // G4cout <<  sourceMaterial << G4endl;
  if ( ! defaultMaterial ) {
    G4ExceptionDescription msg;
    msg << "Cannot retrieve materials already defined.";
    G4Exception("DetectorConstruction::DefineVolumes()",
      "MyCode0001", FatalException, msg);
  }

  //
  // World
  //
  auto worldS
    = new G4Box("World",           // its name
                 worldSizeXY/2, worldSizeXY/2, worldSizeZ/2); // its size

  auto worldLV
    = new G4LogicalVolume(
                 worldS,           // its solid
                 defaultMaterial,  // its material
                 "World");         // its name

  auto worldPV
    = new G4PVPlacement(
                 0,                // no rotation
                 G4ThreeVector(),  // at (0,0,0)
                 worldLV,          // its logical volume
                 "World",          // its name
                 0,                // its mother  volume
                 false,            // no boolean operation
                 0,                // copy number
                 fCheckOverlaps);  // checking overlaps

  //
  // Calorimeter
  //
  // auto calorimeterS
  //   // = new G4Box("Calorimeter",     // its name
  //   //              calorSizeXY/2, calorSizeXY/2, calorSizeXY/2); // its size
  //   = new G4Sphere("Calorimeter",     // its name
  //                0., calorSizeXY, 0., 360*deg, 0., 180*deg); // its size

  // auto calorLV
  //   = new G4LogicalVolume(
  //                calorimeterS,     // its solid
  //                defaultMaterial,  // its material
  //                "Calorimeter");   // its name

  // new G4PVPlacement(
  //                0,                // no rotation
  //                G4ThreeVector(),  // at (0,0,0)
  //                calorLV,          // its logical volume
  //                "Calorimeter",    // its name
  //                worldLV,          // its mother  volume
  //                false,            // no boolean operation
  //                0,                // copy number
  //                fCheckOverlaps);  // checking overlaps

  //
  // Absorber (Old)
  //
  // auto absorberS
  //   // = new G4Box("Abso",            // its name
  //   //              calorSizeXY/2, calorSizeXY/2, calorSizeXY/2); // its size
  //   = new G4Sphere("Abso",            // its name
  //                0., calorSizeXY, 0., 360*deg, 0., 180*deg); // its size

  // auto absorberLV
  //   = new G4LogicalVolume(
  //                absorberS,        // its solid
  //                sourceMaterial, // its material
  //                "Abso");          // its name

  // fAbsorberPV
  //   = new G4PVPlacement(
  //                0,                // no rotation
  //                G4ThreeVector(0., 0., 0.), // its position
  //                absorberLV,       // its logical volume
  //                "Abso",           // its name
  //                worldLV,          // its mother  volume
  //                false,            // no boolean operation
  //                0,                // copy number
  //                fCheckOverlaps);  // checking overlaps



  //
  // Visualization attributes
  //
  worldLV->SetVisAttributes (G4VisAttributes::GetInvisible());

  auto simpleBoxVisAtt= new G4VisAttributes(G4Colour(1.0,1.0,1.0));
  simpleBoxVisAtt->SetVisibility(true);
  worldLV->SetVisAttributes(simpleBoxVisAtt);

  auto SphereBoxVisAtt= new G4VisAttributes(G4Colour(1.0,1.0,-1.0));
  SphereBoxVisAtt->SetVisibility(true);

  auto TubsBoxVisAtt= new G4VisAttributes(G4Colour(1.0,-1.0,1.0));
  TubsBoxVisAtt->SetVisibility(true);

  auto ConsBoxVisAtt= new G4VisAttributes(G4Colour(-1.0,-1.0,1.0));
  ConsBoxVisAtt->SetVisibility(true);

  // User Defined
  size_t count = 0;
  fRSourceSphere = 0.0 * cm;
  for (count = 0; count < fGeometries.size(); ++count) 
  {
    G4cout<<"fGeometries[count]"<<fGeometries[count]<<G4endl;
      if (fGeometries[count] == "spherical") {
          auto sphereS = new G4Sphere(
              "UserObject" + std::to_string(count),             // its name
              fGeometryParameterSet[count][0],                  // inner radius
              fGeometryParameterSet[count][1],                  // outer radius
              fGeometryParameterSet[count][2],                  // starting phi angle
              fGeometryParameterSet[count][3],                  // delta phi angle
              fGeometryParameterSet[count][4],                  // starting theta angle
              fGeometryParameterSet[count][5]                   // delta theta angle
          );
          // Logical volume
          auto sphereLV = new G4LogicalVolume(
              sphereS,                                          // its solid
              fMaterials[count],                                // its material
              "UserObject" + std::to_string(count)              // its name
          );
          sphereLV->SetVisAttributes(SphereBoxVisAtt);

          // Rotation
          G4double rotationAngleX = fGeometryParameterSet[count][6];
          G4double rotationAngleY = fGeometryParameterSet[count][7];
          G4double rotationAngleZ = fGeometryParameterSet[count][8];
          G4RotationMatrix* rotationMatrix = new G4RotationMatrix();
          rotationMatrix->rotateX(rotationAngleX);
          rotationMatrix->rotateY(rotationAngleY);
          rotationMatrix->rotateZ(rotationAngleZ);

          // Position
          G4double posX = fGeometryParameterSet[count][9];
          G4double posY = fGeometryParameterSet[count][10];
          G4double posZ = fGeometryParameterSet[count][11];
          G4ThreeVector position(posX, posY, posZ);

          // Approximate furthest distance from origin
          G4double thisR = sqrt(posX*posX + posY*posY + posZ*posZ + fGeometryParameterSet[count][1]*fGeometryParameterSet[count][1]);
          if (thisR > fRSourceSphere) {
            fRSourceSphere = thisR;
          }

          // Placement
          new G4PVPlacement(
              rotationMatrix,                                   // rotation
              position,                                         // its position
              sphereLV,                                         // its logical volume
              "UserObject" + std::to_string(count),             // its name
              worldLV,                                          // its mother volume
              false,                                            // no boolean operation
              0,                                                // copy number
              fCheckOverlaps                                    // checking overlaps
          );
      }

      if (fGeometries[count] == "cylindrical") {
          auto tubsS = new G4Tubs(
              "UserObject" + std::to_string(count),               // its name
              fGeometryParameterSet[count][0],                    // inner radius
              fGeometryParameterSet[count][1],                    // outer radius
              fGeometryParameterSet[count][2] * 0.5,              // Z half length
              fGeometryParameterSet[count][3],                    // starting phi angle
              fGeometryParameterSet[count][4]                     // delta phi angle
          );

          // Logical volume
          auto tubsLV = new G4LogicalVolume(
              tubsS,                                              // its solid
              fMaterials[count],                                 // its material
              "UserObject" + std::to_string(count)               // its name
          );
          tubsLV->SetVisAttributes(TubsBoxVisAtt);

          // Rotation
          G4double rotationAngleX = fGeometryParameterSet[count][5];
          G4double rotationAngleY = fGeometryParameterSet[count][6];
          G4double rotationAngleZ = fGeometryParameterSet[count][7];
          G4RotationMatrix* rotationMatrix = new G4RotationMatrix();
          rotationMatrix->rotateX(rotationAngleX);
          rotationMatrix->rotateY(rotationAngleY);
          rotationMatrix->rotateZ(rotationAngleZ);

          // Position
          G4double posX = fGeometryParameterSet[count][8];
          G4double posY = fGeometryParameterSet[count][9];
          G4double posZ = fGeometryParameterSet[count][10];
          G4ThreeVector position(posX, posY, posZ);

          // Approximate furthest distance from origin
          G4double thisR = sqrt(posX*posX + posY*posY + posZ*posZ + fGeometryParameterSet[count][1]*fGeometryParameterSet[count][1]
            + fGeometryParameterSet[count][2] * fGeometryParameterSet[count][2] * 0.25);
          if (thisR > fRSourceSphere) {
            fRSourceSphere = thisR;
          }

          // Placement
          new G4PVPlacement(
              rotationMatrix,                                    // rotation
              position,                                          // its position
              tubsLV,                                            // its logical volume
              "UserObject" + std::to_string(count),              // its name
              worldLV,                                           // its mother volume
              false,                                             // no boolean operation
              0,                                                 // copy number
              fCheckOverlaps                                     // checking overlaps
          );
      }

      if (fGeometries[count] == "conical") {
          auto consS = new G4Cons(
              "UserObject" + std::to_string(count),              // its name
              fGeometryParameterSet[count][0],                   // inner radius at -dz
              fGeometryParameterSet[count][1],                   // outer radius at -dz
              fGeometryParameterSet[count][2],                   // inner radius at +dz
              fGeometryParameterSet[count][3],                   // outer radius at +dz
              fGeometryParameterSet[count][4] * 0.5,             // half-length along z
              fGeometryParameterSet[count][5],                   // starting phi angle
              fGeometryParameterSet[count][6]                    // delta phi angle
          );

          // Logical volume
          auto consLV = new G4LogicalVolume(
              consS,                                             // its solid
              fMaterials[count],                                // its material
              "UserObject" + std::to_string(count)              // its name
          );
          consLV->SetVisAttributes(ConsBoxVisAtt);

          // Rotation
          G4double rotationAngleX = fGeometryParameterSet[count][7];
          G4double rotationAngleY = fGeometryParameterSet[count][8];
          G4double rotationAngleZ = fGeometryParameterSet[count][9];
          G4RotationMatrix* rotationMatrix = new G4RotationMatrix();
          rotationMatrix->rotateX(rotationAngleX);
          rotationMatrix->rotateY(rotationAngleY);
          rotationMatrix->rotateZ(rotationAngleZ);

          // Position
          G4double posX = fGeometryParameterSet[count][10];
          G4double posY = fGeometryParameterSet[count][11];
          G4double posZ = fGeometryParameterSet[count][12];
          G4ThreeVector position(posX, posY, posZ);

          // Approximate furthest distance from origin
          G4double thisR = sqrt(posX*posX + posY*posY + posZ*posZ + std::pow( std::max(fGeometryParameterSet[count][1], fGeometryParameterSet[count][3]), 2)
            + fGeometryParameterSet[count][4] * fGeometryParameterSet[count][4] * 0.25);
          if (thisR > fRSourceSphere) {
            fRSourceSphere = thisR;
          }

          // Placement
          new G4PVPlacement(
              rotationMatrix,                                    // rotation
              position,                                          // its position
              consLV,                                            // its logical volume
              "UserObject" + std::to_string(count),             // its name
              worldLV,                                           // its mother volume
              false,                                             // no boolean operation
              0,                                                 // copy number
              fCheckOverlaps                                     // checking overlaps
          );
      }
  }

  // The source region
  // auto sourceRegionS
  //   = new G4Sphere("Source Region",     // its name
  //                0., fRSourceSphere, 0., 360*deg, 0., 180*deg); // its size

  // auto sourceRegionLV
  //   = new G4LogicalVolume(
  //                sourceRegionS,           // its solid
  //                defaultMaterial,  // its material
  //                "SourceRegion");         // its name

  // new G4PVPlacement(
  //                0,                // no rotation
  //                G4ThreeVector(),  // at (0,0,0)
  //                sourceRegionLV,          // its logical volume
  //                "SourceRegion",          // its name
  //                worldLV,                // its mother  volume
  //                false,            // no boolean operation
  //                0,                // copy number
  //                fCheckOverlaps);  // checking overlaps
  // sourceRegionLV->SetVisAttributes(simpleBoxVisAtt);

  //
  // Always return the physical World
  //
  return worldPV;
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

void DetectorConstruction::ConstructSDandField()
{
  // Create global magnetic field messenger.
  // Uniform magnetic field is then created automatically if
  // the field value is not zero.
  G4ThreeVector fieldValue;
  fMagFieldMessenger = new G4GlobalMagFieldMessenger(fieldValue);
  fMagFieldMessenger->SetVerboseLevel(1);

  // Register the field messenger for deleting
  G4AutoDelete::Register(fMagFieldMessenger);
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

}

