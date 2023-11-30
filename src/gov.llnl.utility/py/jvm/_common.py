# -*- coding: utf-8 -*-
"""
Created on Sun May 21 18:16:09 2017

@author: nelson85
"""

import jpype as _jp
from . import control as _control

def imports(name):
    _control.start()
    """ Get a package from the jvm """
    if name.find('.')==-1:
        return eval('_jp.JPackage("'+name+'")')
    (p1,p2)=name.split('.',1)
    return eval('_jp.JPackage("'+p1+'").'+p2)

def forName(name):
    return _jp.JClass(name)

