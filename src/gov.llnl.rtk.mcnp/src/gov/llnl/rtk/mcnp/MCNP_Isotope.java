package gov.llnl.rtk.mcnp;

public class MCNP_Isotope {

    private String name;
    private int z;
    private int a;
    private String crossSectionLibrary;

    public MCNP_Isotope(String name, int z, int a) {
        this.name = name;
        this.z = z;
        this.a = a;
    }

    public void setCrossSectionLibrary(String crossSectionLibrary) {
        this.crossSectionLibrary = crossSectionLibrary;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String string = Integer.toString(1000 * z + a);
        if (crossSectionLibrary != null) {
            string += "." + crossSectionLibrary;
        }
        return string;
    }
}
