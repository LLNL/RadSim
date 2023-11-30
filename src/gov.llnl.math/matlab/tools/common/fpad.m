function out=fpad(in,n);

if (isvector(in))
  if (size(in,1)>size(in,2))
    out=[zeros(n,1);in];
  else
    out=[zeros(1,n) in];
  end
elseif (ismatrix(in))
  out=[zeros(n,size(in,2));in];
end
