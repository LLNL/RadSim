package gov.llnl.rtk.mcnp;

import gov.llnl.math.euclidean.Vector3;
import gov.llnl.math.euclidean.Versor;
import gov.llnl.math.matrix.Matrix;
import gov.llnl.math.matrix.MatrixFactory;
import gov.llnl.math.matrix.MatrixOps;
import gov.llnl.math.matrix.special.MatrixTriDiagonal;

import java.util.List;

/**
 * @author lahmann
 */
public class MCNP_Transformation {

    private static int totalTransformations;
    private Vector3 displacementVector;
    private Matrix rotationMatrix;
    private int id;

    public MCNP_Transformation(Vector3 displacementVector, Matrix rotationMatrix) {
        totalTransformations++;
        this.displacementVector = displacementVector;
        this.rotationMatrix = rotationMatrix;
        this.id = totalTransformations;
    }

    public MCNP_Transformation(Vector3 displacementVector, Vector3 originVector, Vector3 destVector) {
        totalTransformations++;
        this.displacementVector = displacementVector;
        this.id = totalTransformations;

        // Compute the rotation matrix
        Vector3 a = Vector3.of(
                originVector.getX() / originVector.norm(),
                originVector.getY() / originVector.norm(),
                originVector.getZ() / originVector.norm()
        );
        Vector3 b = Vector3.of(
                destVector.getX() / destVector.norm(),
                destVector.getY() / destVector.norm(),
                destVector.getZ() / destVector.norm()
        );
        double c = a.dot(b);
        if (c == -1) {
            rotationMatrix =  MatrixFactory.wrapArray(new double[] {-1, 0, 0, 0, -1, 0, 0, 0, -1}, 3, 3);
            return;
        }

        Vector3 v = Vector3.of(
                a.getY() * b.getZ() - a.getZ() * b.getY(),
                a.getZ() * b.getX() - a.getX() * b.getZ(),
                a.getX() * b.getY() - a.getY() * b.getX()
        );

        Matrix I = MatrixFactory.wrapArray(new double[] {1, 0, 0, 0, 1, 0, 0, 0, 1}, 3, 3);
        Matrix vx = MatrixFactory.wrapArray(
                new double[] {
                        0.0, v.getZ(), -v.getY(),
                        -v.getZ(), 0.0, v.getX(),
                        v.getY(), -v.getX(), 0.0
                }, 3, 3);

        Matrix vx2 = MatrixOps.multiply(vx, vx);
        for (int i = 0; i < vx2.columns(); i++) {
            for (int j = 0; j < vx2.rows(); j++) {
                vx2.set(i, j, vx2.get(i, j) / (1 + c));
            }
        }
        rotationMatrix = MatrixOps.add(MatrixOps.add(I, vx), vx2);

    }

    public static void resetCount(){
        totalTransformations = 0;
    }

    public int getId() {
        return id;
    }

    public MCNP_Card getCard(){
        MCNP_Card card = new MCNP_Card("TR" + id);
        card.addEntry(displacementVector.getX());
        card.addEntry(displacementVector.getY());
        card.addEntry(displacementVector.getZ());
        for (int i = 0; i < rotationMatrix.rows(); i++) {
            for (int j = 0; j < rotationMatrix.columns(); j++) {
                card.addEntry(rotationMatrix.get(j, i));
            }
        }
        return card;
    }

    public static void main(String ... args) {
        MCNP_Transformation transformation = new MCNP_Transformation(Vector3.ZERO, Vector3.AXIS_X, Vector3.of(5.0, 5.0, 0.0));
        System.out.println(transformation.getCard());

    }
}
