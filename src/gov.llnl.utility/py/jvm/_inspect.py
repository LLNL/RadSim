# -*- coding: utf-8 -*-
import jpype as _jp
import re as _re

# Probe methods
def getTypeName(var)->str:
    """ Get the java type name of this object.

    Args:
        var (java array or object): object to probe

    Returns:
        str holding the name of class, or None if not java object
    """
    if isinstance(var, str):
        return "string"
    if isObject(var):
        return var.__name__
    if isClass(var):
        return var.__name__
    if isArray(var):
        return var.__name__
    return None

def getArrayInfo(var):
    arrayName=getTypeName(var)
    count=arrayName.count('[')
    arrayType=arrayName[:arrayName.find('[')]
    return (_jtype(arrayType),count)

_typemap={
    "boolean":  _jp.JBoolean,
    "byte":     _jp.JByte,
    "short":    _jp.JShort,
    "long":     _jp.JLong,
    "int":      _jp.JInt,
    "float":    _jp.JFloat,
    "double":   _jp.JDouble,
    "char":     _jp.JChar,
    "string":   _jp.JString,
    }

def _jtype(var):
    t1=_typemap.get(var)
    if t1!=None:
        return t1
    return _jp.JClass(var)


#%%
def isPackage(var)->bool:
    """ Determine if a java object is a package. """
    return isinstance(var, _jp._jpackage.JPackage)

def isClass(var)->bool:
    """ Determine if a java object is a class """
    return isinstance(var, _jp._jclass._JavaClass)

def isObject(var)->bool:
    return isinstance(var, _jp._jclass._JAVAOBJECT)

def isArray(var)->bool:
    return isinstance(var, _jp._jarray._JavaArrayClass)

