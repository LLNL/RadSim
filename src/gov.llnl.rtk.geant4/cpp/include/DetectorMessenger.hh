#ifndef DETECTOR_MESSENGER_H
#define DETECTOR_MESSENGER_H

#include "globals.hh"
#include "G4UImessenger.hh"

class G4UIdirectory;
class G4UIcmdWithAString;
class G4UIcmdWithADoubleAndUnit;
class G4UIcmdWithAnInteger;
class G4UIcmdWithoutParameter;

namespace RadSim
{

class DetectorConstruction;

class DetectorMessenger: public G4UImessenger
{
  public:
    DetectorMessenger(DetectorConstruction* );
    ~DetectorMessenger() override;

    void SetNewValue(G4UIcommand*, G4String) override;

  private:
    DetectorConstruction*  fDetectorConstruction = nullptr;

    G4UIdirectory*         fDirectory = nullptr;
    G4UIdirectory*         fDetDirectory = nullptr;

    G4UIcmdWithAString*         fCompElemCmd = nullptr;
    G4UIcmdWithAnInteger*       fCompElemNumCmd = nullptr;
    G4UIcmdWithADoubleAndUnit*  fCompDensityCmd = nullptr;
    G4UIcmdWithAString*         fGeometryCmd = nullptr;
    G4UIcmdWithADoubleAndUnit*  fGeometryParameterCmd = nullptr;
    G4UIcmdWithoutParameter*    fEndMaterialCmd = nullptr;
};

}

#endif
