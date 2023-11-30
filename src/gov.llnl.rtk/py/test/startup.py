# Get the path
import sys
import os
devel=os.environ.get('DEVEL')
sys.path.append(devel+'/gov.llnl.utility/py')
del devel
#
#%%
# Need numpy for arrays
import numpy as np

# Pull in some commands to make matlab translation easy
from jpype import *
import jvm
jvm.control.start()
import matplotlib.pyplot as plt

#%matplotlib inline
#%matplotlib

#%%
# Import java bridge
#import local
import importlib

import java.lang
import gov.llnl.utility
import gov.llnl.math
import gov.llnl.rtk
import gov.llnl.rdak

