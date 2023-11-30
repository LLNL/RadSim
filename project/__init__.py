import sys
import argparse
import os
import subprocess
import pathlib
import shutil
import string
import glob
import fnmatch
import datetime
import platform
import shlex

try:
    os.environ["JAVA_HOME"]
except KeyError:
    raise RuntimeError("JAVA_HOME not set")


class Package(object):
    def __init__(self, **kwargs):
        self._properties=kwargs
    def __getitem__(self, key):
        return self._properties.get(key, None)
    def __getattr__(self, key):
        return self._properties.get(key, None)

class Properties(object):
    def __init__(self, parent=None):
        self.parent = parent
        self.internal = dict()

    def __getitem__(self, key):
        value = self.get(key, "")
        if "$" in value:
            return string.Template(value).substitute(self)
        return value

    def __setitem__(self, key, value):
        self.internal[key] = value

    def get(self, key, default):
        value = self.internal.get(key, None)
        if value == None:
            if self.parent:
                return self.parent.get(key, default)
            else:
                return default
        return value

    def subst(self, pattern):
        if not pattern:
            return None
        if isinstance(pattern, list):
            out = [string.Template(i).substitute(self) for i in pattern]
            return [i for i in out if i != ""]
        return string.Template(pattern).substitute(self)

    def copy(self, using, items):
        for i in items:
            self.internal[i] = getattr(using, i)

    def require(self, key):
        value = self.get(key, None)
        if value == None:
            raise RuntimeError(
                "%s is required, use '-D %s=value'" % (key, key))


class Target(object):
    def __init__(self, name, depends=None, destination=None, prerequisites=None):
        self.name = name
        self.depends = depends
        self.destination = destination
        self.prerequisites = prerequisites

    def check(self, args, order, visited):
        if self in visited:
            return

        print("  Check", self.name)
        visited.add(self)
        destination = args.subst(self.destination)
        if destination and os.path.exists(destination):
            uptodate = True
            if self.prerequisites:
                for pre in self.prerequisites:
                    if not os.path.exists(pre):
                        uptodate = False
                        break
                    if os.path.getmtime(self.destination) < os.path.getmtime(pre):
                        uptodate = False
                        break
            print("    Dest", destination, uptodate)
            if uptodate:
                return

        if self.depends:
            for dep in self.depends:
                reqs = dep.check(args, order, visited)

        order.append(self)

    def run(self, args):
        pass


class Project(object):
    def __init__(self):
        self.targets = {}
        self.map = dict()
        self.defines = set()
        if platform.system() == 'Windows':
            self.map["ant"] = "ant.bat"
            self.map["mvn"] = "cmd /c mvn"
        else:
            self.map["ant"] = "ant"
            self.map["mvn"] = "mvn"

    def parse(self):
        parser = argparse.ArgumentParser(description="""Process target(s).
        Possible targets include 'pull', 'push', 'status', 'jar'.
        Use option "--list" for a complete list of targets.""")

        parser.add_argument('targets', metavar='target', type=str, nargs='*',
                            help='target to run')
        parser.add_argument('-D', '--define', metavar="define", nargs=1, action='append',
                            help='options of the form key=value')
        parser.add_argument('--list', action='store_true',
                            help='list all defined targets and exit')
        args = parser.parse_args()
        if args.define:
            self.map.update({i[0].split('=')[0]: i[0].split('=')[1]
                             for i in args.define if '=' in i[0]})
            self.defines.update([i[0] for i in args.define if not '=' in i[0]])
        if args.list:
            for target in self.targets:
                print(target)
            sys.exit()
        if not args.targets:
            print("WARNING: no targets supplied. Use option --list to see all targets")
        self.args = args

    def group(self, name):
        return TargetGroup(name, project=self)

    def append(self, target):
        self.targets[target.name] = target
        return target

    def property(self, key, value):
        if key in self.map:
            print("Refuse to override property ", key)
        else:
            self.map[key] = value

    def execute(self):
        visited = set()
        tasks = []
        props = Properties(self.map)

        print("Check up-to-date")
        for name in self.args.targets:
            if not name in self.targets:
                raise RuntimeError("Unknown target %s" % name)
            self.targets[name].check(props, tasks, visited)

        print("Execute tasks", [i.name for i in tasks])

        for task in tasks:
            print("=======", task.name)
            task.run(Properties(self.map))

    def getTarget(self, name):
        return self.targets[name]


###################################################################################

class TargetGroup(Target):
    def __init__(self, name, *, project=None):
        self.project = project
        Target.__init__(self, name, depends=[])
        self.project.append(self)

    def append(self, target):
        self.depends.append(target)
        self.project.append(target)


class GitClone(Target):
    def __init__(self, package, *, repo=None, directory="$src", branch="master", output=None):
        self.repo = repo
        self.package = package
        self.directory = directory
        self.branch = branch
        if not output:
            output = package
        self.output = output
        Target.__init__(self, "clone-%s" % package,
                        destination=os.path.join(directory, output))

    def run(self, args):
        args.copy(self, ["directory", "repo", "package", "branch", "output"])
        directory = args["directory"]
        cwd = os.path.join(directory, self.package)
        pathlib.Path(directory).mkdir(parents=True, exist_ok=True)
        run = args.subst(
            ["git", "clone", "$repo/${package}.git", "-b", "${branch}", "${output}"])
        print(" ".join(run))
        rc = subprocess.run(run, cwd=directory)
        if rc.returncode != 0:
            raise RuntimeError("task failed")


class GitCheckout(Target):
    """ Make sure that we are on the right branch"""
    def __init__(self, package, *, directory="$src", branch="master"):
        self.package = package
        self.directory = directory
        self.branch = branch
        Target.__init__(self, "checkout-%s" % package)

    def run(self, args):
        args.copy(self, ["directory", "package", "branch"])
        directory = args["directory"]
        cwd = os.path.join(directory, self.package)
        run = args.subst(
            ["git", "checkout", "${branch}"])
        print(" ".join(run))
        rc = subprocess.run(run, cwd=cwd)
        if rc.returncode != 0:
            raise RuntimeError("task failed")


class GitArchive(Target):
    def __init__(self, package, *, repo=None, directory=".", branch="master", output=None):
        self.repo = repo
        self.package = package
        self.branch = branch
        self.directory = directory
        if not output:
            output = package
        self.output = output
        Target.__init__(self, "archive-%s" % package,
        destination=os.path.join(directory, output))

    def run(self, args):
        args.copy(self, ["directory", "repo", "package", "branch", "output"])
        directory = args["directory"]
        cwd = os.path.join(directory, args["output"])
        pathlib.Path(cwd).mkdir(parents=True, exist_ok=True)
        run = args.subst("git archive --remote '${repo}/${package}.git' --prefix='${package}/' ${branch}")
        p1 = subprocess.Popen(shlex.split(run), stdout=subprocess.PIPE)
        p2 = subprocess.Popen(["tar","xf","-","--strip-components=1"], cwd=cwd, stdin=p1.stdout)
        p2.wait()
        print("Copied archived files")
        if p2.returncode != 0:
            raise RuntimeError("task failed")


class GitTarget(Target):
    def __init__(self, package, *, directory="src", command=None, args=""):
        self.package = package
        self.directory = directory
        if not command:
            raise RuntimeError("Git command not set")
        self.command = command
        self.args = args
        Target.__init__(self, "git-%s-%s" % (command, package))

    def run(self, args):
        args.copy(self, ["directory", "command", "args"])
        cwd = os.path.join(args["directory"], self.package)
        
        # Check if we need to restore xsd file before running git command
        try:
            run = "git status"
            print("    Run ", run)      
            _output = str(subprocess.check_output(shlex.split(run), cwd=cwd))
            if ".xsd" in _output:
                # xsd file was modified so restore it
                run = "git restore *.xsd"
                print("    Run ", run) 
                rc = subprocess.run(shlex.split(run), cwd=cwd)
        except:
            # Something went wrong while trying to run "git status"
            # Ignore and continue. Let the next statement handles any failure
            pass
      
        run = args.subst("git ${command} ${args}")
        print("    Run ", run)
        rc = subprocess.run(shlex.split(run), cwd=cwd)
        if rc.returncode != 0:
            raise RuntimeError("task failed")


class GitPull(GitTarget):
    def __init__(self, package, *, directory="$src"):
        GitTarget.__init__(self, package, directory=directory, command="pull")

class GitStatus(GitTarget):
    def __init__(self, package, *, directory="$src"):
        GitTarget.__init__(
            self, package, directory=directory, command="status")


class GitDiff(GitTarget):
    def __init__(self, package, *, directory="$src"):
        GitTarget.__init__(
            self, package, directory=directory, command="diff")


class GitPush(GitTarget):
    def __init__(self, package, *, directory="$src"):
        GitTarget.__init__(self, package, directory=directory, command="push")


class GitAdd(GitTarget):
    def __init__(self, package, *, directory="$src"):
        GitTarget.__init__(self, package, directory=directory,
                           command="add", args="--ignore-removal .")


class GitCommit(GitTarget):
    def __init__(self, package, *, directory="$src"):
        GitTarget.__init__(
            self, package, directory=directory, command="commit")

    def run(self, args):
        args.copy(self, ["directory", "command", "args"])
        args.require("mesg")
        cwd = os.path.join(args["directory"], self.package)
        execute = args.subst("git  ${command}  ${args}  -m '${mesg}'")
        rc = subprocess.run(shlex.split(execute), cwd=cwd)

class GitTag(GitTarget):
    def __init__(self, package, *, directory="$src"):
        GitTarget.__init__(
            self, package, directory=directory, command="tag")

    def run(self, args):
        args.copy(self, ["directory", "command", "args"])
        args.require("tag")
        cwd = os.path.join(args["directory"], self.package)
        execute = args.subst("git  ${command}  ${args} '${tag}'")
        rc = subprocess.run(shlex.split(execute), cwd=cwd)

class GitBranch(GitTarget):
    def __init__(self, package, *, directory="$src"):
        GitTarget.__init__(
            self, package, directory=directory, command="checkout -b")

    def run(self, args):
        args.copy(self, ["directory", "command", "args"])
        args.require("tag")
        cwd = os.path.join(args["directory"], self.package)
        execute = args.subst("git  ${command}  ${args} '${tag}'")
        rc = subprocess.run(shlex.split(execute), cwd=cwd)
        execute = args.subst("git  push --set-upstream origin '${tag}'")
        rc = subprocess.run(shlex.split(execute), cwd=cwd)

class GitSwitch(GitTarget):
    def __init__(self, package, branch, *, directory="$src"):
        GitTarget.__init__(
            self, package, directory=directory, command="checkout")
        self.branch = branch

    def run(self, args):
        args.copy(self, ["directory", "command", "args", "branch"])
        cwd = os.path.join(args["directory"], self.package)
        execute = args.subst("git  ${command}  ${args} '${branch}'")
        print(execute)
        rc = subprocess.run(shlex.split(execute), cwd=cwd)


class Ant(Target):
    def __init__(self, task, package, *, directory="$src", depends=None, opts=None):
        self.task = task
        self.directory = directory
        self.package = package
        Target.__init__(self, "ant-%s-%s" % (task, package), depends=depends)

    def run(self, args):
        args.copy(self, ["directory", "package", "task"])
        cwd = os.path.join(args["directory"], self.package)
        run = args.subst('${ant} -Dplatform.active=default_platform -Dplatforms.default_platform.home="%s" ${task}' % os.environ["JAVA_HOME"])
        print("  cd ", cwd)
        print("  ", run)
        rc = subprocess.run(shlex.split(run), cwd=cwd)
        if rc.returncode != 0:
            raise RuntimeError("task failed")


class Mvn(Target):
    def __init__(self, task, package, *, directory="$src", depends=None, opts=None):
        self.task = task
        self.directory = directory
        self.package = package
        self.opts = opts
        Target.__init__(self, "mvn-%s-%s" % (task, package), depends=depends)

    def run(self, args):
        args.copy(self, ["directory", "package", "task", "opts"])
        cwd = os.path.join(args["directory"], self.package)
        run = args.subst("${mvn} ${task} ${opts}")
        print(run)
        rc = subprocess.run(shlex.split(run), cwd=cwd)
        if rc.returncode != 0:
            raise RuntimeError("task failed")


class CopyJars(Target):
    def __init__(self, name, packages=[], *, srcdir=".", destdir=None, depends=None):
        self.srcdir = srcdir
        self.destdir = destdir
        self.packages = packages
        Target.__init__(self, "%s-jars" % name, depends=depends)

    def run(self, args):
        srcdir = args.subst(self.srcdir)
        destdir = args.subst(self.destdir)
        pathlib.Path(destdir).mkdir(parents=True, exist_ok=True)
        for package in self.packages:
            if os.path.exists(os.path.join(srcdir, package, "nbproject")):
                self.copywildcard(os.path.join(
                    srcdir, package, "dist", "*.jar"), destdir)
            if os.path.exists(os.path.join(srcdir, package, "lib")):
                self.copywildcard(os.path.join(
                    srcdir, package, "lib", "*.jar"), destdir)
            if os.path.exists(os.path.join(srcdir, package, "target")):
                self.copywildcard(os.path.join(
                    srcdir, package, "target", "*.jar"), destdir)

            #!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            # gov.llnl.ernie.viewer has required jar and dll that has to be
            # copied. If gov.llnl.ernie.viewer ever got rid of sqljdbc4.jar
            # removed the conditional statement below:
            if os.path.exists(os.path.join(srcdir, package, "dependency")):
                self.copywildcard(os.path.join(
                    srcdir, package, "dependency", "*.jar"), destdir)
                self.copywildcard(os.path.join(
                    srcdir, package, "dependency", "*.dll"), destdir)
            #!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    def copywildcard(self, src, dest):
        for file in glob.glob(src):
            print("  Copy: ", file)
            shutil.copy(file, dest)


class Touch(Target):
    def __init__(self, name, *, target=None):
        if not isinstance(target, str):
            raise RuntimeError("Target is required")
        self.target = target
        Target.__init__(self, "touch-%s" % name, depends=None)

    def run(self, args):
        target = args.subst(self.target)
        with open(target, 'wb'):
            pass


class StageCopy(Target):
    def __init__(self, name, *, includes=[], excludes=[], recursive=False, srcdir=".", destdir=None, depends=None):
        self.srcdir = srcdir
        self.destdir = destdir
        if isinstance(includes, str):
            includes = [includes]
        if isinstance(excludes, str):
            excludes = [excludes]
        self.includes = includes
        self.excludes = excludes
        self.recursive = recursive
        Target.__init__(self, "stage-%s" % name, depends=depends)

    def run(self, args):
        srcdir = args.subst(self.srcdir)
        destdir = args.subst(self.destdir)
        pathlib.Path(destdir).mkdir(parents=True, exist_ok=True)
        pattern = "*"
        if self.recursive:
            pattern = "**/*"
        for file in pathlib.Path(srcdir).glob(pattern):
            rel = file.relative_to(srcdir)
            if os.path.isdir(file):
                continue
            if not self.testMatch(rel):
                continue
            dest = os.path.join(destdir, rel)
            pathlib.Path(os.path.dirname(dest)).mkdir(
                parents=True, exist_ok=True)
            print("  Copy", file)
            shutil.copy(file, dest)

    def testMatch(self, test):
        for pattern in self.excludes:
            if fnmatch.fnmatch(test, pattern):
                return False

        matches = False
        for pattern in self.includes:
            if fnmatch.fnmatch(test, pattern):
                matches = True
                break
        return matches


class StageSrc(StageCopy):
    def __init__(self, package, *, srcdir=".", destdir=None, depends=None):
        StageCopy.__init__(self, "src-%s" % package, depends=depends,
                           srcdir=os.path.join(srcdir, package),
                           destdir=os.path.join(destdir, package),
                           recursive=True,
                           #!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                           # gov.llnl.ernie.viewer has required jar and dll
                           # in the depedency folder. If gov.llnl.ernie.viewer
                           # ever got rid of sqljdbc4.jar remove the word
                           # "dependency" from the list
                           includes=["Notice", "pom.xml", "src/*", "test/*",
                                     "nbproject/*", "build.xml", "ivy.xml",
                                     "dependency/*"],
                           excludes=["nbproject/*/private"]
                           )
        self.package = package


class StageExtra(StageCopy):
    def __init__(self, files, *, srcdir=".", destdir=None, depends=None):
        StageCopy.__init__(self, "extra", depends=depends,
                           srcdir=srcdir,
                           destdir=destdir,
                           includes=files
                           )


class Tar(Target):
    def __init__(self, *, srcdir=".", dest=None, depends=None):
        Target.__init__(self, "package", depends=depends)
        self.srcdir = srcdir
        self.dest = dest

    def run(self, args):
        srcdir = args.subst(self.srcdir)
        parent = os.path.dirname(srcdir)
        basename = os.path.basename(srcdir)
        dest = args.subst(self.dest)
        pathlib.Path(os.path.dirname(dest)).mkdir(parents=True, exist_ok=True)
        target = os.path.relpath(dest, parent)
        run = ["tar", "czf", target, basename]
        print("  Execute '%s' in '%s'" % (" ".join(run), parent))
        rc = subprocess.run(run, cwd=parent)
        if rc.returncode != 0:
            raise RuntimeError("task failed")


class EncryptGPG(Target):
    def __init__(self, *, src=None, directory=".", depends=None):
        Target.__init__(self, "encrypt-gpg", depends=depends)
        self.src = src
        self.directory = directory
        if not src:
            raise RuntimeError("src not defined")

    def run(self, args):
        args.require("recipient")
        args.copy(self, ["directory", "src"])
        tarball = args['tarball']
        filename = os.path.join(args["directory"], args["src"])
        cwd = os.path.dirname(filename)
        args['tarball'] = os.path.basename(filename)
        run = args.subst(["gpg", "--yes", "-e", "-r",
                          "${recipient}", "${tarball}"])
        print("  Execute '%s' in '%s'" % (" ".join(run), cwd))
        rc = subprocess.run(run, cwd=cwd)
        if rc.returncode != 0:
            raise RuntimeError("task failed")


class CleanDir(Target):
    def __init__(self, name, *, directory=".", depends=None):
        Target.__init__(self, "clean-%s" % name, depends=depends)
        self.directory = directory

    def run(self, args):
        args.copy(self, ["directory"])
        if os.path.exists(args["directory"]):
            shutil.rmtree(args["directory"])
