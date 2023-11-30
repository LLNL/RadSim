function out=xaxis(xa);
%  XAXIS  control range of x-axis of a graph
%    XAXIS([x1 x2]) set the x-axis range to (x1,x2)
%      optionally returns the previous axis.
%
%    XA=XAXIS return the current x-axis range.
%
a=axis;
if (nargin==1)
  na=a;
  na(1:2)=xa;
  axis(na);
end

if (nargout>0)
  out=a(1:2);
end

