import jpype
import start_jvm

import numpy as np
import matplotlib.pyplot as plt
import addcopyfighandler

from java.nio.file import Path
from java.util import ArrayList

from gov.llnl.ensdf.decay import BNLDecayLibrary
from gov.llnl.rtk.data import EnergyScaleFactory
from gov.llnl.rtk.physics import EmissionCalculator, SourceImpl, Nuclides, ActivityUnit
from gov.llnl.rtk.physics import SphericalModel, LayerImpl, MaterialImpl
from gov.llnl.rtk.flux import FluxBinned, FluxGroupBin
from gov.llnl.rtk.flux import FluxTrapezoid, FluxGroupTrapezoid
from gov.llnl.rtk.flux import FluxSpectrum, FluxLineStep

MCNP_DeckBuilder = jpype.JClass('mcnp_api.MCNP_DeckBuilder')
MCNP_Photon = jpype.JClass('mcnp_api.MCNP_Photon')
MCNP_Electron = jpype.JClass('mcnp_api.MCNP_Electron')
MCNP_Job = jpype.JClass('mcnp_api.MCNP_Job')
Vector3Impl = jpype.JClass('gov.llnl.math.euclidean.Vector3Impl')


def parse_beta_file(path):
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
        flux.addPhotonGroup(FluxGroupTrapezoid(energies[i-1], energies[i], densities[i-1], densities[i]))
    return flux


def get_flux_from_lines(isotopes, activities):
    # Load the decay library
    decay_lib = BNLDecayLibrary()
    decay_lib.loadFile(Path.of("../data/BNL2023.txt/"))

    # Set up the emission calculator
    calculator = EmissionCalculator()
    calculator.setDecayLibrary(decay_lib)

    # Add some sources
    sources = ArrayList()
    for isotope, activity in zip(isotopes, activities):
        sources.add(SourceImpl.fromActivity(Nuclides.get(isotope), activity, ActivityUnit.Bq))

    # Calculate the emissions
    emissions = calculator.calculate(sources)

    # Create the flux
    flux = FluxBinned()
    for gamma in emissions.getGammas():
        flux.addPhotonLine(FluxLineStep(gamma.getEnergy().getValue(), gamma.getIntensity().getValue(), 0.0))

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


def run_mcnp(flux, num_particles=int(1e6), particles=(MCNP_Photon()), source_model=None, detector_model=None, detector_origin=None, mcnp_path="mcnp6"):
    generator = MCNP_DeckBuilder()
    generator.setFlux(flux)
    generator.setParticleOptions(num_particles, particles)
    if source_model is not None:
        generator.setSourceModel(source_model)
    if detector_model is not None:
        generator.setDetectorModel(detector_model, detector_origin)

    deck = generator.getDeck()
    print(deck)

    job = MCNP_Job("RadSim_SourceGen_Test", deck, Path.of("test_dir"), mcnp_path)
    tallies = job.run(8).getSpectra()
    return tallies


def plot_spectra(flux, **kwargs):
    groups = flux.getPhotonGroups()
    bins = [groups[0].getEnergyLower()]
    counts = []
    for group in groups:
        bins.append(group.getEnergyUpper())
        counts.append(group.getCounts())
    bins = np.array(bins)
    centers = 0.5 * (bins[0:-1] + bins[1::])
    counts_per_keV = counts / (bins[1::] - bins[0:-1]) / 1000.0
    plt.plot(centers, counts_per_keV, **kwargs)
    plt.ylabel('Counts per keV per source particle')
    plt.xlabel('Energy (keV)')


def main(lead_thickness=0.001, detector_thickness=1.0, detector_distance=10.0, num_source_betas=int(1e5), num_source_gammas=int(1e6)):
    # Define the source geometry
    lead = MaterialImpl()
    lead.setDensity(11.34)
    lead.addElement("Pb206", 0.241, 0.0)            # Todo: use natural abundances
    lead.addElement("Pb207", 0.221, 0.0)
    lead.addElement("Pb208", 0.524, 0.0)

    lead_layer = LayerImpl()
    lead_layer.setThickness(lead_thickness)
    lead_layer.setMaterial(lead)

    source_model = SphericalModel()
    source_model.addLayer(lead_layer)

    # Define the detector geometry          # todo: use formula generator
    nai = MaterialImpl()
    nai.setDensity(3.67)
    nai.addElement("Na23", 1.0, 0.0)
    nai.addElement("I127", 1.0, 0.0)

    nai_layer = LayerImpl()
    nai_layer.setThickness(detector_thickness)
    nai_layer.setMaterial(nai)

    detector_model = SphericalModel()
    detector_model.addLayer(nai_layer)
    detector_origin = Vector3Impl(detector_distance, 0.0, 0.0)

    # Some electron variance reduction
    p = MCNP_Photon()
    e = MCNP_Electron()
    e.setNumBremPhotonsPerStep(1000)
    e.setNumBremPerStep(1000)

    # MCNP Beta Runs (No Detector)
    fluxes = {}
    beta_flux = parse_beta_file('../data/beta-_Cs137_tot.bs')
    results = run_mcnp(beta_flux, num_source_betas, [e, p], None)
    fluxes['beta_source'] = results[1]
    results = run_mcnp(beta_flux, num_source_betas, [e, p], source_model)
    fluxes['gammas_from_betas'] = results[0]

    # MCNP Gamma Runs (No Detector)
    line_flux = get_flux_from_lines(["Cs137", "Ba137m"], [100.0, 94.7])
    results = run_mcnp(line_flux, num_source_gammas, [p], None)
    fluxes['gamma_source'] = results[0]
    results = run_mcnp(line_flux, num_source_gammas, [p], source_model)
    fluxes['attenuated_gammas'] = results[0]

    # MCNP detector transport (No source model)
    total_flux = add_fluxes([fluxes['gammas_from_betas'], fluxes['attenuated_gammas']])
    results = run_mcnp(total_flux, num_source_gammas, [p], None, detector_model, detector_origin)
    fluxes['gammas_at_detector'] = results[1]

    # Do some plots
    plot_spectra(fluxes['beta_source'], color='k')
    plt.title('Cs137 Beta Spectrum')
    plt.ylim([10**-10, 10**-5])
    plt.xlim([0.0, 1500])
    plt.yscale('log')
    plt.legend()
    plt.show()

    plot_spectra(fluxes['gammas_from_betas'], label='Gammas from Betas', color='r')
    plot_spectra(fluxes['attenuated_gammas'], label='Direct Gammas', color='g')
    plot_spectra(total_flux, label='Total', color='k')
    plot_spectra(fluxes['gammas_at_detector'], label='Gammas at Detector', color='k', ls='--')
    plt.title('Cs137 Source Through %.1e mm of Pb' % (10.0 * lead_thickness))
    plt.ylim([10**-14, 10**-2])
    plt.xlim([0.0, 1500])
    plt.yscale('log')
    plt.legend()
    plt.show()


if __name__ == '__main__':
    main()


