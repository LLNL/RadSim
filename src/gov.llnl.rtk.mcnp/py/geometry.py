from gov.llnl.math.euclidean import Vector3

def get_offset_vector(offset, axis, origin=Vector3.ZERO):
    return Vector3.of(
        offset * axis.getX() / axis.norm() + origin.getX(),
        offset * axis.getY() / axis.norm() + origin.getY(),
        offset * axis.getZ() / axis.norm() + origin.getZ()
    )