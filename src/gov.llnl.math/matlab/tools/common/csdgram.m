function [Pxy,f]=csdgram(x,y,Nfft,Fs,win,over);

if (nargin<3 | isempty(Nfft) ) Nfft=256; end
if (nargin<4 | isempty(Fs) )   Fs=2; end
if (nargin<5 | isempty(win) )  win=Nfft; end
if (nargin<6 | isempty(over) ) over=floor(Nfft/2); end

if length(x)~=length(y)
  error 'vectors x and y must be the same length'
end

% In terms of OS:DTSP
L=Nfft;  % length of segment
R=over;
w=window_func(Nfft,win);
U=1/L*sum(w.^2);

X=fft(window(block(x,L,L-R),w),L);
Y=fft(window(block(y,L,L-R),w),L);
K=size(X,2);

Pxy=(Y.*conj(X))/L/U;
f=[0:Nfft-1]/Nfft*Fs;

if (isreal(x)&isreal(y))
  N=floor((Nfft+1)/2)+mod(Nfft+1,2);
  Pxy=Pxy(1:N,:);
  f=f(1:N);
end

if nargout<1
  t=(1:K)*L/Fs;
  subplot(211)
  imagesc(t,f,dbp(Pxy));
  ydir
  subplot(212)
  imagesc(t,f,angle(Pxy));
  ydir
  grid on;
  xlabel('Time');
  ylabel('Frequency (Hz)');
  clear Pxy
end
