from java.util import ArrayList
from java.nio.file import Path
import numpy as np

from gov.llnl.rtk.data import EnergyScaleFactory
from gov.llnl.rtk.flux import FluxBinned, FluxGroupBin
from gov.llnl.rtk.flux import FluxSpectrum, FluxLineStep
from gov.llnl.rtk.flux import FluxTrapezoid, FluxGroupTrapezoid
from gov.llnl.rtk.physics.EmissionConverter import getEmissionAsFluxTrapezoid

def get_flux_from_lines(emissionList):
    scale = EnergyScaleFactory.newLinearScale(0., 3e3, 3000)   
    flux = getEmissionAsFluxTrapezoid(emissionList,scale)

    return flux

def parse_beta_file(nuclide, activity):
    path='/usr/workspace/hangal1/RDA/RadSim/proj-radsim/src/gov.llnl.rtk.mcnp/data/beta-_'+nuclide+'_tot.bs'
    with open(path, 'r') as f:
        lines = f.readlines()

    energies = []
    densities = []
    for line in lines:
        entries = line.split()
        try:
            energies.append(float(entries[0]))
            densities.append(float(entries[1]))
        except Exception:
            ...

    flux = FluxTrapezoid()
    for i in range(1, len(energies)):
        flux.addPhotonGroup(FluxGroupTrapezoid(energies[i-1], energies[i], activity*densities[i-1], activity*densities[i]))
    return flux

def add_fluxes(fluxes):
    # Todo: We're assuming they all have the same binning
    # Get the bins
    lower_bins = []
    upper_bins = []
    for group in fluxes[0].getPhotonGroups():
        lower_bins.append(group.getEnergyLower())
        upper_bins.append(group.getEnergyUpper())

    # Sum the counts
    total_counts = []
    for flux in fluxes:
        groups = flux.getPhotonGroups()
        counts = []
        for group in groups:
            counts.append(group.getCounts())
        total_counts.append(counts)
    total_counts = np.array(total_counts).sum(axis=0)

    total_flux = FluxBinned()
    for lower, upper, counts in zip(lower_bins, upper_bins, total_counts):
        total_flux.addPhotonGroup(FluxGroupBin(lower, upper, counts, 0.0))
    return total_flux