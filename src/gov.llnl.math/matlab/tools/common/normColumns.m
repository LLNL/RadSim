function out=normCol(in,L);

if ~exist('L')
  L=1;
end

if (L==0)
  sm=max(in);
  size(sm)
elseif (L==1)
  sm=sum(in,1);
elseif (L==2)
  sm=sqrt(sum(in.^2,1));
end

sm(find(sm==0))=1;
out=in.*(ones(size(in,1),1)*(1./sm));


