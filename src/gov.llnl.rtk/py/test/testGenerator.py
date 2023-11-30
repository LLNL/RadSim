import jvm
import java.nio.file
from gov.llnl.rtk.data import SampleGeneratorBuilder, DoubleSpectraList
from gov.llnl.rtk.gadras.io import SpectraPcfFileStreamReader
from gov.llnl.rtk.gadras.model import GadrasEnergyModel
import matplotlib.pyplot as plt

# Read our samples
spfsr = SpectraPcfFileStreamReader()
spfsr.openFile(jvm.new.Path('ComputedSpectrum.pcf'))
bkg=spfsr.getNext()
src=spfsr.getNext()
spfsr.close()

# Add an energy scale
gem=GadrasEnergyModel(-47.0, 2702.42, -105.0, 170.0, -0.0275)
eb=gem.toBins(512)
bkg.setEnergyBins(eb)
src.setEnergyBins(eb)

# Test with a gain miscalibration
sgb=SampleGeneratorBuilder()
sg=sgb.add(bkg).miscalibrate(0.0, 0.01).create()
plt.figure(1)
plt.clf()
for i in range(0, 50):
    plt.plot(sg.drawDouble().toDoubles())
plt.gca().set_yscale('log', nonposy='clip')
plt.title('miscalibrate(0.0, 0.01)')
plt.show()

# Test with a offset miscalibration
sgb=SampleGeneratorBuilder()
sg=sgb.add(bkg).miscalibrate(5.0, 0.0).create()
plt.figure(2)
plt.clf()
for i in range(0, 50):
    plt.plot(sg.drawDouble().toDoubles())
plt.gca().set_yscale('log', nonposy='clip')
plt.title('miscalibrate(5.0, 0.0)')
plt.show()

# Test of coadding to make sure that this pattern works
sgb=SampleGeneratorBuilder()
sg1=sgb.add(src).create()
sg=sgb.add(bkg).includeWeightedSNR(sg1,10.0).miscalibrate(0.0, 0.01).create()
plt.figure(3)
plt.clf()
for i in range(0, 50):
    plt.plot(sg.drawDouble().toDoubles())
plt.gca().set_yscale('log', nonposy='clip')
plt.title('miscalibrate(5.0, 0.0)')
plt.show()



# Note on miscalibrate.  For most cases miscalibration should be the
# last operation applied to the sample generator so that all of the 
# samples in the spectrum have the same miscalibration
