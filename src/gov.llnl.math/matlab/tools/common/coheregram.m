function [Cxy,f]=coheregram(x,y,Nfft,Fs,win,over,quasiwin);

if (nargin<3 | isempty(Nfft) ) Nfft=256; end
if (nargin<4 | isempty(Fs) )   Fs=2; end
if (nargin<5 | isempty(win) )  win=Nfft; end
if (nargin<6 | isempty(over) ) over=floor(Nfft/2); end
if (nargin<7 | isempty(quasiwin) ) quasiwin=16; end

if length(x)~=length(y)
  error 'vectors x and y must be the same length'
end

% In terms of OS:DTSP
L=Nfft;  % length of segment
R=over;
w=window_func(Nfft,win);
w=w./sum(w);
[qw,len]=window_func(64,quasiwin);
U=1/L*sum(w.^2);

X=fft(window(block(x,L,L-R),w),L);
Y=fft(window(block(y,L,L-R),w),L);
K=size(X,2);

Pxy=(Y.*conj(X))/L/U;
Pxx=(X.*conj(X))/L/U+1e-12;
Pyy=(Y.*conj(Y))/L/U+1e-12;
Pxy=filter1(qw,Pxy',floor(len/2))';
Pxx=filter1(qw,Pxx',floor(len/2))';
Pyy=filter1(qw,Pyy',floor(len/2))';
Cxy=abs(Pxy).^2./(Pxx.*Pyy);
f=[0:Nfft-1]/Nfft*Fs;

if (isreal(x)&isreal(y))
  N=floor((Nfft+1)/2)+mod(Nfft+1,2);
  Cxy=Cxy(1:N,:);
  f=f(1:N);
end

if nargout<1
  t=(1:K)*L/Fs;
  imagesc(t,f,Cxy);
  ydir
  grid on;
  xlabel('Time');
  ylabel('Frequency (Hz)');
  clear Cxy
end
