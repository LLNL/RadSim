import startJVM
import jpype
import jpype.imports
import numpy as np
import matplotlib.pyplot as plt

from gov.llnl.rtk.physics import Nuclides
from gov.llnl.rtk.physics import SourceImpl
from gov.llnl.rtk.physics import EmissionCalculator
from gov.bnl.nndc.ensdf.decay import BNLDecayLibrary
from gov.llnl.rtk.physics import Quantity
from gov.sandia.gadras.decay import DecayLibrary
from gov.nist.physics.xray import NISTXrayLibrary

from java.nio.file import Paths

from java.util import ArrayList
from gov.llnl.rtk.physics import DecayCalculator


bnllib = BNLDecayLibrary()
bnllib.setXrayLibrary(NISTXrayLibrary.getInstance())
try:
    bnllib.loadFile(Paths.get("BNL2023.txt"))
except jpype.JException as ex:
    print(ex.stacktrace())
    raise ex

print(bnllib.getTransitionsFrom(Nuclides.get("Th234")))
print(bnllib.getTransitionsFrom(Nuclides.get("Pa234")))
print(bnllib.getTransitionsFrom(Nuclides.get("Pa234m")))

from gov.llnl.rtk.physics import DecayCalculator

#add aging
dc = DecayCalculator()
dc.setDecayLibrary(bnllib)

U235 = SourceImpl.fromActivity(Nuclides.get("U235"), Quantity.of(100,"Bq"))
sourceList = ArrayList()
sourceList.add(U235)
aged_sourceList = dc.age(sourceList,10000)

plt.clf()
t=10**np.arange(-10,24,0.1)
out = []
for i in t:
    result = dc.age(sourceList, i)
    out.append([j.getAtoms() for j in result])

ls = ['-','--','-.']

out=np.array(out)
for i in range(len(aged_sourceList)):
    plt.plot(t, np.array(out[:,i]), linewidth = 3, label=str(aged_sourceList[i].getNuclide()))
plt.legend()

plt.xscale('log')
plt.yscale('log')
#plt.ylim(1e-30,1e15)
plt.xlabel('time (s)')
plt.ylabel('atoms')
plt.show()