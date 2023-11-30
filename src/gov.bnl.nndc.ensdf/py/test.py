import startJVM
import jpype
import jpype.imports

# jvm_path = '../dist/*:/Users/cheung27/Documents/Radiation Detection/RadSim/gov.llnl.rtk/dist/*'
# jpype.startJVM(classpath=jvm_path)

from java.nio.file import Paths
from gov.llnl.ensdf import EnsdfParser

records = EnsdfParser.parseFile(Paths.get("../data/Cs-137.txt"))
decay = records[0]
print(decay.identification)
for hist in decay.history:
    print(hist)
print(decay.QValue)
print(decay.parents)
print(decay.normalizations)
print(decay.unassigned)
for level in decay.levels:
    print (level.toString())
    print("level E = ", level.E)
    for gamma in level.gamma:
        print("gamma E = ", gamma.E, gamma.M)
    for xray in level.xray:
        print("xray E = ", xray.E, "RI = ", xray.RI, xray.M)
    for auger in level.auger:
        print("auger E = ", auger.E, "RI = ", auger.RI, auger.M)
    print("--end of level--")

# for comment in decay.identification.comments:
#     print(comment.contents)
# import pdb; pdb.set_trace()