function out=exprand(m, s)

if nargin<2 
  s=1;
end

out=-m*log(rand(s));
