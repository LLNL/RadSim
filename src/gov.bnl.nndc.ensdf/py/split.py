import startJVM

# input("go to Netbeans and attach debugger")

import jpype
import jpype.imports

from java.nio.file import Paths
from gov.llnl.ensdf import EnsdfParser
from gov.llnl.ensdf.decay import SplitIsomers
from gov.llnl.ensdf.decay import SandiaFormatter
from gov.nist.xray import NISTLibrary

# records = EnsdfParser.parseFile(Paths.get("../data/Cs-137.txt"))
# records = EnsdfParser.parseFile(Paths.get("../data/CS-137-BNL.txt"))
# records = EnsdfParser.parseFile(Paths.get("../data/Ba-137IT.txt"))
records = EnsdfParser.parseFile(Paths.get("../data/BNL-everything.txt"))
# records = EnsdfParser.parseFile(Paths.get("../data/test-record.txt"))
# records = EnsdfParser.parseFile(Paths.get("../data/U-238-BNL.txt"))
# records = EnsdfParser.parseFile(Paths.get("../data/Ir-192.txt"))
# records = EnsdfParser.parseFile(Paths.get("../data/Mo-99-BNL.txt"))

# index = 0
# print(records[index])
# si = SplitIsomers(records[index])
# # si.setOptions(Option.USE_MS_ISOMERS)
# si.setXrayLibrary(NISTLibrary.getInstance())
# # si.split = False
# dts = si.execute()

# sf = SandiaFormatter()
# for dt in dts:
#     print(sf.format(dt))

##### Mass Testing
# ignoreList = [18, 101, 126, 155, 168, 169, 261]
# ignoreList = []

i = 0
for record in records:
    # if i in ignoreList:
    #     print("record ignored")
    #     i += 1
    #     continue
    print("record # ", i)
    si = SplitIsomers(record)
    # si.setOptions(Option.USE_MS_ISOMERS)
    si.setXrayLibrary(NISTLibrary.getInstance())
    # si.split = False
    print(record.identification)
    dts = si.execute()

    sf = SandiaFormatter()
    for dt in dts:
        print(sf.format(dt))
    i += 1

### End of Mass Testing

# import pdb; pdb.set_trace()