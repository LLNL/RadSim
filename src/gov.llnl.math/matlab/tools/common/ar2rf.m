function rf=ar2rf(ar);
% lpc ar coef to reflection coef.

% make column vector
if (isvector(ar))
  ar=reshape(ar,prod(size(ar)),1);
end
[n,M]=size(ar);

% remove the constant term
if (any(ar(1,:)~=1))
  ar=ar./ar(ones(n,1),:);
end
ar=-ar(2:n,:);

rf=ar;
for i=size(rf,1):-1:1
  % cheap inverse
  k1=1./(1-rf(i,:).^2);
  k2=rf(i,:); 
  rf(1:i-1,:)=k1(ones(i-1,1),:).*(rf(1:i-1,:)+k2(ones(i-1,1),:).*rf(i-1:-1:1,:));
end

