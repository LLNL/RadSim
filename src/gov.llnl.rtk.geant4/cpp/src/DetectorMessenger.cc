#include "DetectorMessenger.hh"
#include "DetectorConstruction.hh"

#include "G4UIdirectory.hh"
#include "G4UIcmdWithAString.hh"
#include "G4UIcmdWithADoubleAndUnit.hh"
#include "G4UIcmdWithAnInteger.hh"
#include "G4UIcmdWithoutParameter.hh"

namespace RadSim
{

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

DetectorMessenger::DetectorMessenger(DetectorConstruction* Det)
 : fDetectorConstruction(Det)
{
  fDirectory = new G4UIdirectory("/source/");
  fDirectory->SetGuidance("Source control.");

  fDetDirectory = new G4UIdirectory("/source/mat/");
  fDetDirectory->SetGuidance("Source material control");

  fCompElemCmd = new G4UIcmdWithAString("/source/mat/addElement",this);
  fCompElemCmd->SetGuidance("Add an element to the material.");
  fCompElemCmd->SetParameterName("Element",false);
  fCompElemCmd->AvailableForStates(G4State_PreInit,G4State_Idle);

  fCompElemNumCmd = new G4UIcmdWithAnInteger("/source/mat/addMultiplier",this);
  fCompElemNumCmd->SetGuidance("Specify number of atoms to add.");
  fCompElemNumCmd->SetParameterName("nAtoms",false);
  fCompElemNumCmd->AvailableForStates(G4State_PreInit,G4State_Idle);

  fCompDensityCmd = new G4UIcmdWithADoubleAndUnit("/source/mat/setDensity",this);
  fCompDensityCmd->SetGuidance("Set the density of the composite material.");
  fCompDensityCmd->SetParameterName("density",false);
  fCompDensityCmd->SetDefaultUnit("g/cm3");
  fCompDensityCmd->AvailableForStates(G4State_PreInit,G4State_Idle);

  fGeometryCmd = new G4UIcmdWithAString("/source/mat/setGeometry", this);
  fGeometryCmd->SetGuidance("Set the geometry type.");
  fGeometryCmd->SetParameterName("geometryType", false);
  fGeometryCmd->AvailableForStates(G4State_PreInit, G4State_Idle);  

  fGeometryParameterCmd = new G4UIcmdWithADoubleAndUnit("/source/mat/setGeometryParameter", this);
  fGeometryParameterCmd->SetGuidance("Set the parameters for the geometry.");
  fGeometryParameterCmd->AvailableForStates(G4State_PreInit, G4State_Idle);

  fEndMaterialCmd = new G4UIcmdWithoutParameter("/source/mat/endMaterial", this);
  fEndMaterialCmd->SetGuidance("End the current material construction.");
  fEndMaterialCmd->AvailableForStates(G4State_PreInit,G4State_Idle);
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

DetectorMessenger::~DetectorMessenger()
{
  delete fCompElemCmd;
  delete fCompElemNumCmd;
  delete fCompDensityCmd;
  delete fEndMaterialCmd;
  delete fGeometryCmd;
  delete fGeometryParameterCmd;
  delete fDirectory;
  delete fDetDirectory;
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

void DetectorMessenger::SetNewValue(G4UIcommand* command,G4String newValue)
{
  if( command == fCompElemCmd ) {
    fDetectorConstruction->AddElement(newValue);
  }

  if( command == fCompElemNumCmd ){
    fDetectorConstruction->AddAtoms(fCompElemNumCmd->GetNewIntValue(newValue));
  }

  if( command == fCompDensityCmd ) {
    fDetectorConstruction->SetDensity(fCompDensityCmd->GetNewDoubleValue(newValue));
  }

  if (command == fGeometryCmd) {
    fDetectorConstruction->SetGeometry(newValue);
  }

  if (command == fGeometryParameterCmd) {
    fDetectorConstruction->SetGeometryParameter(fGeometryParameterCmd->GetNewDoubleValue(newValue));
  }  

  if( command == fEndMaterialCmd ) {
    fDetectorConstruction->EndMaterialConstruction();
  }
}

//....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo........oooOO0OOooo......

}
