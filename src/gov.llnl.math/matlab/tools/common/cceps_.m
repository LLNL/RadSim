function [out,nd]=cceps_(in,n);

%if (nargout==0)
%   return
%end
if (nargin==1)
   x=fft(in);
else
   x=fft(in,n);
end

if (ismatrix(in))
  n = size(x,1);
  y = unwrap(angle(x));
  nd=round((y(n,:)+y(2,:))/pi/2);
  y = y - 2*pi*([0:(n-1)]'*nd/n);
else
  n = length(x);
  y = unwrap(angle(x));
  nd=round((y(n)+y(2))/pi/2);
  if (size(y,2)==1)
    y = y - 2*nd*pi*([0:(n-1)]'/n);
  else
    y = y - 2*nd*pi*([0:(n-1)]/n);
  end
end
out=real(ifft(log(abs(x))+j*y));
