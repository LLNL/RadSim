function Y=filtercol(B,A,X);

Y=zeros(size(X));

if (size(B)==[1 1])
  for i=1:size(X,2)
    Y(:,i)=filter(B,A(:,i),X(:,i));
  end
elseif (size(A)==[1 1])
  for i=1:size(X,2)
    Y(:,i)=filter(B(:,i),A,X(:,i));
  end
else
  for i=1:size(X,2)
    Y(:,i)=filter(B(:,i),A(:,i),X(:,i));
  end
end

