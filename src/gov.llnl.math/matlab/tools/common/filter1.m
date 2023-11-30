function y=filter1(f,x,delta);

if (nargin<3 | isempty(delta)) delta=0; end
if isvector(x)
  N=length(x);
  x=rpad(x,max(length(f),delta));
  y=filter(f,1,x);
  y=y(delta+(1:N));
else
  N=size(x,1);
  x=rpad(x,max(length(f),delta));
  y=filter(f,1,x);
  y=y(delta+(1:N),:);
end

