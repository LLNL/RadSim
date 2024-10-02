package gov.llnl.rtk.mcnp;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MCNP_Particle {

    private String name;
    private String id;

    private List<Object> physicsOptions;

    private Double timeCutoff;
    private Double lowerEnergyCutoff;
    private Double weightCutoff1, weightCutoff2;
    private Double minimumSourceWeight;

    public MCNP_Particle(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void setPhysicsOptions(Object ... options) {
        physicsOptions = Arrays.asList(options);
    }

    public String getId() {
        return id;
    }

    public MCNP_Card getPhysicsCard() {
        MCNP_Card card = new MCNP_Card("PHYS:" + id);
        for (Object option : physicsOptions) {
            card.addEntry(option);
        }
        card.addComment(name + " Physics Options");
        return card;
    }

    public void setTimeCutoff(Double timeCutoff) {
        this.timeCutoff = timeCutoff;
    }

    public void setLowerEnergyCutoff(Double lowerEnergyCutoff) {
        this.lowerEnergyCutoff = lowerEnergyCutoff;
    }

    public void setWeightCutoff1(Double weightCutoff1) {
        this.weightCutoff1 = weightCutoff1;
    }

    public void setWeightCutoff2(Double weightCutoff2) {
        this.weightCutoff2 = weightCutoff2;
    }

    public void setMinimumSourceWeight(Double minimumSourceWeight) {
        this.minimumSourceWeight = minimumSourceWeight;
    }

    public MCNP_Card getCutoffCard(){
        MCNP_Card card = new MCNP_Card("CUT:" + id);
        card.addEntry(Objects.requireNonNullElse(timeCutoff, "J"));
        card.addEntry(Objects.requireNonNullElse(lowerEnergyCutoff, "J"));
        card.addEntry(Objects.requireNonNullElse(weightCutoff1, "J"));
        card.addEntry(Objects.requireNonNullElse(weightCutoff2, "J"));
        card.addEntry(Objects.requireNonNullElse(minimumSourceWeight, "J"));
        card.addComment(name + " Cutoff Options");
        return card;
    }
}
