function ar=rf2ar(rf,N);
% RF2AR converts reflection coefficents to autoregressive LPC coefficients
%   AR=rf2ar(FR) can be used to compute the LPC from a set 
%   of PARCOR.  This is useful to truncate a LPC sequence to a
%   specific error length without recomputing the durbin algorithm.
%

% make column vector
if (isvector(rf))
  rf=reshape(rf,prod(size(rf)),1);
end
[p,M]=size(rf);
if (nargin<2)
  N=p;
end

ar=zeros(N,M);

for i=1:min(p,N)
  ar(i,:)=rf(i,:);
  k=rf(i,:);
  ar(1:i-1,:)=ar(1:i-1,:)-k(ones(i-1,1),:).*ar(i-[1:i-1],:);
end

ar=[ones(1,M); -ar];
