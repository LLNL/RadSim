import numpy as np
energies = (np.loadtxt('neutron.txt', delimiter=',')[:,0])
energies = energies[::-1]
print(energies)


for i in range(len(energies)-1):
    with open("ngroup%02d.gam"%i, "w") as fd:
        print("GamFileVersion = 2.0", file=fd)
        print("DataType = SnTransport 18.6.3.0", file=fd)
        print("Geometry = Spherical", file=fd)
        print("LargestDimension = 4.950E+00", file=fd)
        print("Data =", file=fd)
        print("        0         0        1 ! photon lines, photon groups, neutron groups", file=fd)
        print(" %8.4E     %8.4E ! neutron groups"%(energies[i+1], 1000), file=fd)
        print(" %8.4E     %8.4E"%(energies[i], 0.0), file=fd)

with open("neutron.bat", "w") as fd:
    for i in range(len(energies)-1):
        print("GADSPE , ngroup%02d.gam                response * ngroup%0d2"%(i,i), file=fd)

