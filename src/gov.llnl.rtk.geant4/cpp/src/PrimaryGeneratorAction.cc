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
// * By ustd::sing,  copying,  modifying or  distributing the software (or *
// * any work based  on the software)  you  agree  to acknowledge its *
// * use  in  resulting  scientific  publications,  and indicate your *
// * acceptance of all terms of the Geant4 Software license.          *
// ********************************************************************
//
//
/// \file PrimaryGeneratorAction.cc
/// \brief Implementation of the PrimaryGeneratorAction class

#include "PrimaryGeneratorAction.hh"
#include "PrimaryGeneratorActionMessenger.hh"
#include "G4RunManager.hh"
#include "G4LogicalVolumeStore.hh"
#include "G4LogicalVolume.hh"
#include "G4Box.hh"
#include "G4Event.hh"
#include "G4ParticleGun.hh"
#include "G4ParticleTable.hh"
#include "G4ParticleDefinition.hh"
#include "G4SystemOfUnits.hh"
#include "Randomize.hh"
#include "G4AnalysisManager.hh"
#include <cmath>
#include <algorithm>
#include <fstream>
#include <sstream>
#include <string>

namespace RadSim
{

G4double SampleEnergyFromPDF();
G4double InverseCDF(G4double rand);

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

PrimaryGeneratorAction::PrimaryGeneratorAction()
{
  G4int nofParticles = 1;
  fParticleGun = new G4ParticleGun(nofParticles);
  fMessenger = new PrimaryGeneratorActionMessenger(this);
  // default particle kinematic
  //
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

PrimaryGeneratorAction::~PrimaryGeneratorAction()
{
  delete fParticleGun;
  delete fMessenger;
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

void PrimaryGeneratorAction::SetBeamParticle(G4String newType) {
  fBeamParticle = newType;
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

void PrimaryGeneratorAction::SetRSphere(G4double newRSphere) {
  fRSphere = newRSphere;
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

void PrimaryGeneratorAction::SetRelI(G4double newRelI) {
  fRelI.push_back(newRelI);
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

void PrimaryGeneratorAction::SetIsSpherical(G4bool isSpherical) {
  fIsSpherical = isSpherical;
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......
void PrimaryGeneratorAction::GeneratePrimaries(G4Event* anEvent)
{
  // This function is called at the begining of event
  //
  G4double PI = std::acos(-1.0);

  auto particleDefinition = G4ParticleTable::GetParticleTable()->FindParticle(fBeamParticle);
  fParticleGun->SetParticleDefinition(particleDefinition);

  // Set gun position
  if (fIsSpherical) {
    G4double rRGunRand = fRSphere * cbrt(G4UniformRand());
    G4double rPhiRand = G4UniformRand()*2.*PI;
    G4double rThetaRand = std::acos(G4UniformRand()*2.0-1.0);
    fParticleGun->SetParticlePosition(G4ThreeVector(rRGunRand*std::sin(rThetaRand)*std::cos(rPhiRand),
                                                    rRGunRand*std::sin(rThetaRand)*std::sin(rPhiRand),
                                                    rRGunRand*std::cos(rThetaRand)));                                           
  } else {
  fParticleGun->SetParticlePosition(G4ThreeVector(0.,0.,0.));
  }

  G4double pThetaRand = std::acos(G4UniformRand()*2.0-1.0);
  G4double pPhiRand;
  pPhiRand = G4UniformRand()*2.*PI;
  fParticleGun->SetParticleMomentumDirection(G4ThreeVector(std::sin(pThetaRand)*std::cos(pPhiRand),
                                                             std::sin(pThetaRand)*std::sin(pPhiRand),
                                                             std::cos(pThetaRand)));                                                      
  G4double energy = SampleEnergyFromPDF();
  fParticleGun->SetParticleEnergy(energy);

  auto analysisManager = G4AnalysisManager::Instance();
  analysisManager->FillH1(0, energy);
  fParticleGun->GeneratePrimaryVertex(anEvent);
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

G4double SampleEnergyFromPDF() {
  G4double rand = G4UniformRand();
  G4double energy = InverseCDF(rand);
  return energy;
}

G4double InverseCDF(G4double rand) {

  std::vector<G4double> x_values;
  std::vector<G4double> f_values;
  std::ifstream file("PdfCDF.csv");
  std::string line;
  while (std::getline(file, line)) {
      std::istringstream iss(line);
      std::string x_str, pdf, f_str;

      if (!(std::getline(iss, x_str, ',') && std::getline(iss, pdf, ',') && std::getline(iss, f_str, ','))) { break; } // error

      x_values.push_back(std::stod(x_str)/1000.);
      f_values.push_back(std::stod(f_str));
  }

  G4double scale_factor = f_values.back();
  for (size_t i = 0; i < f_values.size(); ++i) {
      f_values[i] /= scale_factor;
  }

  // Initialize variables for lin-lininterpolation
  size_t n = x_values.size(); 
  G4double x1, x2, y1, y2; // Points for interpolation
  G4double energy = 0.0;

  for (size_t i = 1; i < n; ++i) {
      if (rand < f_values[i]) {
          x1 = x_values[i - 1];
          x2 = x_values[i];
          y1 = f_values[i - 1];
          y2 = f_values[i];
          // Interpolate to find the inverse CDF value
          energy = x1 + (rand - y1) * (x2 - x1) / (y2 - y1);
          break;
      }
  }
  return energy;
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......


}
