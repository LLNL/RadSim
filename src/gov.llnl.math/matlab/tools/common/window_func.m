function [w,len]=window_func(N,w)

if (nargin<2)
  w=hanning(N);
  len=N;
elseif (ischar(w))
  eval(sprintf('w=%s(N);',w));
  len=N;
elseif (isvector(w))
  w=w(:);
  len=length(w);
elseif (size(w)==[0 0])
  w=ones(N,1);
  len=N;
elseif (size(w)==[1 1])
  len=w;
  w=hanning(w);
end

% always return the proper length
w=rpad(w,N-length(w));

