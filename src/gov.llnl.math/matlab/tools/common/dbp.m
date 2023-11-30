function out=db(in,mn)
if (nargin<2)
  mn=-inf;
end
 
out=max(10*log10(abs(in)),mn);
