package gov.llnl.rtk.mcnp;

public class MCNP_Electron extends MCNP_Particle {

    private double upperEnergyLimit = 100.0;
    private boolean productionByPhotons = true;
    private boolean photonProduction = true;

    private boolean useFullBremTabularAngularDistribution = true;
    private boolean useSimpleBremAngularDistribution = false;

    private boolean useSampledValueStragglingMethod = true;
    private boolean useExpectedValueStragglingMethod = false;

    private int numBremPhotonsPerStep = 1;
    private int numXraysProducedPerStep = 1;
    private int numKnockOnElectronsProducedPerStep = 1;
    private int numPhotonInducedSecondaryElectronsPerStep = 1;
    private int numBremPerStep = 0;                             // I don't know how this is different from numBremPhotons

    private boolean useCoulombScatteringModel = true;
    private boolean useTotalElasticCrossSection = false;

    private double fractionalEnergyLossPerStep = 0.917;
    private double electronMethodBoundary = 1e-3;
    private double cerenkovPhotonScaleFactor = 0.0;

    public MCNP_Electron() {
        super("Electron", "e");
        setPhysicsOptions();
    }

    public void setUpperEnergyLimit(double upperEnergyLimit) {
        this.upperEnergyLimit = upperEnergyLimit;
    }

    public void setProductionByPhotons(boolean productionByPhotons) {
        this.productionByPhotons = productionByPhotons;
    }

    public void setPhotonProduction(boolean photonProduction) {
        this.photonProduction = photonProduction;
    }

    public void setUseFullBremTabularAngularDistribution(boolean useFullBremTabularAngularDistribution) {
        this.useFullBremTabularAngularDistribution = useFullBremTabularAngularDistribution;
        this.useSimpleBremAngularDistribution = !useFullBremTabularAngularDistribution;
    }

    public void setUseSimpleBremAngularDistribution(boolean useSimpleBremAngularDistribution) {
        this.useSimpleBremAngularDistribution = useSimpleBremAngularDistribution;
        this.useFullBremTabularAngularDistribution = !useSimpleBremAngularDistribution;
    }

    public void setUseSampledValueStragglingMethod(boolean useSampledValueStragglingMethod) {
        this.useSampledValueStragglingMethod = useSampledValueStragglingMethod;
        this.useExpectedValueStragglingMethod = !useSampledValueStragglingMethod;
    }

    public void setUseExpectedValueStragglingMethod(boolean useExpectedValueStragglingMethod) {
        this.useExpectedValueStragglingMethod = useExpectedValueStragglingMethod;
        this.useSampledValueStragglingMethod = !useExpectedValueStragglingMethod;
    }

    public void setNumBremPhotonsPerStep(int numBremPhotonsPerStep) {
        this.numBremPhotonsPerStep = numBremPhotonsPerStep;
    }

    public void setNumXraysProducedPerStep(int numXraysProducedPerStep) {
        this.numXraysProducedPerStep = numXraysProducedPerStep;
    }

    public void setNumKnockOnElectronsProducedPerStep(int numKnockOnElectronsProducedPerStep) {
        this.numKnockOnElectronsProducedPerStep = numKnockOnElectronsProducedPerStep;
    }

    public void setNumPhotonInducedSecondaryElectronsPerStep(int numPhotonInducedSecondaryElectronsPerStep) {
        this.numPhotonInducedSecondaryElectronsPerStep = numPhotonInducedSecondaryElectronsPerStep;
    }

    public void setNumBremPerStep(int numBremPerStep) {
        this.numBremPerStep = numBremPerStep;
    }

    public void setUseCoulombScatteringModel(boolean useCoulombScatteringModel) {
        this.useCoulombScatteringModel = useCoulombScatteringModel;
    }

    public void setUseTotalElasticCrossSection(boolean useTotalElasticCrossSection) {
        this.useTotalElasticCrossSection = useTotalElasticCrossSection;
    }

    public void setFractionalEnergyLossPerStep(double fractionalEnergyLossPerStep) {
        this.fractionalEnergyLossPerStep = fractionalEnergyLossPerStep;
    }

    public void setElectronMethodBoundary(double electronMethodBoundary) {
        this.electronMethodBoundary = electronMethodBoundary;
    }

    public void setCerenkovPhotonScaleFactor(double cerenkovPhotonScaleFactor) {
        this.cerenkovPhotonScaleFactor = cerenkovPhotonScaleFactor;
    }

    private void setPhysicsOptions() {
        setPhysicsOptions(
                upperEnergyLimit,
                MCNP_Utils.valueIfTrueElse(productionByPhotons, "0", "1"),
                MCNP_Utils.valueIfTrueElse(photonProduction, "0", "1"),
                MCNP_Utils.valueIfTrueElse(useFullBremTabularAngularDistribution, "0", "1"),
                MCNP_Utils.valueIfTrueElse(useSampledValueStragglingMethod, "0", "1"),
                numBremPhotonsPerStep,
                numXraysProducedPerStep,
                numKnockOnElectronsProducedPerStep,
                numPhotonInducedSecondaryElectronsPerStep,
                numBremPerStep,
                MCNP_Utils.valueIfTrueElse(useCoulombScatteringModel, "0", "-1"),
                MCNP_Utils.valueIfTrueElse(useTotalElasticCrossSection, "2", "0"),
                "J",
                fractionalEnergyLossPerStep,
                electronMethodBoundary,
                cerenkovPhotonScaleFactor
        );
    }

    public static void main(String ... args) {
        MCNP_Electron electron = new MCNP_Electron();
        System.out.println(electron.getPhysicsCard());
        System.out.println(electron.getCutoffCard());
    }
}
