function out=db(in,mn)
if (nargin<2)
  mn=-inf;
end
 
out=max(20*log10(abs(in)),mn);
