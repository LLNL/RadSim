import jpype as _jp
import jvm as _jvm
import numpy as _np
import types as _types
from gov.llnl.math import DoubleArray as _da

# Get the DoubleArray Class
#_da=_jvm.forName("gov.llnl.math.DoubleArray")

#%% Augment double[]

# FIXME figure out how to make shape a property
def _shape1(self):
    return (len(self),)

def _unpack1(self):
    return self[:]

def _add1(self, other):
    if isinstance(other, int):
        other=float(other)
    return _da.add(self, other)

def _iadd1(self, other):
    if isinstance(other, int):
        other=float(other)
    return _da.addAssign(self, other)

def _fill1(self, other):
    return _da.fill(self, float(other))

def _mult1(self, other):
    if isinstance(other, int):
        other=float(other)
    return _da.multiply(self, other)

def _imult1(self, other):
    if isinstance(other, int):
        other=float(other)
    return _da.multiplyAssign(self, other)

def _dot1(self, other):
    return _da.multiplyInner(self, other)

class _Double1ArrayCustomizer(object):
    def canCustomize(self, name, jc):
        if name == 'double[]':
            return True
        return False

    def customize(self, name, jc, bases, members):
        members['__add__']=_add1
        members['__iadd__']=_iadd1
        members['__mul__']=_mult1
        members['__imul__']=_imult1
        members['shape'] = property(_shape1, None)
        members['unpack'] = _unpack1
        members['fill']=_fill1
        members['dot']=_dot1

_jp.registerArrayCustomizer(_Double1ArrayCustomizer())

#%% Augment double[][]
def _shape2(self):
    d1=len(self)
    if d1==0  or self[0]==None:
        d2=0
    else:
        d2=len(self[0])
    return (d1, d2)

def _unpack2(var, transpose=False):
    d=var.shape
    if transpose:
        out=_np.zeros([d[1], d[0]])
        for i in range(0, d[0]):
            if isinstance(var[i],type(None)):
                continue
            out[:,i]=var[i][:]
        return out
    else:
        out=_np.zeros(d)
        for i in range(0, d[0]):
            if isinstance(var[i],type(None)):
                continue
            out[i]=var[i][:]
        return out

class _Double2ArrayCustomizer(object):
    def canCustomize(self, name, jc):
        if name == 'double[][]':
            return True
        return False

    def customize(self, name, jc, bases, members):
        members['shape'] = property(_shape2,None)
        members['unpack'] = _unpack2

_jp.registerArrayCustomizer(_Double2ArrayCustomizer())

#%% Convert implementation
def to(var):
    """ Convert to a java double[] """
    if isinstance(var, _np.ndarray):
        return _jp.JArray(_jp.JDouble,1)(var.tolist())
    if isinstance(var, list):
        return _jp.JArray(_jp.JDouble,1)(var)
    if _jvm.isArray(var) and _jvm.getTypeName(var)=="double[]":
        return var;
    raise Exception("Can't find conversion for %s to double[]"%(type(var)))


#%% Merge implementations
def cat(*var):
    """ Convert a list of items into java double[]

    Merges all parameters into one dimensional array.

    Args:
        *var (java double[], array, list)

    Returns:
        java double[] array containing all elements.
    """
    # Convert the arrays
    collection=[]
    sz=0
    for entry in var:
        array=to(entry)
        sz=sz+len(array)
        collection.append(array)

    # Trivial case
    if len(collection)==1:
        return collection[0]

    # Merge the arrays
    out=_jvm.new.Doubles(sz)
    sz=0
    for entry in collection:
        _da.assign(out, sz, entry, 0, len(entry))
        sz=sz+len(entry)
    return out

