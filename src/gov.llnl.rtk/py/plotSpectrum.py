import matplotlib as _mpl
import matplotlib.pyplot as _plt
import matplotlib.mlab as _mlab
import jvm as _jvm
import numpy as _np
import matplotlib as _mpl
import matplotlib.pyplot as _plt
import matplotlib.mlab as _mlab
from collections import deque as _dq

#%% Main
def plotSpectrum(spectra, *args):
    handles=[]
    options=_parseOptions(args)
    if isinstance(spectra, list):
        for spectrum in spectra:
            handles.append(_plotSpectrum(spectrum, options))
    else:
        handles.append(_plotSpectrum(spectra, options))
    _plt.xlabel('Channel')
    _plt.ylabel('Counts')


#%% Options
def _parseOptions(args):
    options={}
    options['label']=False
    options['knots']=False
    options['mode']=0
    options['percent']=0

    args=_dq(args)
    while len(args)>0:
        arg=args.popleft().lower()
#      # Place labels on the plot
#        if arg=='label':
#            options['label']=True
#      # show the reference points (optional)
#        elif arg=="knots" or arg=="points":
#            options['knots']=True
#
#        elif arg=='relative':
#            options.mode=1
#            options.reference=args.popleft()
#        elif arg=='percent':
#            options['percent']=True
#
#        elif arg=='quadratic':
#            options['mode']=2
#
#        else:
        print("unknown option "+arg)

    return options

#%% Plot
def _plotSpectrum(spectrum, options):
    hi,=_plt.semilogy(spectrum.toDoubles())

