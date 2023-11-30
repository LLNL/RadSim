import jpype as _jp
from . import _inspect
from . import _common

def ctors(var):
    """ Print a list of all public constructors.

    Args:
        var (java class or object): object to inspect

    Returns:
        none
    """
    if _inspect.isPackage(var):
        raise Exception("Packages have no methods")
    if _inspect.isClass(var) or _inspect.isObject(var):
        return [ "%s"%i for i  in var.__javaclass__.getConstructors()]
    return None

def methods(var):
    """ Print a list of all public methods.

    Args:
        var (java class or object): object to inspect

    Returns:
        none
    """
    if _inspect.isPackage(var):
        raise Exception("Packages have no methods")
    if _inspect.isClass(var) or _inspect.isObject(var):
        return [ "%s"%i for i  in var.__javaclass__.getMethods()]
    return None

def fields(var):
    """ Print a list of all public fields.

    Args:
        var (java class or object): object to inspect

    Returns:
        none
    """
    if _inspect.isPackage(var) or (not _inspect.isClass(var) or not _inspect.isObject(var)):
        print("Not java object")
        return
    if _inspect.isPackage(var):
        raise Exception("Packages have no methods")
    if _inspect.isClass(var) or _inspect.isObject(var):
        return [ "%s"%i for i  in var.__javaclass__.getClassFields()]
    return None

# Class<T> is hard to obtain
def getClass(name):
    """ Get the java Class<> for a name.

    This is the same a pkg.className.__javaclass__

    Args:
        name (str): class name

    Returns:
        a java Class<> object
    """
    c=_common.imports(name)
    if not _inspect.isClass(c):
        print('Not a class description')
        return None
    return c.__javaclass__


