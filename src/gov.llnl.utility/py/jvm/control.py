# -*- coding: utf-8 -*-
"""
Created on Sun May 21 18:17:57 2017

@author: nelson85
"""
import jpype as _jp
from . import environ as _env

jvmStarted=False

def start():
    """ Start the jvm.  This can only be executed once """
    global jvmStarted
    if jvmStarted==True:
        return
    jvmStarted=True
    cp=_env.classpath()
    _jp.startJVM(_jp.getDefaultJVMPath(),
       r'-Djava.class.path='+cp,
#       r'-Dsun.boot.library.path='+jre+r'\bin',
#       r'-Xbootclasspath:'+jre+r'\lib\rt.jar'
       )

def end():
    """ Shutdown the jvm. Accessing java objects after end()
    will likely cause a crash. """
    _jp.shutdownJVM()
