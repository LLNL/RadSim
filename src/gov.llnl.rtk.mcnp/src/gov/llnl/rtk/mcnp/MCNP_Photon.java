package gov.llnl.rtk.mcnp;

public class MCNP_Photon extends MCNP_Particle {

    private double upperEnergyLimit = 100.0;
    boolean generateElectrons = true;
    boolean coherentScattering = true;
    boolean photoNuclearProduction = false;
    boolean biasedPhotoNuclearProduction = false;
    boolean dopplerBroadening = true;
    boolean promptPhotoFissionGammas = false;

    public MCNP_Photon() {
        super("Photon", "p");
        setPhysicsOptions();
    }

    public void setUpperEnergyLimit(double upperEnergyLimit) {
        this.upperEnergyLimit = upperEnergyLimit;
        setPhysicsOptions();
    }

    public void setGenerateElectrons(boolean generateElectrons) {
        this.generateElectrons = generateElectrons;
        setPhysicsOptions();
    }

    public void setCoherentScattering(boolean coherentScattering) {
        this.coherentScattering = coherentScattering;
        setPhysicsOptions();
    }

    public void setPhotoNuclearProduction(boolean photoNuclearProduction) {
        this.photoNuclearProduction = photoNuclearProduction;
        setPhysicsOptions();
    }

    public void setBiasedPhotoNuclearProduction(boolean biasedPhotoNuclearProduction) {
        this.biasedPhotoNuclearProduction = biasedPhotoNuclearProduction;
        setPhysicsOptions();
    }

    public void setDopplerBroadening(boolean dopplerBroadening) {
        this.dopplerBroadening = dopplerBroadening;
        setPhysicsOptions();
    }

    public void setPromptPhotoFissionGammas(boolean promptPhotoFissionGammas) {
        this.promptPhotoFissionGammas = promptPhotoFissionGammas;
        setPhysicsOptions();
    }

    private void setPhysicsOptions() {
        setPhysicsOptions(
                upperEnergyLimit,
                MCNP_Utils.valueIfTrueElse(generateElectrons, "0", "1"),
                MCNP_Utils.valueIfTrueElse(coherentScattering, "0", "1"),
                MCNP_Utils.valueIfTrueElse(photoNuclearProduction, MCNP_Utils.valueIfTrueElse(biasedPhotoNuclearProduction, "1", "-1"), "0"),
                MCNP_Utils.valueIfTrueElse(dopplerBroadening, "0", "1"),
                "J",
                MCNP_Utils.valueIfTrueElse(promptPhotoFissionGammas, "1", "0")
        );
    }

    public static void main(String ... args) {
        MCNP_Photon photon = new MCNP_Photon();
        System.out.println(photon.getPhysicsCard());
    }
}
