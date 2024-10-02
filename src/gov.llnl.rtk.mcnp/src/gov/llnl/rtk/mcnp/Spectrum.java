package gov.llnl.rtk.mcnp;

public class Spectrum {

    private double[] upperEnergyBins;
    private double[] values;
    private double[] uncertainties;

    public Spectrum(double[] upperEnergyBins, double[] values, double[] uncertainties) {
        this.upperEnergyBins = upperEnergyBins;
        this.values = values;
        this.uncertainties = uncertainties;
    }

    public double[] getUpperEnergyBins() {
        return upperEnergyBins;
    }

    public double[] getValues() {
        return values;
    }

    public double[] getUncertainties() {
        return uncertainties;
    }
}
