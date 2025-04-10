
""" Functions for loading files.
"""

import os
import numpy as np
import xml.etree.ElementTree as ET
import startJVM
import java
import org
import gov

def asJavaPath(pathString):
    """ Expand and resolve python string path, 
        return as java.nio.file.Path
    """
    resolvedPath = os.path.realpath(os.path.expanduser(pathString))
    return java.nio.file.Path.of(resolvedPath)

def loadSpectraListFile(pathString):
    """ Reads in and returns spectra list data from .xml.gz file.
    """
    javaPath = asJavaPath(pathString)
    reader = gov.llnl.utility.xml.bind.DocumentReader.create(gov.llnl.rtk.data.SpectraList)
    return reader.loadFile(javaPath)

def loadBackgroundFile(pathString=None):
    """ Reads in and returns (mean, basis) from .xml file.
        ``pathString`` is optional.
        mean:  gov.llnl.rdak.data.DoubleSpectrumTemplate
        basis: gov.llnl.rdak.internal.backgroundestimator.StaticBackgroundBasis
    """
    if pathString is None:
        pathString = os.path.join(os.path.dirname(__file__), 'mjx', 'd3s_background.xml')
    javaPath = asJavaPath(pathString)
    reader = gov.llnl.utility.xml.bind.DocumentReader.create(gov.llnl.rdak.mjx.tools.ExtractBackground)
    background = reader.loadFile(javaPath)
    
    mean = gov.llnl.rdak.data.DoubleSpectrumTemplate(background.getBackground())
    energyScale = mean.getSpectrum().getEnergyScale()
    basis = background.getBasisEstimator()
    basis.applyEnergyScale(energyScale)
    return mean, basis

def loadMatchedFilterFile(pathString=None):
    """ Reads in and returns matched filter (templates[], ids[]) from .xml file.
        ``pathString`` is optional.
        templates[]: array of gov.llnl.rdak.data.SpectralTemplateLinear
        ids[]:       array of python strings
    """
    if pathString is None:
        pathString = os.path.join(os.path.dirname(__file__), 'mjx', 'd3s_mf.xml')
    tree = ET.parse(pathString)
    root = tree.getroot()
    elements = root.findall('{http://rdak.llnl.gov}template')
    templateElements = [e for e in elements if e.attrib['id'].startswith('mf.Filters.template-')]
    
    mfTemplates = []
    mfIds = []
    reader = gov.llnl.rdak.data.SpectralTemplateReader()
    for element in templateElements:
        attrib = org.xml.sax.helpers.AttributesImpl()
        attrib.addAttribute('', '', 'type',  '', element.attrib['type'])
        attrib.addAttribute('', '', 'scale', '', element.attrib['scale'])
        attrib.addAttribute('', '', 'label', '', element.attrib['label'])
        attrib.addAttribute('', '', 'id',    '', element.attrib['id'])
        template = reader.start(gov.llnl.utility.internal.xml.bind.ReaderContextImpl(), attrib)
        template.data = gov.llnl.utility.ArrayEncoding.decodeDoubles(element.text)
        mfTemplates.append(template)
        mfIds.append(element.attrib['id'])
    sortidx = np.argsort([int(_id.split('-')[-1]) for _id in mfIds])
    mfTemplates = (np.array(mfTemplates)[sortidx]).tolist()
    mfIds = (np.array(mfIds)[sortidx]).tolist()
    return mfTemplates, mfIds
