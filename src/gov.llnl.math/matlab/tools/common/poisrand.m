function out=poisrand(m, s);
% poisrand(m, s)
%   m is the number expected in this period 

if nargin<2 
	out=zeros(size(m));
	for i1=1:prod(size(m))
		out(i1)=prand(m(i1));
	end
	return
else
	if (m>70)
		out=round(m^0.5*randn(s)+m);
	else
		out=zeros(s);
		for i1=1:prod(size(out))
			out(i1)=prand(m(1));
		end
		return
	end
end

function out=prand(m);
% generate exponential random events and then count the number that occur
% in an interval.

if (m>70)
	out=round(m^0.5*randn(1)+m);
  return
end

% was   -m > sum log(1-rand)    becomes  exp -m < prod 1-rand
out=0;
g=exp(-m);
R=cumprod(rand(floor(m+1)*2,1));
while (R(end)>g)
  out=out+length(R);
  R=R(end)*cumprod(rand(floor(m/2)+2,1));
end
out=out+length(find(R>g));
  
