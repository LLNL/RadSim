function ar=cc2ar(cc,N)


% This recursion is correct but in other ways wrong.  
% The proper way to terminate an lpc short is to take the 
% cc to an lpc of the same length, then to PARCOR parameters and
% then truncate it.  Otherwise the resulting sequence may be unstable.
%

% make column vector
if (isvector(cc))
  cc=reshape(cc,prod(size(cc)),1);  
end
[p,M]=size(cc);

% handle optional arguments
if (nargin<2)
  N=p;
end

% reformat the input
ar=ones(N,M);
p=min(p,N);

% perform recursion
ar(1,:)=cc(1,:);
for n=2:p
  ar(n,:)=cc(n,:)-[1:n-1]*(cc([1:n-1],:).*ar([n-1:-1:1],:))/n; 
end
if (N>p)
  for n=p+1:N
    ar(n,:)=-(1:p)*(cc(1:p,:).*ar(n-[1:p],:))/n;
  end
end

ar=[ones(1,M);-ar];
