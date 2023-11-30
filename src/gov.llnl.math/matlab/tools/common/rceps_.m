function out=rceps_(in,n);

if (nargout==0)
   return
end

if (nargin==1)
   out=real(ifft(log(abs(fft(in)))));
elseif (nargin==2)
   out=real(ifft(log(abs(fft(in,n)))));
end

