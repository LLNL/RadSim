import sys
import jpype
import jpype.imports

jpype.startJVM(classpath=[
  "../../gov.llnl.utility/dist/*",
  "../../gov.llnl.math/dist/*",
  "../../gov.llnl.rtk/dist/*",
  "../../gov.sandia.gadras.decay/dist/*",
])

from gov.llnl.rtk.physics import UnitSystem
from gov.llnl.rtk.physics import MaterialBuilder
from gov.llnl.rtk.physics import DecayCalculator
from gov.llnl.rtk.physics import Nuclides, Elements, Quantity
from gov.sandia.gadras.decay import DecayLibrary

dc = DecayCalculator()
dc.setDecayLibrary(DecayLibrary.getInstance())
builder = MaterialBuilder(UnitSystem.SI)

# Define material properties
builder.age(Quantity.of(20,"a")) # Age 20 years
builder.density(Quantity.of(19.65,"g/cm3"))

# Pu from GADRAS definition
builder.add(Nuclides.get("Pu236"), 1e-8)
builder.add(Nuclides.get("Pu238"), 1.5e-2)
builder.add(Nuclides.get("Pu239"), 95.321)
builder.add(Nuclides.get("Pu240"), 4.501)
builder.add(Nuclides.get("Pu241"), 0.16)
builder.add(Nuclides.get("Pu242"), 3e-3)
material = builder.build()

for p in material:
    print(p)

material2 = dc.age(material)
material2.normalize()

