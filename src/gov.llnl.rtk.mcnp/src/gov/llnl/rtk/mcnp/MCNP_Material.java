package gov.llnl.rtk.mcnp;

import gov.llnl.rtk.physics.Component;
import gov.llnl.rtk.physics.Material;
import gov.llnl.rtk.physics.Nuclide;

import java.util.ArrayList;
import java.util.List;

public class MCNP_Material {

    private static int totalMaterials;

    private String name;
    private int id;
    private List<MCNP_Isotope> isotopes = new ArrayList<>();
    private List<Double> fractions = new ArrayList<>();

    public MCNP_Material(String name) {
        totalMaterials++;
        this.name = name;
        this.id = totalMaterials;
    }

    public MCNP_Material(String name, Material material) {
        this(name);
        for (Component component : material) {
            Nuclide nuclide = component.getNuclide();
            this.addIsotope(new MCNP_Isotope(nuclide.getName(), nuclide.getAtomicNumber(), nuclide.getMassNumber()), -component.getMassFraction());
        }
    }

    public static void resetCount(){
        totalMaterials = 0;
    }

    public void addIsotope(MCNP_Isotope isotope, double fraction) {
        isotopes.add(isotope);
        fractions.add(fraction);
    }

    public int getId() {
        return id;
    }

    public MCNP_Card getCard() {
        MCNP_Card card = new MCNP_Card();
        card.addEntry("m" + id);
        for (int i = 0; i < isotopes.size(); i++) {
            card.addEntry(isotopes.get(i));
            card.addEntry(fractions.get(i));
        }
        card.addComment(name);
        return card;
    }

    public static void main(String ... args) {
        MCNP_Material material = new MCNP_Material("Uranium");
        material.addIsotope(new MCNP_Isotope("U235", 92, 235), 0.007);
        material.addIsotope(new MCNP_Isotope("U238", 92, 238), 0.993);
        System.out.println(material);
    }
}
