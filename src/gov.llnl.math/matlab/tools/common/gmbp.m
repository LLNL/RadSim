function y=gmf(x,fc,fs,alpha);

b=-cos(2*pi*fc/fs);
if (nargin<4)
  a=0.95;
else
  a=alpha;
end
if (nargout<1)
  freqz((1-a)/2*[1 0 -1],[1 (1+a)*b a],2^16,fs);
else
  y=filter((1-a)/2*[1 0 -1],[1 (1+a)*b a],x);
end
