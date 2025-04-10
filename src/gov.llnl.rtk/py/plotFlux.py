# Utility script for converting GADRAS gam files into Tarantula flux files.

import numpy as np
import matplotlib.pyplot as plt
import pickle
import sys
import os
import re
import jpype
import jpype.imports
jpype.startJVM(classpath=[
    "../../gov.llnl.utility/dist/*",
    "../../gov.llnl.math/dist/*",
    "../../gov.llnl.rtk/dist/*",
    ])
#import common
#import extractor
#import gov
import java
from collections import deque
from java.nio.file import Paths
from gov.llnl.rtk.data import EnergyScale
from gov.llnl.rtk.data import EnergyScaleFactory
from gov.llnl.rtk.flux import FluxSpectrum
from gov.llnl.rtk.flux import FluxUtilities
from gov.llnl.rtk.flux import FluxEncoding
from gov.llnl.rtk.data import DoubleSpectraList
from gov.llnl.utility.xml.bind import DocumentReader
from gov.llnl.rtk.flux import FluxLineStep
from java.util import ArrayList
import argparse


##############################################################################
if __name__ == "__main__":
    parser = argparse.ArgumentParser(
        description='Convert gam to rtk bin file.')
    parser.add_argument('--surface', action='store_true', help='Convert a surface source', default=False)
    parser.add_argument('--blind', action='store_true', help='Do not use the gadras line information', default=False)
    parser.add_argument('--plot', action='store_true', help='Plot resulting extracted flux', default=False)
    parser.add_argument('--trap', action='store_true', help='Plot resulting extracted flux', default=False)
    parser.add_argument('filename', type=str, nargs="*", help='Gam file to convert')
    args = parser.parse_args()

    #os.environ['SANDIA_TOOLS'] = "C:\\users\\nelson85\\Documents\\devel\\Sandia.Gadras.Tools"
    SANDIA_TOOLS = os.environ['SANDIA_TOOLS']

    for filename in args.filename:
        # step 6 save (without compression)
        print("Read to disk")
        with java.nio.file.Files.newInputStream(Paths.get(filename)) as oos:
            bt = oos.readAllBytes()
        flux = FluxEncoding.getInstance().parseBytes(bt)

        print("Plot")
        import matplotlib.pyplot as plt
        le = [i.getEnergy() for i in flux.getPhotonLines()]
        li = [i.getIntensity() for i in flux.getPhotonLines()]
        #plt.scatter(gl[:, 0], gl[:, 1], c='g')
        h = plt.plot(le, li, '.', alpha=0.5)

        gc = [i.getDensity() for i in flux.getPhotonGroups()]
        e0 = [i.getEnergyLower() for i in flux.getPhotonGroups()]
        e1 = [i.getEnergyLower() for i in flux.getPhotonGroups()]
        x = np.zeros(2*len(e0))
        y = np.zeros(2*len(e0))
        x[0:-1:2] = e0
        x[1::2] = e1
        y[0::2] = gc
        y[1::2] = gc
        plt.plot(x, y/2, color=h[0].get_color(), label=filename)
        plt.yscale('log')

plt.legend()
plt.show()


  




