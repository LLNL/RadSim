function out=xcorgram(x,T,N,M,w);
% XCORGRAM produces a graph of segmented cross correlation 
%  T number of terms to display (default 32)
%  N size of window, can be [] (default 256)
%  M step of window, can be [] (default N/2)
%  w window function, can be [], vector 1xN or name for window function
%      like 'hamming'  (default hamming)
%


%% arguments
units='samples';

if (nargin<2 | size(T)==[0 0])
   T=32;
end
if (nargin<3 | size(N)==[0 0])
   N=256;
end
if (nargin<4 | size(M)==[0 0])
   M=N/2;
end
if (nargin<5)
   w='hanning';
end


%
if (~isvector(x))
   error('X must be a vector\n');
end

if (size(x,1)<size(x,2))
   x=x.';
end

cor=xcorr_(window(block(x,N,M)),T,'auto'); %,'coef');

if (nargout==0)
   w=[-T:T];
   nc=[1:size(cor,2)]*M;
   imagesc(nc,w,20*log10(abs(cor)+0.1));
   set(gca,'YDir','normal')
   xlabel(sprintf('Time (%s)',units));
   ylabel(sprintf('Delay (%s)',units));
else
   out=cor;
end
