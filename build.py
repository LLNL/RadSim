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
project.property("repo", "ssh://git@czgitlab.llnl.gov:7999/rda") # for RDA packages
project.property("src", "src")
project.property("project", "rda")
project.property("build", "build")
project.property("stage", "$build/$project")
project.property("dist", "dist")

# FIXME these need to be platform dependent
if platform.system()=='Windows':
    project.property("ant", "ant.bat")
    project.property("mvn", 'cmd /c mvn')
else:
    project.property("ant", "ant")
    project.property("mvn", "mvn")

project.property("date", datetime.date.today().strftime("%Y%m%d"))

clones = project.group("src")
for package in packages:
    clones.append(GitClone(package[1], branch=package[2], output=package[0]))

pulls = project.group("pull")
pulls.append(GitPull('', directory="project"))
for package in packages:
    pulls.append(GitPull(package[0]))

status = project.group("status")
status.append(GitStatus('', directory='project'))
for package in packages:
    status.append(GitStatus(package[0]))

diffs = project.group("diff")
diffs.append(GitDiff('', directory='project'))
for package in packages:
    diffs.append(GitDiff(package[0]))

adds = project.group("add")
adds.append(GitAdd('', directory='project'))
for package in packages:
    adds.append(GitAdd(package[0]))

commits = project.group("commit")
commits.append(GitCommit('', directory='project'))
for package in packages:
    commits.append(GitCommit(package[0]))

pushs = project.group("push")
pushs.append(GitPush('', directory='project'))
for package in packages:
    pushs.append(GitPush(package[0]))

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

# FIXME:
#javadocs = project.group("javadoc")
#for package in packages:
#    dep = [project.getTarget("clone-%s" % package[1])]
#    if package[3] == "ant":
#        javadocs.append(Ant("-Dnb.internal.action.name=javadoc javadoc", package[0], depends=dep))

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
