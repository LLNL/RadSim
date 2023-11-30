# -*- coding: utf-8 -*-
"""
Created on Sun May 21 17:33:05 2017

@author: nelson85
"""

## Dump methods

import jpype as _jp
from . import _inspect


# This is a debugging function
def dumpClassPath():
    """ Prints the classpath of the jvm. """
    cl = _jp.java.lang.ClassLoader.getSystemClassLoader()
    urls = cl.getURLs()
    for val in urls:
        print(val)


# This is a debugging function will be removed
def dumpProperties():
    """ Dump a list of all java system properties.

    Args:
        None

    Returns:
        None
    """
    props = _jp.java.lang.System.getProperties()
    for prop in props:
        print("%s = %s" % (prop, _jp.java.lang.System.getProperty(prop)))
