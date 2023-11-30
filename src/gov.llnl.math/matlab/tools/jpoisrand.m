function out=jpoisrand(in);

global prand;
if isempty(prand)
  import gov.llnl.math.random.*;
  prand=PoissonRandom();
end

out=double(prand.draw(in(:)));

if any(size(out)~=size(in))
  out=reshape(out,size(in));
end

