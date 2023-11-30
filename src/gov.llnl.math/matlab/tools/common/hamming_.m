function out=hamming_(N)
% hamming window
% (for machines without signal library)

out=0.54-0.46*cos(2*pi*[0:N-1]'/(N-1));

