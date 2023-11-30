import jpype
import jpype.imports 
jpype.startJVM()

from gov.llnl.rtk.physics import DoseTable
from gov.llnl.rtk.physics import Nuclides

# first load the DoseTable Singleton 
doseTable = DoseTable.getInstance()
# setup the inputs
nuclide = Nuclides.get("Cs137")
activity = 1 # Ci 
Z = 26 # atomic number of shielding
AD = 10 # areal density of shielding
# get the dose activity conversion factor
d2a = doseTable.getDoseToActivityConversion(nuclide, Z, AD)
# print the results
print("The dose from %s(%4.2f,%4.2f) %6.2f Ci source at 1 m is %f Sv/hr"  % (nuclide.getName(), Z, AD, activity, d2a*activity))

