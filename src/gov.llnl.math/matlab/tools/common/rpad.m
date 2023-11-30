function out=fpad(in,n);

if (ismatrix(in))
  out=[in;zeros(n,size(in,2))];
else
  if (size(in,1)<size(in,2))
    out=[in zeros(1,n)];
  else
    out=[in;zeros(n,1)];
  end
  if (n<0)
    out=out(1:length(out)+n);
  end
end

