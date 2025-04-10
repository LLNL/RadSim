import startJVM
import jpype
import jpype.imports
import numpy as np
import matplotlib.pyplot as plt

from gov.llnl.rtk.physics import Nuclides
from gov.llnl.rtk.physics import SourceImpl
from gov.llnl.rtk.physics import Quantity
from gov.llnl.rtk.physics import EmissionCalculator
from gov.bnl.nndc.ensdf.decay import BNLDecayLibrary
from gov.sandia.gadras.decay import DecayLibrary
from gov.nist.physics.xray import NISTXrayLibrary
from gov.llnl.rtk.physics import DecayCalculator

from java.nio.file import Paths
from java.util import ArrayList

bnllib = BNLDecayLibrary()
bnllib.setXrayLibrary(NISTXrayLibrary.getInstance())
bnllib.loadFile(Paths.get("BNL2023.txt"))
sandialib = DecayLibrary.getInstance()

bnlemcal = EmissionCalculator()
bnlemcal.setDecayLibrary(bnllib)

sandiaemcal = EmissionCalculator()
sandiaemcal.setDecayLibrary(sandialib)

from gov.llnl.rtk.physics import NuclideImpl

import xml
import xml.etree.ElementTree as ET

tree = ET.parse('/Users/hangal1/Downloads/nuclides.xml')
root = tree.getroot()

bnlfail, sandiafail = 0,0

for i,child in enumerate(root):
    if(i<5225):continue
    if('nuclide' in child.tag):
        print(child.attrib['symbol'])
        sourceList = ArrayList()
        nuclide = child.attrib['symbol']
        if(Nuclides.get(nuclide).getDecayConstant() < 1e-12 or Nuclides.get(nuclide).getDecayConstant() > 0.07): continue
        activity = Quantity.of(1,"Ci")
        nuclide = SourceImpl.fromActivity(Nuclides.get(nuclide), activity)
        sourceList.add(nuclide)
        dc_bnl = DecayCalculator()
        dc_bnl.setDecayLibrary(bnllib)
        dc_sandia = DecayCalculator()
        dc_sandia.setDecayLibrary(sandialib)
        try:
            aged_sourceList_bnl = dc_bnl.age(sourceList,100)
        except jpype.JException as ex:
            print(ex.stacktrace())
            #raise ex
            bnlfail+=1
            continue

        try:
            aged_sourceList_sandia = dc_sandia.age(sourceList,100)
        except jpype.JException as ex:
            print(ex.stacktrace())
            #raise ex
            sandiafail+=1
            continue

        plt.clf()
        t=10**np.arange(-10,24,0.1)
        out_bnl = []
        out_sandia = []
        for i in t:
            try:
                result_bnl = dc_bnl.age(sourceList, i)
                out_bnl.append([j.getAtoms() for j in result_bnl])
            except jpype.JException as ex:
                print(ex.stacktrace())
                #raise ex
                break
            try:
                result_sandia = dc_sandia.age(sourceList, i)
                out_sandia.append([j.getAtoms() for j in result_sandia])
            except jpype.JException as ex:
                print(ex.stacktrace())
                #raise ex
                break


        ls = ['-','--','-.',':']

        out_bnl=np.array(out_bnl)
        out_sandia=np.array(out_sandia)
        #print(len(aged_sourceList_bnl),len(aged_sourceList_sandia))
        for i in range(len(aged_sourceList_bnl)):
            if(i<4): lsi = ls[i]
            else: lsi = ls[3]
            plt.plot(t, np.array(out_bnl[:,i]), linestyle = lsi, color = 'blue', linewidth = 3, alpha = 0.7, label='BNL: '+str(aged_sourceList_bnl[i].getNuclide()))
        for i in range(len(aged_sourceList_sandia)):
            if(i<4): lsi = ls[i]
            else: lsi = ls[3]
            sandia_lines, = plt.plot(t, np.array(out_sandia[:,i]), linestyle = lsi, color = 'orange', linewidth = 2, alpha = 0.7, label='Sandia: '+str(aged_sourceList_sandia[i].getNuclide()))
        plt.legend()

        plt.xscale('log')
        plt.yscale('log')
        plt.ylim(1e-10,1e25)
        plt.xlabel('time (s)')
        plt.ylabel('atoms')
        plt.savefig('figures/aging_results/'+child.attrib['symbol']+'.png')

        
print("bnlfail: ",bnlfail, "  sandiafail: ", sandiafail)
#170 nuclides fail for BNL and 45 nuclides fail for Sandia