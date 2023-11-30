# This file holds all of patches required to get jpype to work
import jpype as _jp

# Patch the Array types to have __name__
class _NameArrayCustomizer(object):
    def canCustomize(self, name, jc):
        return True
    def customize(self, name, jc, bases, members):
        members['__name__']=name
_jp.registerArrayCustomizer(_NameArrayCustomizer())

def _objectSetAttr(self, attr, value):
    if attr.startswith('_') \
           or callable(value) \
           or isinstance(getattr(self.__class__, attr), property):
        object.__setattr__(self, attr, value)
    else:
        raise AttributeError("%s does not have field %s"%(self.__name__, attr), self)

# Patch the Class types for __setattr__
class _SetAttrClassCustomizer(object):
    def canCustomize(self, name, jc):
        return True
    def customize(self, name, jc, bases, members):
        members['__setattr__']=_objectSetAttr

_jp.registerClassCustomizer(_SetAttrClassCustomizer())


