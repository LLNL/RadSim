function out=poiscdf(u, v);
% poiscdf(u,v)
%   u is mean 
%   v is observation


if u<0
  out=0;
else
  out=1-gammainc(u,v+1);
end
