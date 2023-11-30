import jpype
import jpype.imports
import argparse

jpype.startJVM(
        classpath = [
            '../../gov.llnl.utility/dist/*',
            '../../gov.nist.physics.n42/dist/*',
            ], 
        convertStrings=False)

parser = argparse.ArgumentParser(description='Parse an N42 file')
parser.add_argument('filename', type=str, nargs=1, help='file to process')
parser.add_argument('xslt_file', type=str, nargs=1, help='xslt file')
args = parser.parse_args()
n42_file = args.filename[0]
xslt_file = args.xslt_file[0]

from java.nio.file import Paths
from gov.llnl.utility.xml.bind import DocumentReader
from gov.nist.physics.n42.data import RadInstrumentData

dr = DocumentReader.create(RadInstrumentData)
dr.setProperty(DocumentReader.SCHEMA_SOURCE, "http://physics.nist.gov/N42/2011/n42.xsd")
dr.setProperty(DocumentReader.XSLT_SOURCE, xslt_file)
try:
    dr.loadFile(Paths.get(n42_file))
    print("Successfully read in %s" % n42_file)	
except Exception as ex:
    print(ex)
    print(ex.stacktrace())
