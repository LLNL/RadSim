function plotSpectrum(spectra, varargin)
% Plot an energy scale

rate=0;
if (nargin>1)
  i=1;
  while i<=length(varargin)
    switch lower(varargin{i})
% For gain view
      % Place labels on the plot
      case 'rate'
        rate=1;
       otherwise
        disp('Unknown option');
    end
    i=i+1;
  end
end

washold=ishold;

for i=1:length(spectra)
  spectrum=spectra(i);
  data=spectrum.toDoubles();
  if (rate==1)
    data=data/spectrum.getLiveTime();
  end
  n=length(data);
  data(n)=0;
  eb=spectrum.getEnergyBins();
  if ~isempty(eb)
    e0=eb.getCenters();
  else
    e0=[0:n-1]';
  end

  %Chunk of code used when debugging a peak search for Th232
  %u1=(e0/n).*data;
  %u2=filter([1 -1],1,u1);
  %u3=filter(1,[1 -0.98], u2);
  %u4=flipud(filter(1,[1 -0.98], flipud(u3)));
  semilogy(e0, data );
  hold on;
end

if washold==0
  hold off;
end
