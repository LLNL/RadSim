function Y=interpNCS(spline, X)

Y=zeros(size(X));

for i=1:prod(size(X))
  % figure out which segment we are on
  i0=find(X(i)<spline.x,1);
  if (i0==1)
    % prespline
    Y(i)=spline.y(1);
  elseif (isempty(i0))
    % postspline
    Y(i)=spline.y(end);
  else
    i0=i0-1;
    h=spline.x(i0+1)-spline.x(i0);
    Y(i)=(spline.z(i0+1)*(X(i)-spline.x(i0)).^3 + spline.z(i0)*(spline.x(i0+1)-X(i))^3)/6/h + ...
         (spline.y(i0+1)/h -h/6*spline.z(i0+1))*(X(i)-spline.x(i0)) + ...
         (spline.y(i0)/h   -h/6*spline.z(i0))  *(spline.x(i0+1)-X(i));
  end
end
