# -*- coding: utf-8 -*-
"""
Created on Sun May 21 17:35:50 2017

@author: nelson85
"""

# Utility to see the methods defined for an object
# Borrowed from the internet.  Not very reliable currently.
def dumpInfo(object, spacing=10, collapse=1):
    """Print methods and doc strings.

    Takes module, class, list, dictionary, or string."""
    methodList = [method for method in dir(object) if callable(getattr(object, method))]
    processFunc = collapse and (lambda s: " ".join(s.split())) or (lambda s: s)
    print("\n".join(["%s %s" %
         (method.ljust(spacing),
          processFunc(str(getattr(object, method).__doc__)))
       for method in methodList]))
