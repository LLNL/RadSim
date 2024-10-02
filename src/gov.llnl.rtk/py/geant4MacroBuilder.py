import startJVM
import jpype
import jpype.imports
import numpy as np
import matplotlib.pyplot as plt

from gov.llnl.rtk.physics import Nuclides
from gov.llnl.rtk.physics import SourceImpl
from gov.llnl.rtk.physics import EmissionCalculator
from gov.llnl.ensdf.decay import BNLDecayLibrary
from gov.llnl.rtk.physics import ActivityUnit

from java.nio.file import Paths
from gov.llnl.ensdf import EnsdfParser
from gov.llnl.ensdf.decay import SplitIsomers
from gov.llnl.ensdf.decay import SandiaFormatter
from gov.nist.xray import NISTLibrary

### beginning of demo section
# This is to test the calculate function with a chosen decay library
Bq = ActivityUnit.Bq
Cs137 = SourceImpl.fromActivity(Nuclides.get("Cs137"), 100, Bq)
Ba137m = SourceImpl.fromActivity(Nuclides.get("Ba137m"), 94.7, Bq)
Co60 = SourceImpl.fromActivity(Nuclides.get("Co60"), 100, Bq)
Na22 = SourceImpl.fromActivity(Nuclides.get("Na22"), 100, Bq)
Ir192 = SourceImpl.fromActivity(Nuclides.get("Ir192"), 100, Bq)
Mo99 = SourceImpl.fromActivity(Nuclides.get("Ir192"), 100, Bq)

# can also add aging of source

bnllib = BNLDecayLibrary()
bnllib.loadFile(Paths.get("BNL2023.txt"))
emcal = EmissionCalculator()
emcal.setDecayLibrary(bnllib)

from java.util import ArrayList
sourceList = ArrayList()
sourceList.add(Cs137)
sourceList.add(Ba137m)
# sourceList.add(Co60)
# sourceList.add(Na22)
# sourceList.add(Ir192)
# sourceList.add(Mo99)

beta_record = {'E':[], 'I': []}
gamma_record = {'E':[], 'I': []}
xray_record = {'E':[], 'I': []}

out = emcal.calculate(sourceList)

for emission in out.getBetas():
    e_e = emission.getEnergy().getValue()
    e_i = emission.getIntensity().getValue()
    beta_record['E'].append(e_e)
    beta_record['I'].append(e_i)
    print("Beta-: ", e_e, e_i, emission.getForbiddenness())

class G4MacroGenerator:
    def __init__(self, filename='rtk.mac'):
        self.filename = filename
        self.file_content = []

    def writeRunSettings(self):
        settings = [
            "/control/verbose 2",
            "/control/saveHistory",
            "/run/numberOfThreads 8",
            "/run/verbose 2"
        ]
        self.file_content.extend(settings)

    def writeMaterial(self, elementList, compositionList, density):
        for element, composition in zip(elementList, compositionList):
            self.file_content.append(f"/source/mat/addElement {element}")
            self.file_content.append(f"/source/mat/addMultiplier {composition}")

        self.file_content.append(f"/source/mat/setDensity {density} g/cm3")

    def writeInitialization(self):
        self.file_content.append("/run/initialize")

    def writeBetaEnergy(self, betaEnergy, relativeIntensity):
        for energy, RI in zip(betaEnergy, relativeIntensity):
            self.file_content.append(f"/generation/beamDef/Emax {energy} keV")
            self.file_content.append(f"/generation/beamDef/RelI {RI}")

    def writeIteration(self, nParticle):
        self.file_content.append(f"/run/beamOn {nParticle}")

    def generateFile(self):
        with open(self.filename, 'w') as file:
            file.write('\n'.join(self.file_content))
        print(f"File '{self.filename}' generated successfully.")

# Example usage:
elementList = ['Cs', 'O']
compositionList = [2, 1]
density = 4.65
betaEnergy = [300, 800, 1200]
relativeIntensity = [5, 5, 90]
nParticle = 100000

rtk_generator = G4MacroGenerator()
rtk_generator.writeRunSettings()
rtk_generator.writeMaterial(elementList, compositionList, density)
rtk_generator.writeInitialization()
rtk_generator.writeBetaEnergy(betaEnergy, relativeIntensity)
rtk_generator.writeIteration(nParticle)
rtk_generator.generateFile()



# import pdb; pdb.set_trace()