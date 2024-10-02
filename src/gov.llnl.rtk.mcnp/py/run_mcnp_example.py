import jpype
import jpype.imports

DEVEL = '../jars'
if not jpype.isJVMStarted():
    jpype.startJVM(classpath=[
            "%s/*"%DEVEL
        ]
    )

import jpype
#import start_jvm

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

RadSim_MCNP_Job = jpype.JClass('mcnp_api.RadSim_MCNP_Job')
MCNP_Photon = jpype.JClass('mcnp_api.MCNP_Photon')
MCNP_Electron = jpype.JClass('mcnp_api.MCNP_Electron')

MCNP_Job = jpype.JClass('mcnp_api.MCNP_Job')
Vector3 = jpype.JClass('gov.llnl.math.euclidean.Vector3')
SphericalSection = jpype.JClass('gov.llnl.rtk.physics.SphericalSection')
ConicalSection = jpype.JClass('gov.llnl.rtk.physics.ConicalSection')
CylindricalSection = jpype.JClass('gov.llnl.rtk.physics.CylindricalSection')


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


def get_offset_vector(offset, axis, origin=Vector3.ZERO):
    return Vector3.of(
        offset * axis.getX() / axis.norm() + origin.getX(),
        offset * axis.getY() / axis.norm() + origin.getY(),
        offset * axis.getZ() / axis.norm() + origin.getZ()
    )


def get_source_geometry(axis, thickness=0.01):
    # Define some materials
    lead = MaterialImpl()
    lead.setDensity(11.34)
    lead.addElement("Pb206", 0.241, 0.0)            # Todo: use natural abundances
    lead.addElement("Pb207", 0.221, 0.0)
    lead.addElement("Pb208", 0.524, 0.0)

    cs_oxide = MaterialImpl()
    cs_oxide.addElement('Cs137', 1.0, 0.0)
    cs_oxide.addElement('O16', 2.0, 0.0)

    # Calculate the reverse axis
    reverse_axis = Vector3.of(-axis.getX(), -axis.getY(), -axis.getZ())

    # Define sections
    sphere = SphericalSection.Sphere(Vector3.ZERO, 1.0)
    sphere.setMaterial(cs_oxide)

    cap = SphericalSection(Vector3.ZERO, axis, 0.0, np.pi/2, 1.5, 1.5 + thickness)
    cap.setMaterial(lead)

    container = CylindricalSection(get_offset_vector(-3.0, axis), axis, 1.5, 1.5 + thickness, 3.0)
    container.setMaterial(lead)

    floor = CylindricalSection.Cylinder(get_offset_vector(-2.75, axis), axis, 1.5, 0.5)
    floor.setMaterial(lead)

    holder = ConicalSection(Vector3.ZERO, reverse_axis, np.pi/16, 5*np.pi/32, 1.0, 2.25)
    holder.setMaterial(lead)

    return [sphere, cap, container, floor, holder]


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


def build_radsim_job(flux, source_particle, other_particles=(), source_section=None, sections=(), num_particles=int(1e6)):
    job = RadSim_MCNP_Job("RadSim_SourceGen_Test", Path.of("test_dir"), Path.of('/collab/usr/gapps/mcnp/toss_4_x86_64_ib/bin/mcnp6'))
    #job = RadSim_MCNP_Job("RadSim_SourceGen_Test", Path.of("test_dir"), Path.of('C:\\mcnp620\\MCNP_CODE\\bin\\mcnp6.exe'))
    job.setEnergyBins(0.0, 3.0, 3001)
    job.setFlux(flux)
    job.setParticleOptions(num_particles, source_particle, other_particles)
    if source_section is not None:
        job.setSourceSection(source_section)
    for section in sections:
        job.addSection(section)
    return job


def main(num_source_betas=int(1e3), num_source_gammas=int(1e5), num_tasks=8):
    # Get the source sections
    sections = get_source_geometry(Vector3.AXIS_Z)
    source_section = sections[0]
    shielding_sections = sections[1::]

    # Set some particle options
    p = MCNP_Photon()
    e = MCNP_Electron()
    e.setNumBremPhotonsPerStep(1000)
    e.setNumBremPerStep(1000)

    # Create the source fluxes
    beta_flux = parse_beta_file('../data/beta-_Cs137_tot.bs')
    line_flux = get_flux_from_lines(["Cs137", "Ba137m"], [100.0, 94.7])
    results = {}

    # MCNP Beta Run (No Detector Tally, No Source Geometry)
    job = build_radsim_job(beta_flux, e, [p], num_particles=num_source_betas)
    job.addSurfaceCurrentTally("Source Tally", Vector3.of(0.0, 0.0, 0.0), 5.0)
    job.run(num_tasks)
    results['Beta Source'] = job.getTallySpectrum("Source Tally", e, True)

    # MCNP Beta Run (No Detector Tally)
    job = build_radsim_job(beta_flux, e, [p], source_section=None, sections=shielding_sections, num_particles=num_source_betas)
    job.addSurfaceCurrentTally("Source Tally", Vector3.of(0.0, 0.0, 0.0), 5.0)
    job.run(num_tasks)
    results['Gamma From Betas'] = job.getTallySpectrum("Source Tally", p, True)

    # MCNP Gamma Run (No Detector Tally, No Source Geometry)
    job = build_radsim_job(line_flux, p, num_particles=num_source_gammas)
    job.addSurfaceCurrentTally("Source Tally", Vector3.of(0.0, 0.0, 0.0), 5.0)
    job.run(num_tasks)
    results['Gamma Source'] = job.getTallySpectrum("Source Tally", p, True)

    # MCNP Gamma Run (No Detector Tally)
    job = build_radsim_job(line_flux, p, source_section=None, sections=shielding_sections, num_particles=num_source_gammas)
    job.addSurfaceCurrentTally("Source Tally", Vector3.of(0.0, 0.0, 0.0), 5.0)
    job.run(num_tasks)
    results['Attenuated Gammas'] = job.getTallySpectrum("Source Tally", p, True)

    # MCNP detector transport (No Source Geometry)
    total_flux = add_fluxes([results['Gamma From Betas'], results['Attenuated Gammas']])
    job = build_radsim_job(total_flux, p, num_particles=num_source_gammas)
    job.addSurfaceCurrentTally("Detector Tally", Vector3.of(0.0, 0.0, 10.0), 1.0)
    job.run(num_tasks)
    results['Gammas At Detector'] = job.getTallySpectrum("Detector Tally", p, False)           # Note the false indicates particles entering the sphere

    # Do some plots
    plot_spectra(results['Beta Source'], color='k')
    plt.title('Cs137 Beta Spectrum')
    plt.ylim([10**-10, 10**-5])
    plt.xlim([0.0, 1500])
    plt.yscale('log')
    plt.legend()
    plt.show()

    plot_spectra(results['Gamma Source'], color='k')
    plt.title('Cs137 Gamma Spectrum')
    # plt.ylim([10**-10, 10**-5])
    plt.xlim([0.0, 1500])
    plt.yscale('log')
    plt.legend()
    plt.show()

    plot_spectra(results['Gamma From Betas'], label='Gammas from Betas', color='r')
    plot_spectra(results['Attenuated Gammas'], label='Direct Gammas', color='g')
    plot_spectra(total_flux, label='Total', color='k')
    plot_spectra(results['Gammas At Detector'], label='Gammas at Detector', color='k', ls='--')
    plt.title('Cs137')
    plt.ylim([10**-14, 10**-2])
    plt.xlim([0.0, 1500])
    plt.yscale('log')
    plt.legend()
    plt.show()


if __name__ == '__main__':
    main()


