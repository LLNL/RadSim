function out=yaxis(ya);
%  YAXIS  control range of y-axis of a graph
%    YAXIS([x1 x2]) set the y-axis range to (x1,x2)
%      optionally returns the previous axis.
%
%    YA=YAXIS return the current y-axis range.
%
a=axis;
if (nargin==1)
  na=a;
  na(3:4)=ya;
  axis(na);
end

if (nargout>0)
  out=a(3:4);
end

