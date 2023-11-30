function out=isvector(in)

out=1;
if (size(in,1)==1&size(in,2)>1)
  return
elseif (size(in,2)==1&size(in,1)>1)
  return
end
out=0;
