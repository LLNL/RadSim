# This is an example computing a rectangular box with 3 faces moving down the road.
import numpy as np
import jpype
import jpype.imports
from jpype.types import *
import matplotlib.pyplot as plt

# Start JPype with required jars
jpype.startJVM(classpath=[
    '../../gov.llnl.utility/dist/*',
    '../../gov.llnl.math/dist/*',
    '../../gov.llnl.rtk/dist/*'], convertStrings = False)

from gov.llnl.rtk.view import SensorViewFactory
from gov.llnl.rtk.view import SensorViewEncounter
from gov.llnl.rtk.view import FixedInstantList
from gov.llnl.math.euclidean import Vector3
from gov.llnl.math.euclidean import Versor
from gov.llnl.rtk.view import TraceFactory, Trace, SensorView
from java.time import Instant, Duration
from java.time.temporal import ChronoUnit
from java.util import Collection

# Create a box centered at zero (End area is 0.25 m^2,  Side area is 1 m^2)
cuboid = SensorViewFactory.createCuboid(0.5,0.5,2, Vector3.of(1,0,0), Versor.ZERO)
#for face in cuboid:
#    print(face)
#    print(face.auditSolidAngle(Vector3.of(-1,0.25,0)))

print("View end  1m", cuboid.computeSolidAngle(Vector3.of(1,0,0)), '=2*pi')
print("View end  -1m", cuboid.computeSolidAngle(Vector3.of(-1,0,0)), '=2*pi')
print("View end  2m", cuboid.computeSolidAngle(Vector3.of(2,0,0)))
print("View end  4m", cuboid.computeSolidAngle(Vector3.of(4,0,0)))
print("View side 0.25m", cuboid.computeSolidAngle(Vector3.of(0.,0.25,0)))
print("View side 1.25m", cuboid.computeSolidAngle(Vector3.of(0,1.25,0)))
print("View top  0.25m", cuboid.computeSolidAngle(Vector3.of(0,0,0.25)))
print("View top  2m", cuboid.computeSolidAngle(Vector3.of(0,0,2)))


# Show how solid angle naive compares with actual solid angle
d = np.array(np.arange(0.1,20,0.1))
sa = [cuboid.computeSolidAngle(Vector3.of(i+1,0,0)) for i in d]
sa0 = [ 0.25/x**2 for x in d]
plt.loglog(d,sa)
plt.loglog(d,sa0)
plt.ylabel("A/R^2")
plt.xlabel("distance")
sa = [cuboid.computeSolidAngle(Vector3.of(0,i+0.25,0)) for i in d]
sa0 = [ 1/x**2 for x in d]
plt.loglog(d,sa)
plt.loglog(d,sa0)
# Long story short, so long as we are 4x the longest side, naive is the same as calculated


# Next set up a simulation

# First we create a LinearTrace between the starting and ending times in the form of a path.
sve = SensorViewEncounter()
start = Instant.parse("2020-01-01T12:00:00Z")
end = start.plus(5, ChronoUnit.SECONDS)
trace = JObject(TraceFactory.linear(start, Vector3.of(3,-10,0), end, Vector3.of(3,10,0)), Trace)

plt.figure()
times = FixedInstantList(start, Duration.ofMillis(10), 500)
out = sve.simulate(cuboid, times, trace)
plt.plot(out.solidAngle)
plt.show()


