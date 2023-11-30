function out=unwindow(in,w)

N=size(in,1);
if (nargin<2)
  w=hamming(N);
elseif (ischar(w))
  eval(sprintf('w=%s(N);',w));
elseif (isvector(w))
  if (size(w,2)~=N)
    w=w.';
  end
elseif (size(w)==[0 0])
  out=in;
  return
elseif (size(w)==[1 1])
  out=in./w;
  return
end
out=in./(w*ones(1,size(in,2)));

