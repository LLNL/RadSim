function out=lpcgram(x,T,N,M,w);
% LPCGRAM produces a graph of segmented cross correlation 
%  T number of terms to display [ar,cc] (default 32)
%  N size of window, can be [] (default 256)
%  M step of window, can be [] (default N/2)
%  w window function, can be [], vector 1xN or name for window function
%      like 'hamming'  (default hamming)
%


%% arguments
units='samples';

if (nargin<2 | size(T)==[0 0])
   arT=32;
   ccT=32;
else
   if (length(T)>1)
     arT=T(1);
     ccT=T(2);
   else
     arT=T;
     ccT=T;
   end
end
if (nargin<3 | size(N)==[0 0])
   N=256;
end
if (nargin<4 | size(M)==[0 0])
   M=N/4;
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

ar=lpc_(window(block(x,N,M,'nopad')),arT);
cc=ar2cc(ar,ccT);

if (nargout==0)
   w=[1:ccT];
   nc=[1:size(cc,2)]*M;
   sc=[1:ccT]'.^0.5;
   imagesc(nc,w,0.8*cc.*sc(1:ccT,ones(1,size(cc,2))));
   ydir normal;
   xlabel(sprintf('Time (%s)',units));
   %ylabel(sprintf('Delay (%s)',units));
else
   out=cc;
end
