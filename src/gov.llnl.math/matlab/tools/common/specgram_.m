function out=specgram_(x,fc,Fs,w,dec);
% SPECGRAM produce a windowed fft view of a signal
%  out=SPECGRAM(x,fc,Fs,w,dec);
%
%   x  input vector
%   fc window size
%   Fs sample rate
%   w  window type
%   dec  skip ratio

%% arguments
if (nargin<2 | size(fc)==[0 0])
   fc=256;
end
if (nargin<3 | size(Fs)==[0 0])
   Fs=2;
end
if (nargin<4)
   w='hanning';
end
if (nargin<5)
   dec=floor(fc/2);
end


%
if (~isvector(x))
   error('X must be a vector\n');
end

if (size(x,1)<size(x,2))
   x=x.';
end

fcr=floor(fc/2)+1;
spec=fft(window(block(x,fc,dec),w));
spec=spec(1:fcr,:);
%x=[x;zeros(fc,1)];
%n=size(x,1);
%nr=floor((n-fc-1)/dec)+1;
%fcr=floor(fc/2);
%
%spec=zeros(nr,fcr);
%for i=0:nr-1
%   u=fft(x((1:fc)+i*dec));
%   spec(i+1,:)=u(1:fcr)';
%end
%
if (nargout==0)
   w=[0:fcr]/fcr*Fs/2;
   nc=[1:size(spec,2)]*dec;
   spec=abs(spec);
   mspec=mean(mean(spec));
   imagesc(nc,w,20*log10(spec+0.01*mspec));
   set(gca,'YDir','normal')
   xlabel('Time');
   ylabel('Freq');
else
   out=spec;
end
