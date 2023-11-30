function plotEnergyScale(energyScales, varargin)
% Plot an energy scale

options.label=0;
options.knots=0;
options.mode=0;
options.percent=0;
options.reference=[];
if (nargin>1)
  i=1;
  while i<=length(varargin)
    switch lower(varargin{i})
% For gain view
      % Place labels on the plot
      case 'label'
        options.label=1;
      % show the reference points (optional)
      case {'knots', 'points'}
        options.knots=1;

% For relative view
      case 'relative'
        i=i+1;
        options.mode=1;
        options.reference=varargin{i};
      case 'percent'
        options.percent=1;

% For quad view
      case 'quadratic'
        options.mode=2;
      otherwise
        disp('Unknown option');
    end
    i=i+1;
  end
end

washold=0;
%washold=ishold;

if (options.mode==0)
  for uu=1:length(energyScales)
    energyScale=energyScales(uu);
    plotEnergyScaleEfficiency(energyScale, options);
    hold on;
  end
  if options.label
    labelEfficiency(energyScale);
  end
  xaxis([30 3000])

  %plot(centers,(0.5:1:centers.length)'./centers,'LineWidth',2);
  xlabel('Energy (keV)');
  ylabel('Channel/Energy');
end

if options.mode==1
  for uu=1:length(energyScales)
    energyScale=energyScales(uu);
    plotEnergyScaleRelative(energyScale, options);
    hold on;
  end
end

if options.mode==2
  for uu=1:length(energyScales)
    energyScale=energyScales(uu);
    plotEnergyScaleQuadratic(energyScale, options);
    hold on;
  end
end

% Restore the hold
if washold
  hold on;
else
  hold off;
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
function plotEnergyScaleEfficiency(energyScale, options)
  channels=[0:1:4096]';
  energies=energyScale.getEnergyBins().toArray();
  i0=find(energies>40);
  energies=energies(i0);
  channels=channels(i0);
  gain=channels./energies;
  h=semilogx(energies,gain,'-');

  %Show the knot points
  if (options.knots==1)
    energiesP=energyScale.getPairs().getEnergies();
    channelsP=energyScale.getPairs().getChannelsEdge();
    i0=find(energiesP>40);
    h2=line(energiesP(i0), channelsP(i0)./energiesP(i0), 'LineStyle', 'none','Marker','.','MarkerSize',10,'Color',h.Color);
    h2.Annotation.LegendInformation.IconDisplayStyle='off';
  end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
function labelEfficiency(energyScale)
  ax=axis();
  dy=(ax(4)-ax(3))*0.05;
  pairs=energyScale.getPairs();
  iter=pairs.iterator();
  i=1;
  R1=zeros(pairs.size,2);
  R2=zeros(pairs.size,2);
  labels={};
  energiesP=energyScale.getPairs().getEnergies();
  channelsP=energyScale.getPairs().getChannelsEdge();
  while (iter.hasNext())
    pair=iter.next;
    if isempty(pair.getLabel())
      continue;
    end
    energy=energiesP(i);
    channel=channelsP(i);
    gain=channel/energy;

    R1(i,:)=[energy, gain];
    R2(i,:)=[energy*1.05, gain+dy];
    labels{end+1}=char(pair.getLabel());
    i=i+1;
  end
  labelEnergyScale(R1,R2,labels);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
function plotEnergyScaleRelative(energyScale, options)    
  channels=[0.5:1:4096]';
  energies=energyScale.getEnergyBins().getCenters();
  energiesR=reference.getEnergyBins().getCenters();

  i0=find(energies>40);
  energiesR=energiesR(i0);
  energies=energies(i0);
  channels=channels(i0);

  if (options.percent==0)
    semilogx(energies, (energies-energiesR),'-');
    xlabel('Energy')
    ylabel('Delta Energy');
  else
    semilogx(energies, 100*(energies-energiesR)./energiesR,'-');
    xlabel('Energy')
    ylabel('Delta Energy %');
    axis([30 3000 -5 5])
  end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
function plotEnergyScaleQuadratic(energyScale, options)    
  channels=[0.5:1:4096]';
  energies=energyScale.getEnergyBins().getCenters();
 
  energiesP=energyScale.getPairs().getEnergies();
  channelsP=energyScale.getPairs().getChannels();
  energiesP=energiesP(1:end-1);
  channelsP=channelsP(1:end-1);

  A=[channelsP.^2 channelsP.^1 channelsP.^0];
  k=A\energiesP;
  energies2=polyval(k, channels);

  i0=find(energies>40);
  energies2=energies2(i0);
  energies=energies(i0);
  channels=channels(i0);

  if (options.percent==0)
    gain=channels./energies;
    semilogx(energies,gain,'-');
    hold on;

    gain=channels./energies2;
    semilogx(energies2,gain,'-');
    legend('Actual','Quadratic');
    xlabel('Energy (keV)');
    ylabel('Channel/Energy');
  else
    semilogx(energies, 100*(energies2-energies)./energies,'-');
    xlabel('Energy')
    ylabel('Delta Energy %');
    yaxis([-5 5])
  end
  xaxis([30 3000])

