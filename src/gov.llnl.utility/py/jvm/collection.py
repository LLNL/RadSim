import jpype as _jp
import jvm as _jvm

def toArray(var):
    """ Convert a collection into an array with the type of the first
    element.

    Args:
        var is a Java Collection<T>.

    Returns:
        a new Java array of T[]
    """
    info=_jvm.getArrayInfo(var.iterator().next())
    info2=(info[0], info[1]+1)
    return var.toArray(_jp.JArray(*info2)(0))

