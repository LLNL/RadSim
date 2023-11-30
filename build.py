#!/usr/bin/env python3

# Bootstrap for a fresh pull
exec(open('.bootstrap.py').read())

# Import the project resources
from project import *

# Define the packages
packages = [
    ('gov.llnl.utility',       'gov.llnl.utility',       'master',   'ant'),
    ('gov.llnl.math',          'gov.llnl.math',          'master',   'ant'),
    ('gov.llnl.rtk',           'gov.llnl.rtk',           'master',   'ant'),
    ('gov.nist.xray',          'gov.nist.xray',          'master',   'ant'),
    ('gov.bnl.nndc.ensdf',     'gov.bnl.nndc.ensdf',     'master',   'ant'),
    ('gov.nist.physics.n42',   'gov.nist.physics.n42',   'master',   'ant'),
]

project = Project()

if platform.system()=='Windows':
    project.property("ant", "ant.bat")
    project.property("mvn", 'cmd /c mvn')
else:
    project.property("ant", "ant")
    project.property("mvn", "mvn")

project.property("date", datetime.date.today().strftime("%Y%m%d"))

jar = project.group("jar")
for package in packages:
    dep = [project.getTarget("clone-%s" % package[1])]
    if package[3]=="ant":
        jar.append(Ant("jar", package[0], depends=dep))
    elif package[3]=="mvn":
        jar.append(Mvn("package", package[0], depends=dep))
jar.append(CopyJars("copy", [i[0] for i in packages], srcdir="$src", destdir="jars"))

clean = project.group("clean")
for package in packages:
    dep = [project.getTarget("clone-%s" % package[1])]
    if package[3]=="ant":
        clean.append(Ant("clean", package[0], depends=dep))
    elif package[3]=="mvn":
        clean.append(Mvn("clean", package[0], depends=dep))

stages = project.group("stage")
stages.append(CopyJars("stage", [i[0] for i in packages], srcdir="$src", destdir="$stage/jars"))
stages.append(StageCopy("py", srcdir='py', destdir='$stage/py', includes=['*.py'], recursive=True))
stages.append(StageCopy("config", srcdir='config', destdir='$stage/config', includes=['*.xml'], recursive=True))
stages.append(StageExtra(files=["Notice", "build.py", 'Readme.*'], destdir="$stage"))
for package in packages:
    stages.append(StageSrc(package[0], srcdir="$src", destdir="$stage/src"))

pkg = project.append(
    Tar(srcdir="$stage", dest="$dist/$project-$date.tar.gz", depends=[stages]))

project.append(EncryptGPG(src="$dist/$project-$date.tar.gz", depends=[pkg]))
project.append(CleanDir("stage", directory="$stage"))
project.append(CleanDir("build", directory="$build"))
project.append(CleanDir("src", directory="$src"))

###################################################################################

project.parse()
project.execute()
