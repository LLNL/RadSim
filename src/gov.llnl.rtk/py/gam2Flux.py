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


def readGam(filename):
    if not os.path.exists(filename):
        out = {}
        out['GammaLines'] = []
        out['NeutronScale'] = None
        out['NeutronCounts'] = None
        return out

    ex = re.compile("([^=]+) = (.*)")
    out = {}
    with open(filename, "r") as fd:
        lines = deque(fd.readlines())
    while len(lines) > 0:
        first = lines.popleft()
        m = ex.match(first)
        if m is None:
            raise RuntimeError("Bad line " + first)

        if m.group(1) == "Data":
            break
        else:
            out[m.group(1).strip()] = m.group(2).strip()

    ex = re.compile(r" +(\S+) +(\S+) +(\S+) +!.*")
    first = lines.popleft()
    m = ex.match(first)
    nglines = int(m.group(1))
    nggrp = int(m.group(2))
    nngrp = int(m.group(3))

    # Parse the gamma lines section
    ex = re.compile(r" +(\S+) +(\S+)( +.*)?")
    glines = []
    for i in range(nglines):
        first = lines.popleft()
        m = ex.match(first)
        glines.append((float(m.group(1)), float(m.group(2))))
    out['GammaLines'] = np.array(glines)

    # Parse the gamme groups section
    for i in range(nggrp + 1):
        first = lines.popleft()

    # Pares the neutrons section
    ngroups = []
    nscale = None
    ncounts = None
    ex = re.compile(r" +(\S+) +(\S+)( +!.*)?")
    if nngrp > 0:
        for i in range(nngrp + 1):
            first = lines.popleft()
            m = ex.match(first)
            ngroups.append((float(m.group(1)), float(m.group(2))))
        nscale = [i[0] for i in ngroups]
        nscale.reverse()
        nscale = EnergyScaleFactory.newScale(nscale)
        ncounts = [i[1] for i in ngroups]
        ncounts.reverse()
        ncounts = ncounts[1:]

    out['NeutronScale'] = nscale
    out['NeutronCounts'] = ncounts
    return out


def computeFluxSpectrum(filename, surface):

    # step 1 convert to a spectrum using the null detector
    if surface:
        os.system("%s/bin/Response.exe --detector=detector/null.xml --rebin=detector/rebin.dat --distance=500 --output=out.xml %s" % (SANDIA_TOOLS, filename + ".gam"))
    else:
        os.system("%s/bin/Response.exe --detector=detector/null.xml --rebin=detector/rebin.dat --distance=100 --output=out.xml %s" % (SANDIA_TOOLS, filename + ".gam"))

    # step 2 load the spectrum
    dr = DocumentReader.create(DoubleSpectraList)
    sl = dr.loadFile(Paths.get("out.xml"))
    spec = sl[0]
    sa = (6.7 * 6.7) / (4 * np.pi * 100**2)  # area/(4pi*dist^2)
    if surface:
        sa /= 2

    ges = EnergyScaleFactory.newScale(spec.getEnergyScale().getEdges())
    gcounts = np.array(spec.toDoubles()) / sa / spec.getLiveTime()
    return ges, gcounts


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

    if not os.path.exists("%s/bin/Response.exe" % SANDIA_TOOLS):
        print("Unable to find %s/bin/Response.exe" % SANDIA_TOOLS)
        sys.exit()

    for filename in args.filename:
        if filename.endswith(".gam"):
            filename = filename[:-4]

        # step 1 convert to a spectrum using the null detector
        ges, gcounts = computeFluxSpectrum(filename, args.surface)

        gam = readGam(filename + ".gam")

        # step 4 copy data into Java.
        nes = gam['NeutronScale']
        ncounts = gam['NeutronCounts']
        fs = FluxSpectrum(ges, gcounts, nes, ncounts)

        # step 5 convert to a line/group representation.
        print("Convert")
        ges2 = EnergyScaleFactory.newSqrtScale(10, 6000, 128)
        al = ArrayList([FluxLineStep(i[0], i[1], 0) for i in gam['GammaLines']])
        if args.blind:
            # Extract the flux from the spectrum only without line augmentation
            flux = FluxUtilities.toBinned(fs, ges2)
        else:
            # Extract with line info from GADRAS
            flux = FluxUtilities.toBinned(fs, ges2, al)

        # step 6 save (without compression)
        print("Write to disk")
        bt = FluxEncoding.getInstance().toBytes(flux)
        with java.nio.file.Files.newOutputStream(Paths.get(filename + ".bin")) as oos:
            oos.write(bt)
        flux = FluxEncoding.getInstance().parseBytes(bt)

        if args.plot:
            print("Plot")
            import matplotlib.pyplot as plt
            le = [i.getEnergy() for i in flux.getPhotonLines()]
            li = [i.getIntensity() for i in flux.getPhotonLines()]
            gl = gam['GammaLines']
            plt.plot(ges.getCenters(), gcounts)
            #plt.scatter(gl[:, 0], gl[:, 1], c='g')
            plt.scatter(le, li, c='r', alpha=0.5)

            gc = [i.getDensity() for i in flux.getPhotonGroups()]
            x = np.zeros(256)
            y = np.zeros(256)
            e= np.array(ges2.getEdges())
            x[0] = e[0]
            x[-1] = e[-1]
            x[1:-2:2] = e[1:-1]
            x[2:-1:2] = e[1:-1]
            y[0::2] = gc
            y[1::2] = gc
            plt.plot(x, y/2)
            plt.yscale('log')
            plt.show()

        if args.trap:
            print("Plot trap")
            import matplotlib.pyplot as plt
            le = [i.getEnergy() for i in flux.getPhotonLines()]
            li = [i.getIntensity() for i in flux.getPhotonLines()]
            gl = gam['GammaLines']
            plt.plot(ges.getCenters(), gcounts)
            #plt.scatter(gl[:, 0], gl[:, 1], c='g')
            plt.scatter(le, li, c='r', alpha=0.5)

            flux = FluxUtilities.toTrapezoid(flux)
            n = flux.getPhotonGroups().size()
            x = np.zeros(n*2)
            y = np.zeros(n*2)
            i=0
            for g in flux.getPhotonGroups():
                x[i]=g.getEnergyLower()
                x[i+1]=g.getEnergyUpper()
                y[i]=g.getDensityLower()
                y[i+1]=g.getDensityUpper()
                i+=2
            plt.plot(x, y/2)
            plt.yscale('log')
            plt.show()

  




