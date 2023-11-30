# -*- coding: utf-8 -*-
"""
Created on Sun May 21 18:28:15 2017

@author: nelson85
"""

import jpype as _jp
import os as _os

def Ints(sz):
    return _jp.JArray(_jp.JInt,1)(sz)

def Doubles(sz):
    return _jp.JArray(_jp.JDouble,1)(sz)


# Paths.get is very hard to use from within python.
# Thus we will define a dedicated function for this.
def Path(*name):
    """ Create a new path.

    java.nio.files.Paths.get() is hard to use due to variable
    arguments requirements.  This short cut simplifies
    """
    return _jp.java.io.File(_os.path.join(*name)).toPath()

