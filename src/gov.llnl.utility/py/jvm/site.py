# -*- coding: utf-8 -*-
"""
Created on Mon May 29 02:10:08 2017

@author: nelson85
"""

import os
import importlib
import importlib.util
from ._jimport import *

class SitePy(JImportCustomizer):
    def canCustomize(self, name):
        if name.startswith('gov.llnl') and name.endswith('.py'):
            return True
        return False

    def getSpec(self, name):
        pname = name[:-3]
        devel = os.environ.get('DEVEL')
        path = os.path.join(devel,pname,'py','__init__.py')
        return importlib.util.spec_from_file_location(name, path)

registerImportCustomizer(SitePy())
#import gov.llnl.test
#import gov.llnl.test.py as test

