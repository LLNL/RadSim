function out=cepsgram(x,N,M,w,Fs);
% CEPSGRAM produces cepstrum equivelent of a spectragram
%  N size of window, can be [] (default 256)
%  M step of window, can be [] (default N/2)
%  w window function, can be [], vector 1xN or name for window function
%      like 'hamming'  (default hamming)
%
%  Cepsgram fails badly on non-causal sequences, use lpcgram.
%


%% arguments
units='s';
if (nargin<2 | size(N)==[0 0])
   N=256;
end
if (nargin<3 | size(M)==[0 0])
   M=N/2;
end
if (nargin<4)
   w='hanning';
end
if (nargin<5)
   units='samples';
   Fs=1;
end


%
if (~isvector(x))
   error('X must be a vector\n');
end

if (size(x,1)<size(x,2))
   x=x.';
end

Nr=floor(N/2)+1;
ceps=real(ifft(log(abs(fft(window(block(x,N,M,'nopad'),w))+0.1))));
ceps=ceps((1:Nr),:);

if (nargout==0)
   w=[0:Nr]/Fs;
   nc=[1:size(ceps,2)]*M/Fs;
   imagesc(nc,w,20*log10(abs(ceps)+0));
   set(gca,'YDir','normal')
   xlabel(sprintf('Time (%s)',units));
   ylabel(sprintf('Delay (%s)',units));
else
   out=ceps;
end
