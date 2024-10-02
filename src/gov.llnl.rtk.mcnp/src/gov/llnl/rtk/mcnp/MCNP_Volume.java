package gov.llnl.rtk.mcnp;

import java.util.ArrayList;
import java.util.List;

public class MCNP_Volume {

    private List<MCNP_Surface> surfaces = new ArrayList<>();
    private List<MCNP_Cell> complementCells = new ArrayList<>();
    private List<Orientation> orientations = new ArrayList<>();

    public enum Orientation {
        POSITIVE, NEGATIVE
    }

    public MCNP_Volume() {
    }

    public MCNP_Volume(MCNP_Surface surface, Orientation orientation) {
        this.surfaces.add(surface);
        this.orientations.add(orientation);
    }

    public void addSurface(MCNP_Surface surface, Orientation orientation) {
        this.surfaces.add(surface);
        this.orientations.add(orientation);
    }

    public void addCellComplement(MCNP_Cell cell) {
        this.complementCells.add(cell);
    }

    public List<MCNP_Surface> getSurfaces() {
        return surfaces;
    }

    @Override
    // Todo: handle strings longer than 80 characters
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String prefix = "";
        for (int i = 0; i < surfaces.size(); i++) {
            builder.append(prefix);
            prefix = " ";
            if (orientations.get(i) == Orientation.NEGATIVE) {
                builder.append("-");
            }
            builder.append(surfaces.get(i).getId());
        }
        for (MCNP_Cell cell : complementCells) {
            builder.append(prefix);
            prefix = " ";
            builder.append("#");
            builder.append(cell.getId());
        }
        return builder.toString();
    }
}
