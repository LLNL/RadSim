function out=plusminus(N)
% Colormap to distinguish between positive, negative, and nuetral values

M=3;
if (nargin<1)
  [N,M]=size(colormap);
end
N=floor(N/2);
%out=exp(2*[ [0.9 0.9 1]; [ zeros(N+1,2) [N:-1:0]'/N ]; [ [1:N]'/N zeros(N,2) ]; [1 0.9 0.9] ])/exp(2);
%out=exp(2*[ [ zeros(N+1,2) [N:-1:0]'/N ]; [ [1:N]'/N zeros(N,2) ]; ])/exp(2);
out=exp(2*[ [ [N:-1:0]'/N zeros(N+1,2) ]; [  zeros(N,2) [1:N]'/N ]; ])/exp(2);


