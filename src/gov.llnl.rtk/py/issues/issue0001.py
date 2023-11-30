#!/usr/bin/env python3

def _import():
    
    global _os
    import os as _os

    global _jpype
    import jpype as _jpype
    if not _jpype.isJVMStarted():
        _jpype.startJVM(classpath=[_os.path.join('..', '..', 'build', 'classes'),
                                   _os.path.join('..', '..', '..', 'gov.llnl.utility', 'build', 'classes'),
                                  ])
    import jpype.imports

    global _java, _gov
    import java as _java
    import gov as _gov

    global _np, _mpl, _plt
    import numpy as _np
    import matplotlib as _mpl
    import matplotlib.pyplot as _plt
    _mpl.rcParams['font.size'] = 18

def run():
    energyScale = _gov.llnl.rtk.data.EnergyScaleFactory.newLinearScale(0., 3000., 1024)

    energies = _np.linspace(0., 3000., _np.round(3000. * 3.14).astype(_np.int))
    channels = _np.linspace(0., 1024., _np.round(1024. * 3.14).astype(_np.int))

    en2ch2en = _np.zeros(energies.size)
    ch2en2ch = _np.zeros(channels.size)

    for i, energy in enumerate(energies):
        en2ch2en[i] = energyScale.getEnergyOfEdge( energyScale.findBin(energy) )

    for i, channel in enumerate(channels):
        ch2en2ch[i] = energyScale.findBin( energyScale.getEnergyOfEdge(channel) )

    fig, (axL, axR) = _plt.subplots(ncols=2, figsize=[19,6], tight_layout=True)
    axL.plot(energies[1:], (en2ch2en - energies)[1:] / energies[1:], 'b.')
    axR.plot(channels[1:], (ch2en2ch - channels)[1:] / channels[1:], 'b.')

    axL.set_title('Energy -> Channel -> Energy\ngetEnergyOfEdge( findBin( energy ) )')
    axL.set_xlabel('Original Energy')
    axL.set_ylabel('Relative Difference: (result - energy) / energy')

    axR.set_title('Channel -> Energy -> Channel\nfindBin( getEnergyOfEdge( channel ) )')
    axR.set_xlabel('Original Channel')
    axR.set_ylabel('Relative Difference: (result - channel) / channel')

    _plt.show(block=(__name__ == '__main__'))

_import()
if __name__ == '__main__':
    run()

