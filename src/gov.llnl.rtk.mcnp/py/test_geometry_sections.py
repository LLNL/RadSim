import jpype
import start_jvm

import numpy as np
import matplotlib.pyplot as plt
import addcopyfighandler

from gov.llnl.rtk.physics import MaterialImpl

MCNP_Photon = jpype.JClass('mcnp_api.MCNP_Photon')
MCNP_Job = jpype.JClass('mcnp_api.MCNP_Job')
MCNP_Cell = jpype.JClass('mcnp_api.MCNP_Cell')
MCNP_Source = jpype.JClass('mcnp_api.MCNP_Source')
MCNP_Deck = jpype.JClass('mcnp_api.MCNP_Deck')

Vector3 = jpype.JClass('gov.llnl.math.euclidean.Vector3')
Vector3Impl = jpype.JClass('gov.llnl.math.euclidean.Vector3Impl')
SphericalSection = jpype.JClass('gov.llnl.rtk.physics.SphericalSection')
ConicalSection = jpype.JClass('gov.llnl.rtk.physics.ConicalSection')
CylindricalSection = jpype.JClass('gov.llnl.rtk.physics.CylindricalSection')


def get_offset_vector(offset, origin, axis):
    return Vector3.of(
        offset * axis.getX() / axis.norm() + origin.getX(),
        offset * axis.getY() / axis.norm() + origin.getY(),
        offset * axis.getZ() / axis.norm() + origin.getZ()
    )


def main(origin, axis):
    # Calculate the reverse axis
    reverse_axis = Vector3.of(-axis.getX(), -axis.getY(), -axis.getZ())

    # Define some material
    material = MaterialImpl()
    material.setDensity(11.34)
    material.addElement("Pb206", 0.241, 0.0)
    material.addElement("Pb207", 0.221, 0.0)
    material.addElement("Pb208", 0.524, 0.0)

    # Define sections
    sphere = SphericalSection.Sphere(origin, 1.0)
    cap = SphericalSection(origin, axis, 0.0, np.pi/2, 1.5, 2.5)
    container = CylindricalSection(get_offset_vector(-3.0, origin, axis), axis, 1.5, 2.5, 3.0)
    floor = CylindricalSection.Cylinder(get_offset_vector(-2.75, origin, axis), axis, 1.5, 0.5)
    holder = ConicalSection(origin, reverse_axis, np.pi/16, 5*np.pi/32, 1.0, 2.25)

    # Make source
    source = MCNP_Source("Source", MCNP_Photon(), int(1e6))

    # Build and print deck
    deck = MCNP_Deck("Section Test", source)
    sections = [sphere, cap, container, floor, holder]
    for section in sections:
        section.setMaterial(material)
        cell = MCNP_Cell.fromSection("", section)
        deck.addCells(cell)
    deck.addParticles(MCNP_Photon())

    file = open('test_dir/section_test.input', 'w')
    file.write(str(deck))
    file.close()


if __name__ == '__main__':
    main(origin=Vector3.of(2.0, 0.0, 0.0), axis=Vector3.of(1.0, 1.0, 0.0))

