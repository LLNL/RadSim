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

for emission in out.getGammas():
    e_e = emission.getEnergy().getValue()
    e_i = emission.getIntensity().getValue()
    gamma_record['E'].append(e_e)
    gamma_record['I'].append(e_i)
    print("Gamma: ", e_e, e_i)

for emission in out.getXrays():
    e_e = emission.getEnergy().getValue()
    e_i = emission.getIntensity().getValue()
    xray_record['E'].append(e_e)
    xray_record['I'].append(e_i)
    print("Xray: ", e_e, e_i,emission.getName())

e_values = beta_record['E']
i_values = beta_record['I']
plt.vlines(e_values, ymin=0, ymax=i_values, linewidth=1, color='blue', label="Calculated", alpha=0.7)
E = [513.97, 892.13, 1175.63]
I = [94.70, 5.8E-4, 5.30]
plt.vlines(E, ymin=0, ymax=I, linewidth=1, color='red', label="NuDat", alpha=0.7)
plt.xlabel('Energy')
plt.ylabel('Intensity')
plt.yscale('log')
plt.ylim(ymin = 1E-5, ymax = 100)
plt.title('Beta (Endpoint)')
plt.legend()
plt.show()

e_values = gamma_record['E']
i_values = gamma_record['I']
plt.vlines(e_values, ymin=0, ymax=i_values, linewidth=1, color='blue', label="Calculated", alpha=0.7)
E = [283.5, 661.657]
I = [5.8E-4, 85.10]
plt.vlines(E, ymin=0, ymax=I, linewidth=1, color='red', label="NuDat", alpha=0.7)
plt.xlabel('Energy')
plt.ylabel('Intensity')
plt.yscale('log')
plt.ylim(ymin = 1E-5, ymax = 100)
plt.title('Gamma')
plt.legend()
plt.show()

e_values = xray_record['E']
i_values = xray_record['I']
plt.vlines(e_values, ymin=0, ymax=i_values, linewidth=1, color='blue', label="Calculated", alpha=0.7)
E = [4.47, 31.817, 32.194, 36.304, 36.378, 37.255]
I = [0.91, 1.99, 3.64, 0.348, 0.672, 0.213]
plt.vlines(E, ymin=0, ymax=I, linewidth=1, color='red', label="NuDat", alpha=0.7)
plt.xlabel('Energy')
plt.ylabel('Intensity')
plt.yscale('log')
plt.ylim(ymin = 1E-2, ymax = 10)
plt.title('Xray')
plt.legend()
plt.show()
### end of demo section

# import pdb; pdb.set_trace()
