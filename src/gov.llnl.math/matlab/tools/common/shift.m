function out=shift(x,i)

if (i==0)
  out=x;
  return;
end
out=zeros(size(x));

if (isempty(size(x)))
  return;
elseif (all(size(x)==[1 1]))
  return;
end

if (i<0)
  if (size(x,1)==1)
    n=size(x,2);
    out=[x(1-i:n) zeros(1,min(-i,n))];
  else
    n=size(x,2);
    m=size(x,1);
    out=[ x(1-i:m,:); zeros(min(-i,m),n) ]; 
  end
else
  if (size(x,1)==1)
    n=size(x,2);
    out=[zeros(1,min(i,n)) x(1:n-i)];
  else
    n=size(x,2);
    m=size(x,1);
    out=[zeros(min(i,m),n); x(1:m-i,:)];
  end
end
