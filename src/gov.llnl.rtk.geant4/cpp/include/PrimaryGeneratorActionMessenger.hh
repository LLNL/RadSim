#ifndef PRIMARY_GENERATOR_ACTION_MESSENGER_H
#define PRIMARY_GENERATOR_ACTION_MESSENGER_H

#include "globals.hh"
#include "G4UImessenger.hh"
#include "G4UIcmdWithADouble.hh"
#include "G4UIcmdWithAnInteger.hh"
#include "G4UIcmdWithAString.hh"
#include "G4UIcmdWithoutParameter.hh"
#include "G4UIcmdWithADoubleAndUnit.hh"
#include "G4UIcmdWithABool.hh"
#include "G4UIcommand.hh"
#include "G4UIdirectory.hh"
#include "G4UIparameter.hh"


namespace RadSim
{

class PrimaryGeneratorAction;

class PrimaryGeneratorActionMessenger : public G4UImessenger {
public:
    PrimaryGeneratorActionMessenger(PrimaryGeneratorAction* );
    ~PrimaryGeneratorActionMessenger() override;

    void SetNewValue(G4UIcommand*, G4String) override;

private:
    PrimaryGeneratorAction* fPrimaryGeneratorAction = nullptr;

    G4UIdirectory* fDir = nullptr;
    G4UIdirectory* fMyGenDir = nullptr;

    G4UIcmdWithADoubleAndUnit* fsetRSphere = nullptr;
    G4UIcmdWithADouble* fsetRelI = nullptr;
    G4UIcmdWithABool* fisSpherical = nullptr;
    G4UIcmdWithAString* fsetBeamType = nullptr;
};

}

#endif