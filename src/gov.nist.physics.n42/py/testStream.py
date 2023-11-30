import jpype
import jpype.imports

jpype.startJVM(
        classpath = [
            '../../gov.llnl.utility/dist/*',
            '../../gov.nist.physics.n42/dist/*',
            ], 
        convertStrings=False)
print(jpype.java.lang.System.getProperty("java.class.path"))

from java.nio.file import Paths
from gov.nist.physics.n42 import RadDataFileStream
from gov.nist.physics.n42.data import RadInstrumentData

try:
     
    with RadDataFileStream(Paths.get("orca.n42")) as rdfs:
        for record in rdfs:
            print(record)
  
except Exception as ex:
    print(ex)
    print(ex.stacktrace())
