package gov.llnl.rtk.physics;

import gov.llnl.math.euclidean.Vector3;

/**
 * @author lahmann
 */
public interface Section {

    void setMaterial(Material material);

    Vector3 getOrigin();

    Vector3 getAxis();

    Material getMaterial();
}
