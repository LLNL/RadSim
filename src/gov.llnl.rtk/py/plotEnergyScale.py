import matplotlib as _mpl
import matplotlib.pyplot as _plt
import matplotlib.mlab as _mlab

import jvm as _jvm
import numpy as _np
import matplotlib as _mpl
import matplotlib.pyplot as _plt
import matplotlib.mlab as _mlab

def plotEnergyScale(scales, *args):
    handles=[]
    options=_parseOptions(args)
    if options['mode']==0:
        if isinstance(scales, list):
            es=scales[0]
            for scale in scales:
                handles.append(_plotEnergyScaleEfficiency(scale, options))
        else:
            es=scales
            handles.append(_plotEnergyScaleEfficiency(scales, options))
        if options['label']==True:
            _labelEfficiency(es)
        _plt.xlabel('Energy (keV)')
        _plt.ylabel('Channel/Energy')

    elif options['mode']==1:
        print("Not supported")
#         if isinstance(scales, list):
#            es=scales[0]
#            for scale in scales:
#                handles.append(_plotEnergyScaleRelative(scale, options))
#        else:
#            es=scales
#            handles.append(_plotEnergyScaleRelative(scales, options))

    elif options['mode']==2:
        print("Not supported")

#if options.mode==2
#  for uu=1:length(energyScales)
#    energyScale=energyScales(uu);
#    plotEnergyScaleQuadratic(energyScale, options);
#    hold on;
#  end
#end


#%%
from collections import deque as _dq
def _parseOptions(args):
    options={}
    options['label']=False
    options['knots']=False
    options['mode']=0
    options['percent']=0

    args=_dq(args)
    while len(args)>0:
        arg=args.popleft()
      # Place labels on the plot
        if arg=='label':
            options['label']=True
      # show the reference points (optional)
        elif arg=="knots" or arg=="points":
            options['knots']=True

        elif arg=='relative':
            options.mode=1
            options.reference=args.popleft()
        elif arg=='percent':
            options['percent']=True

        elif arg=='quadratic':
            options['mode']=2

        else:
            print("unknown option "+arg)

    return options

#%%
def _plotEnergyScaleEfficiency(energyScale, options):
    energies=energyScale.getEnergyBins().toArray()
    channels=_jvm.doubles(_np.arange(0, len(energies)))

    # find first greater than
    gt=_jvm.forName('gov.llnl.math.DoubleConditional$GreaterThan')(40.0)
    da=_jvm.forName('gov.llnl.math.DoubleArray')
    i0=da.findIndexOfFirstRange(energies, 0, len(energies), gt)

    # Remove low energy
    energies=da.copyOfRange(energies, i0, len(energies))
    channels=da.copyOfRange(channels, i0, len(channels))
    gain=da.copyOf(channels)
    da.divideAssign(gain, energies)

    h,=_plt.semilogx(energies, gain, '-')

    #Show the knot points
    if (options['knots']==1):
        energies=energyScale.getPairs().getEnergies()
        gain=da.copyOf(energyScale.getPairs().getChannelsEdge())
        da.divideAssign(gain, energies)
        h2,=_plt.plot(energies, gain, '.')
        h2.set_color(h.get_color())
    return h



#%%
def _labelEfficiency(energyScale):
#    ax=axis()
#    dy=(ax(4)-ax(3))*0.05
    for pair in energyScale.getPairs():
        if pair.getLabel()==None:
            continue

        channels = pair.getChannel()
        energy = pair.getEnergy()
        gain=channel/energy

        kk={'x1':energy, 'y1':gain,
            'x2':energy*1.05, 'y2':gain+dy,
            'label':pair.getLabel()}
        out.append(kk)
    _labelEnergyScale(out)

def _labelEnergyScale(out):
    print('Not implemented')


#%%
#def plotEnergyScaleRelative(energyScale, options):
#    channels=[0.5:1:4096]
#    energies=energyScale.getEnergyBins().getCenters()
#    energiesR=reference.getEnergyBins().getCenters()
#
#    i0=find(energies>40)
#    energiesR=energiesR(i0)
#    energies=energies(i0)
#    channels=channels(i0)
#
#    if options.percent==0:
#        semilogx(energies, (energies-energiesR),'-');
#        xlabel('Energy')
#        ylabel('Delta Energy')
#    else:
#        semilogx(energies, 100*(energies-energiesR)./energiesR,'-')
#        xlabel('Energy')
#        ylabel('Delta Energy %');
#        axis([30 3000 -5 5])
#

#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
#function plotEnergyScaleQuadratic(energyScale, options)
#  channels=[0.5:1:4096]';
#  energies=energyScale.getEnergyBins().getCenters();
#
#  energiesP=energyScale.getPairs().getEnergies();
#  channelsP=energyScale.getPairs().getChannels();
#  energiesP=energiesP(1:end-1);
#  channelsP=channelsP(1:end-1);
#
#  A=[channelsP.^2 channelsP.^1 channelsP.^0];
#  k=A\energiesP;
#  energies2=polyval(k, channels);
#
#  i0=find(energies>40);
#  energies2=energies2(i0);
#  energies=energies(i0);
#  channels=channels(i0);
#
#  if (options.percent==0)
#    gain=channels./energies;
#    semilogx(energies,gain,'-');
#    hold on;
#
#    gain=channels./energies2;
#    semilogx(energies2,gain,'-');
#    legend('Actual','Quadratic');
#    xlabel('Energy (keV)');
#    ylabel('Channel/Energy');
#  else
#    semilogx(energies, 100*(energies2-energies)./energies,'-');
#    xlabel('Energy')
#    ylabel('Delta Energy %');
#    yaxis([-5 5])
#  end
#  xaxis([30 3000])

