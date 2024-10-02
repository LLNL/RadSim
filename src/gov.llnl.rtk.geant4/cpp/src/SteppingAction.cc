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
/// \file SteppingAction.cc
/// \brief Implementation of the SteppingAction class

#include "SteppingAction.hh"
#include "EventAction.hh"
#include "DetectorConstruction.hh"

#include "G4Step.hh"
#include "G4RunManager.hh"
#include "G4ParticleDefinition.hh"
#include "G4Track.hh"
#include "G4Gamma.hh"
#include "G4Electron.hh"
#include "G4Positron.hh"
#include "G4AnalysisManager.hh"

using namespace RadSim;

namespace RadSim
{

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

SteppingAction::SteppingAction(const DetectorConstruction* detConstruction,
                               EventAction* eventAction)
  : fDetConstruction(detConstruction),
    fEventAction(eventAction)
{}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

SteppingAction::~SteppingAction()
{}

void SteppingAction::UserSteppingAction(const G4Step* step)
{
    const G4ParticleDefinition* particleDef = step->GetTrack()->GetDefinition();
    auto analysisManager = G4AnalysisManager::Instance();

    if (particleDef == G4Gamma::Gamma()) {
        G4double gammaEnergy = step->GetTrack()->GetKineticEnergy();

        if ( gammaEnergy > 0 ) {
            analysisManager->FillH1(4, gammaEnergy);
            if (step->GetTrack()->GetParentID() != 0) {
                // Kill the secondaries if it has no parent. This will not kill if gamma is from generator.
                step->GetTrack()->SetTrackStatus(fKillTrackAndSecondaries);
            }
        }

    }

    if (step->IsLastStepInVolume() == 1 && particleDef == G4Electron::Electron()) {
        G4cout << "electron found in last step!"<< G4endl;
        analysisManager->FillH1(1, step->GetTrack()->GetKineticEnergy());
    }

    if (step->IsLastStepInVolume() == 1 && particleDef == G4Positron::Positron()) {
        G4cout << "positron found in last step!"<< G4endl;
        analysisManager->FillH1(2, step->GetTrack()->GetKineticEnergy());
    }

    if (step->IsLastStepInVolume() == 1 && particleDef == G4Gamma::Gamma()) {
        G4cout << "gamma found in last step!"<< G4endl;
        analysisManager->FillH1(3, step->GetTrack()->GetKineticEnergy());
    }
}


//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

}
