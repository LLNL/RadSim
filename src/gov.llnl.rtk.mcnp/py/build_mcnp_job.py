from java.nio.file import Path

from gov.llnl.rtk.mcnp import RadSim_MCNP_Job

def build_mcnp_job(mcnp_path, flux, source_particle, other_particles=(), source_section=None, sections=(), num_particles=int(1e6)):
    job = RadSim_MCNP_Job("RadSim_SourceGen_Test", Path.of("test_dir"), Path.of(mcnp_path))
    job.setEnergyBins(0.0, 3.0, 3001)
    job.setFlux(flux)
    job.setParticleOptions(num_particles, source_particle, other_particles)
    if source_section is not None:
        job.setSourceSection(source_section)
    for section in sections:
        job.addSection(section)
    return job