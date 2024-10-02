import startJVM
import jpype
import jpype.imports
import subprocess
import pandas as pd
import os

from gov.llnl.rtk.flux import FluxTrapezoid, FluxGroupTrapezoid
from java.util import ArrayList

from gov.llnl.rtk.physics import SourceImpl, ActivityUnit, Nuclides, EmissionCalculator
from gov.llnl.ensdf.decay import BNLDecayLibrary
from java.nio.file import Paths

SourceGenerator = jpype.JClass('gov.llnl.rtk.geant4.SourceGenerator')

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

def parse_gamma(gammaList, resolution = 1.0):
    nBins = int(4000. / resolution)
    energies = [i * resolution for i in range(nBins + 1)]
    densities = [0.0] * len(energies)

    for gamma in gammaList:
        energy = gamma.getEnergy().getValue()
        intensity = gamma.getIntensity().getValue()

        lower_index = int(energy // resolution)
        upper_index = lower_index + 1

        if upper_index < len(energies):
            lower_fraction = (upper_index * resolution - energy) / resolution
            upper_fraction = (energy - lower_index * resolution) / resolution

            densities[lower_index] += intensity * lower_fraction
            densities[upper_index] += intensity * upper_fraction

    flux = FluxTrapezoid()
    for i in range(1, len(energies)):
        flux.addPhotonGroup(FluxGroupTrapezoid(energies[i-1], energies[i], densities[i-1], densities[i]))

    return flux

def main():
    generator = SourceGenerator()

    # === Transport gamma begins ===
    Bq = ActivityUnit.Bq
    Cs137 = SourceImpl.fromActivity(Nuclides.get("Cs137"), 100, Bq)
    Ba137m = SourceImpl.fromActivity(Nuclides.get("Ba137m"), 94.7, Bq)
    bnllib = BNLDecayLibrary()
    bnllib.loadFile(Paths.get("BNL2023.txt"))
    emcal = EmissionCalculator()
    emcal.setDecayLibrary(bnllib)
    sourceList = ArrayList()
    sourceList.add(Cs137)
    sourceList.add(Ba137m)
    out = emcal.calculate(sourceList)
    gamma_flux = parse_gamma(out.getGammas(), 1.0)
    generator.setFlux(gamma_flux)
    generator.setSourceParticle("gamma")
    
    gamma_sum = 0
    for gamma in out.getGammas():
        gamma_sum += gamma.getIntensity().getValue()
    beta_sum = 0
    for beta in out.getBetas():
        beta_sum += beta.getIntensity().getValue()

    generator.setNumberOfParticle(int(10000 * gamma_sum/beta_sum))
    generator.setSphericalSource(True)
    generator.setSourceRadius(0.5)
    
    generator.setEnvironment()

    # primitive geometry
    sphere_elements = ArrayList()
    sphere_elements.add("C")
    sphere_elements.add("H")
    sphere_elements.add("Cl")
    sphere_multipliers = ArrayList()
    sphere_multipliers.add(2)   # Add 2 C atoms per molecule
    sphere_multipliers.add(3)   # Add 3 H atoms per molecule
    sphere_multipliers.add(1)   # Add 1 Cl atoms per molecule
    sphere_density = 1.3   # in g/cm3
    sphere_geometeries = ArrayList()
    sphere_geometeries.add(0.) # inner radius in cm
    sphere_geometeries.add(1.) # outer radius in cm
    sphere_geometeries.add(0.) # starting phi angle in degrees
    sphere_geometeries.add(360.) # delta phi angle in degrees
    sphere_geometeries.add(0.) # starting theta angle in degrees
    sphere_geometeries.add(180.) # delta theta angle in degrees
    sphere_geometeries.add(0.) # rotation angle X in degrees
    sphere_geometeries.add(0.) # rotation angle Y in degrees
    sphere_geometeries.add(0.) # rotation angle Z in degrees
    sphere_geometeries.add(0.) # position X in cm
    sphere_geometeries.add(0.) # position Y in cm
    sphere_geometeries.add(0.) # position Z in cm
    generator.environment.addSphericalObject(sphere_elements, sphere_multipliers, sphere_density, sphere_geometeries)

    hemisphere_elements = ArrayList()
    hemisphere_elements.add("Pb")
    hemisphere_multipliers = ArrayList()
    hemisphere_multipliers.add(1)
    hemisphere_density = 11.35   # in g/cm3
    hemisphere_geometeries = ArrayList()
    hemisphere_geometeries.add(1.5) # inner radius in cm
    hemisphere_geometeries.add(2.) # outer radius in cm
    hemisphere_geometeries.add(0.) # starting phi angle in degrees
    hemisphere_geometeries.add(360.) # delta phi angle in degrees
    hemisphere_geometeries.add(0.) # starting theta angle in degrees
    hemisphere_geometeries.add(90.) # delta theta angle in degrees
    hemisphere_geometeries.add(0.) # rotation angle X in degrees
    hemisphere_geometeries.add(0.) # rotation angle Y in degrees
    hemisphere_geometeries.add(0.) # rotation angle Z in degrees
    hemisphere_geometeries.add(0.) # position X in cm
    hemisphere_geometeries.add(0.) # position Y in cm
    hemisphere_geometeries.add(0.) # position Z in cm
    generator.environment.addSphericalObject(hemisphere_elements, hemisphere_multipliers, hemisphere_density, hemisphere_geometeries)

    shell_elements = ArrayList()
    shell_elements.add("Sn")
    shell_multipliers = ArrayList()
    shell_multipliers.add(1)
    shell_density = 7.2389   # in g/cm3
    shell_geometeries = ArrayList()
    shell_geometeries.add(1.5) # inner radius in cm
    shell_geometeries.add(2.) # outer radius in cm
    shell_geometeries.add(2.) # half z in cm
    shell_geometeries.add(0.) # starting phi angle in degrees
    shell_geometeries.add(360.) # delta phi angle in degrees
    shell_geometeries.add(0.) # rotation angle X in degrees
    shell_geometeries.add(0.) # rotation angle Y in degrees
    shell_geometeries.add(0.) # rotation angle Z in degrees
    shell_geometeries.add(0.) # position X in cm
    shell_geometeries.add(0.) # position Y in cm
    shell_geometeries.add(-1.2) # position Z in cm
    generator.environment.addCylindricalObject(shell_elements, shell_multipliers, shell_density, shell_geometeries)

    endcap_elements = ArrayList()
    endcap_elements.add("Al")
    endcap_multipliers = ArrayList()
    endcap_multipliers.add(1)
    endcap_density = 2.7   # in g/cm3
    endcap_geometeries = ArrayList()
    endcap_geometeries.add(0) # inner radius in cm
    endcap_geometeries.add(1.5) # outer radius in cm
    endcap_geometeries.add(0.5) # half z in cm
    endcap_geometeries.add(0.) # starting phi angle in degrees
    endcap_geometeries.add(360.) # delta phi angle in degrees
    endcap_geometeries.add(0.) # rotation angle X in degrees
    endcap_geometeries.add(0.) # rotation angle Y in degrees
    endcap_geometeries.add(0.) # rotation angle Z in degrees
    endcap_geometeries.add(0.) # position X in cm
    endcap_geometeries.add(0.) # position Y in cm
    endcap_geometeries.add(-1.8) # position Z in cm
    generator.environment.addCylindricalObject(endcap_elements, endcap_multipliers, endcap_density, endcap_geometeries)

    support_elements = ArrayList()
    support_elements.add("Pb")
    support_multipliers = ArrayList()
    support_multipliers.add(1)
    support_density = 11.35   # in g/cm3
    support_geometeries = ArrayList()
    support_geometeries.add(0.5) # inner radius at -dz in cm
    support_geometeries.add(1.5) # outer radius at -dz in cm
    support_geometeries.add(0.25) # inner radius at +dz in cm
    support_geometeries.add(0.5) # outer radius at +dz in cm
    support_geometeries.add(0.3) # half z in cm
    support_geometeries.add(0.) # starting phi angle in degrees
    support_geometeries.add(360.) # delta phi angle in degrees
    support_geometeries.add(0.) # rotation angle X in degrees
    support_geometeries.add(0.) # rotation angle Y in degrees
    support_geometeries.add(0.) # rotation angle Z in degrees
    support_geometeries.add(0.) # position X in cm
    support_geometeries.add(0.) # position Y in cm
    support_geometeries.add(-1.2) # position Z in cm
    generator.environment.addConicalObject(support_elements, support_multipliers, support_density, support_geometeries)
    
    # This section defines the beam
    generator.environment.prepareBeamLines()

    # This is the last line to prepare the physics problem
    generator.environment.writeSettingsToMacro()

    # Execute runGEANT4 without argument for geometry check
    runScript_path = './runGEANT4.sh'
    try:
        result = subprocess.run([runScript_path], check=True, shell=True, text=True, capture_output=True)
    except subprocess.CalledProcessError as e:
        print(f"An error occurred: {e}")
        print("Return code:", e.returncode)
        print("Output:\n", e.output)
        print("Error output:\n", e.stderr)

    # launch the program for 10,000 runs
    runScript_path = './runGEANT4.sh gamma'
    try:
        result = subprocess.run([runScript_path], check=True, shell=True, text=True, capture_output=True)
    except subprocess.CalledProcessError as e:
        print(f"An error occurred: {e}")
        print("Return code:", e.returncode)
        print("Output:\n", e.output)
        print("Error output:\n", e.stderr)

    # === Transport gamma ends ===

    #  === Transport beta begins ===
    beta_flux = parse_beta_file('../data/beta-_Cs137_tot.bs')
    generator.setFlux(beta_flux)
    generator.setSourceParticle("e-")
    generator.setNumberOfParticle(10000)
    generator.fetchEnvironment()

    generator.environment.prepareBeamLines()
    generator.environment.writeSettingsToMacro()

    runScript_path = './runGEANT4.sh beta'
    try:
        result = subprocess.run([runScript_path], check=True, shell=True, text=True, capture_output=True)
    except subprocess.CalledProcessError as e:
        print(f"An error occurred: {e}")
        print("Return code:", e.returncode)
        print("Output:\n", e.output)
        print("Error output:\n", e.stderr)
    # === Transport beta ends ===

    def combine_csv(file1, file2):
        with open(file2, 'r') as f2:
            first_7_rows_f2 = [next(f2).strip() for _ in range(7)]
        df1 = pd.read_csv(file1, skiprows=7, header=None, na_values=[''], keep_default_na=False)
        df2 = pd.read_csv(file2, skiprows=7, header=None, na_values=[''], keep_default_na=False)
        df1 = df1.apply(pd.to_numeric, errors='coerce').fillna(0)
        df2 = df2.apply(pd.to_numeric, errors='coerce').fillna(0)
        added_part = df1.values + df2.values

        common_prefix = os.path.commonprefix([file1, file2])
        output_file = common_prefix.rstrip('_') + '.csv'
        with open(output_file, 'w') as f_out:
            for line in first_7_rows_f2:
                f_out.write(line + '\n')
            
            pd.DataFrame(added_part).to_csv(f_out, header=False, index=False)

    combine_csv('RadSim_h1_Ebeta_beta.csv', 'RadSim_h1_Ebeta_gamma.csv')
    combine_csv('RadSim_h1_ElectronEnergy_escape_beta.csv', 'RadSim_h1_ElectronEnergy_escape_gamma.csv')
    combine_csv('RadSim_h1_GammaEnergy_escape_beta.csv', 'RadSim_h1_GammaEnergy_escape_gamma.csv')
    combine_csv('RadSim_h1_GammaEnergy_primary_beta.csv', 'RadSim_h1_GammaEnergy_primary_gamma.csv')
    combine_csv('RadSim_h1_PositronEnergy_escape_beta.csv', 'RadSim_h1_PositronEnergy_escape_gamma.csv')

    # results collection section (put things back into flux form)
    generator.environment.parseResults()

    # gather spectra and plot results
    import matplotlib.pyplot as plt
    def plot_spectrum(spectrum_data, title):
        energies_lower = []
        energies_upper = []
        counts = []
        for groups in spectrum_data.getPhotonGroups():
            energies_lower.append(groups.getEnergyLower())
            energies_upper.append(groups.getEnergyUpper())
            counts.append(groups.getCounts())
        plt.figure()
        plt.bar(energies_lower, counts, width=[upper - lower for lower, upper in zip(energies_lower, energies_upper)], align='edge', alpha=0.7)
        plt.title(title)
        plt.xlabel('Energy (MeV)')
        plt.ylabel('Counts')
        if any(count > 0 for count in counts):
            plt.yscale('log')
        plt.grid()
        plt.show()

    gamma_spectrum = generator.environment.getGammaSpectrum()
    electron_spectrum = generator.environment.getElectronSpectrum()
    positron_spectrum = generator.environment.getPositronSpectrum()

    plot_spectrum(gamma_spectrum, "Gamma Spectrum")
    plot_spectrum(electron_spectrum, "Electron Spectrum")
    plot_spectrum(positron_spectrum, "Positron Spectrum")

if __name__ == '__main__':
    main()