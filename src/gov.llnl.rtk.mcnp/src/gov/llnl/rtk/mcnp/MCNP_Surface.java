package gov.llnl.rtk.mcnp;

import java.util.Arrays;
import java.util.List;

public class MCNP_Surface {

    private static int totalSurfaces;
    private String name;
    private int id;
    private MCNP_Transformation transformation;
    private String typeString;
    private List<Double> parameters;

    public MCNP_Surface(String name, String typeString, Double ... parameters) {
        totalSurfaces++;
        this.name = name;
        this.id = totalSurfaces;
        this.typeString = typeString;
        this.parameters = Arrays.asList(parameters);
    }

    public static void resetCount(){
        totalSurfaces = 0;
    }

    public static MCNP_Surface sphere(String name, Double x0, Double y0, Double z0, Double radius) {
        return new MCNP_Surface(name, "s", x0, y0, z0, radius);
    }

    public static MCNP_Surface sphere(String name, Double radius) {
        return new MCNP_Surface(name, "so", radius);
    }

    public int getId() {
        return id;
    }

    public void setTransformation(MCNP_Transformation transformation) {
        this.transformation = transformation;
    }

    public MCNP_Transformation getTransformation() {
        return transformation;
    }

    public MCNP_Card getCard() {
        MCNP_Card card = new MCNP_Card();
        card.addEntry(id);
        if (transformation != null) {
            card.addEntry(transformation.getId());
        }
        card.addEntry(typeString);
        for (Double parameter : parameters) {
            card.addEntry(parameter);
        }
        card.addComment(name);
        return card;
    }

    public static void main(String ... args) {
        System.out.println(MCNP_Card.fullLineCommentCard());
        System.out.println(MCNP_Card.centeredTextCard("C ", "SURFACE CARDS", "*"));
        System.out.println(MCNP_Card.fullLineCommentCard());
        System.out.println(MCNP_Surface.sphere("Test", 2.0).getCard());
        MCNP_Surface longSurface = new MCNP_Surface("Test", "s", 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9);
        System.out.println(longSurface.getCard());
    }
}
