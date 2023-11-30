# -*- coding: utf-8 -*-
"""
Created on Sun May 21 18:07:39 2017

@author: nelson85
"""

import sys as _sys
import os as _os
import glob as _glob
import jpype

# FIXME this function needs to be os aware to
# that is can properljy set up the class path for mac.
def classpath():
    """ Get the class path from the environment """
    out=[]
    classpath=_os.environ.get("CLASSPATH");
    # print(classpath)
    sep=_os.pathsep
    if _sys.platform == 'cygwin':
        sep=';'
    elms=classpath.split(sep)
    for v in elms:
        if v == '':
          continue
        v=v.replace('\\','/')
        if v.endswith('*'):
            out.extend(_glob.glob(v+'.jar'))
        elif v.endswith('.jar'):
            out.append( v )
        else:
            raise NotImplementedError("Unknown element '%s' in classpath!" % v)
            
    path=sep.join(out)
    return path

def client():
    if _sys.platform == 'darwin':
        return jpype.getDefaultJVMPath()
    jdk=_os.environ.get("JAVA_HOME");
    return _os.path.join(jdk,'jre','bin','server','jvm.dll')
    # return jre+"\\bin\\server\\jvm.dll";
