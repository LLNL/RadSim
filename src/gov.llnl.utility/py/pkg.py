# -*- coding: utf-8 -*-
"""
Created on Sun May 21 22:45:13 2017

@author: nelson85
"""

import pip as _pip

def listPackages():
    installed_packages = _pip.get_installed_distributions()
    installed_packages_list = sorted(["%s==%s" % (i.key, i.version)
         for i in installed_packages])
    for i in installed_packages_list:
        print(i)
