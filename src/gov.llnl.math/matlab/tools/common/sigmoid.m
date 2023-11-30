function out=sigmoid(in,w,offset,center);

if (nargin<2) w=1; end
if (nargin<3) offset=0; end
if (nargin<4) center=0; end
out=1./(1+exp(-w*(in-center)))-0.5+offset;
