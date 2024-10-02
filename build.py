#!/usr/bin/env python3

# Import the project resources
from project import *

# Define the packages
packages = [
    ('src/gov.llnl.utility',       'src/gov.llnl.utility',       'ant'),
    ('src/gov.llnl.math',          'src/gov.llnl.math',          'ant'),
    ('src/gov.llnl.rtk',           'src/gov.llnl.rtk',           'ant'),
    ('src/gov.nist.xray',          'src/gov.nist.xray',          'ant'),
    ('src/gov.bnl.nndc.ensdf',     'src/gov.bnl.nndc.ensdf',     'ant'),
    ('src/gov.nist.physics.n42',   'src/gov.nist.physics.n42',   'ant'),
    ('src/gov.llnl.mcnp',          'src/gov.llnl.mcnp',          'ant'),
    ('src/gov.llnl.geant4',        'src/gov.llnl.geant4',        'ant'),
]

project = Project()
project.property("stage", "$build/$project")
project.property("date", datetime.date.today().strftime("%Y%m%d"))

jar = project.group("jar")
for package in packages:
    if package[2]=="ant":
        jar.append(Ant("jar", package[0], depends=None))
    elif package[2]=="mvn":
        jar.append(Mvn("package", package[0], depends=None))
jar.append(CopyJars("copy", [i[0] for i in packages], srcdir="$src", destdir="jars"))

clean = project.group("clean")
for package in packages:
    if package[2]=="ant":
        clean.append(Ant("clean", package[0], depends=None))
    elif package[2]=="mvn":
        clean.append(Mvn("clean", package[0], depends=None))

###################################################################################

project.parse()
project.execute()
