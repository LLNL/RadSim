#include "PrimaryGeneratorActionMessenger.hh"
#include "PrimaryGeneratorAction.hh"
#include "G4SystemOfUnits.hh"

#include "G4UIdirectory.hh"
#include "G4UIcmdWithAString.hh"
#include "G4UIcmdWithADoubleAndUnit.hh"
#include "G4UIcmdWithADouble.hh"
#include "G4UIcmdWithABool.hh"

namespace RadSim
{

PrimaryGeneratorActionMessenger::PrimaryGeneratorActionMessenger(PrimaryGeneratorAction* GenAct)
: fPrimaryGeneratorAction(GenAct)
{
    fMyGenDir = new G4UIdirectory("/generation/");
    fMyGenDir->SetGuidance("Beta to gamma model");

    fDir = new G4UIdirectory("/generation/beamDef/");
    fDir->SetGuidance("Control commands for defining the beam");

    fsetBeamType = new G4UIcmdWithAString("/generation/beamDef/setBeamType",this);
    fsetBeamType->SetGuidance("Set beam particle type.");
    fsetBeamType->SetParameterName("particle type",false);
    fsetBeamType->AvailableForStates(G4State_PreInit,G4State_Idle);

    fsetRSphere = new G4UIcmdWithADoubleAndUnit("/generation/beamDef/RSphere",this);
    fsetRSphere->SetGuidance("Radius of spherical source");
    fsetRSphere->SetParameterName("radius of source",false);
    fsetRSphere->SetDefaultUnit("cm");
    fsetRSphere->AvailableForStates(G4State_Idle);

    fsetRelI = new G4UIcmdWithADouble("/generation/beamDef/RelI",this);
    fsetRelI->SetGuidance("relative intensity of beta particles");
    fsetRelI->SetParameterName("relative intensity",false);
    fsetRelI->AvailableForStates(G4State_Idle);

    fisSpherical = new G4UIcmdWithABool("/generation/beamDef/isSpherical",this);
    fisSpherical->SetGuidance("Specify if the source should be treated as spherical instead of a point source.");
    fisSpherical->SetParameterName("isBeta",false);
    fisSpherical->AvailableForStates(G4State_Idle);

}

PrimaryGeneratorActionMessenger::~PrimaryGeneratorActionMessenger()
{
    delete fsetBeamType;
    delete fsetRSphere;
    delete fMyGenDir;
    delete fDir;
}


void PrimaryGeneratorActionMessenger
::SetNewValue(G4UIcommand* command, G4String newValues)
{
    if (command==fsetBeamType) {
        fPrimaryGeneratorAction->SetBeamParticle(newValues);
    }

    if( command==fsetRSphere ) {
        fPrimaryGeneratorAction->SetRSphere(fsetRSphere->GetNewDoubleValue(newValues));
    }

    if( command==fsetRelI ) {
        fPrimaryGeneratorAction->SetRelI(fsetRelI->GetNewDoubleValue(newValues));
    }

    if( command==fisSpherical ) {
        fPrimaryGeneratorAction->SetIsSpherical(fisSpherical->GetNewBoolValue(newValues));
    }

}



}