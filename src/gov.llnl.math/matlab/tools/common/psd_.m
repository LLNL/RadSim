function [Pxx]=psd(in,Nfft,Fs,win,over);

if (nargin<2 | isempty(Nfft) ) Nfft=256; end
if (nargin<3 | isempty(Fs) )   Fs=2; end
if (nargin<4 | isempty(win) )  win=Nfft; end
if (nargin<5 | isempty(over) ) over=floor(Nfft/2); end

% In terms of OS:DTSP
L=Nfft;  % length of segment
R=over;
w=window_func(Nfft,win);
U=1/L*sum(w.^2);

X=fft(window(block(in,L,L-R),w),L);
K=size(X,2);

Pxx=sum(abs(X).^2,ndims(X))/L/U/K;
f=[0:Nfft-1]/Nfft*Fs;

if (isreal(in))
  N=floor((Nfft+1)/2)+mod(Nfft+1,2);
  Pxx=Pxx(1:N,:,:);
  f=f(1:N);
end

if (nargout<1)
  plot(f,dbp(Pxx));
  grid on;
  xlabel('Frequency');
  ylabel('Power Spectrum Magnitude (dB)');
  clear Pxx;
end



