function cc=ar2cc(ar,N)
% cc=ar2cc(ar,N) - Linear predictor to Cepstrum coeffients.
%   N is the order of the conversion

% make column vector
if (isvector(ar))
  ar=reshape(ar,prod(size(ar)),1);  
end
[p,M]=size(ar);

% handle optional arguments
if (nargin<2)
  N=p-1;
end

% reformat the input
if (any(ar(1,:)~=1))
  ar=ar./ar(ones(p,1),:);
end
ar=-ar(2:p,:);
cc=ones(N,M);
p=min(p-1,N);

% perform recursion
cc(1,:)=ar(1,:);
for n=2:p
  cc(n,:)=ar(n,:)+[1:n-1]*(cc([1:n-1],:).*ar([n-1:-1:1],:))/n; 
end
if (N>p)
  for n=p+1:N
    cc(n,:)=(n+[-p:-1])*(cc(n+[-p:-1],:).*ar([p:-1:1],:))/n;
  end
end
