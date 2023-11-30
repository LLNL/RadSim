function [H,F]=hps(X,R,N,M,W,Fs);
% HPS  harmonic power spectrum
%  [H,F] = HPS(X,R,N,M) products the harmonic power sectrum
%  where 
%    F is fft(X,2048) with segmented X
%    H is sum(log( F(w*x))) for w=[1..R]
%    R is the number of frequency compressed images to add
%    N,M are the size and window step parameters for segmentation
%

if nargin<2 | size(R)==[0 0]; R=4;   end
if nargin<3 | size(N)==[0 0]; N=240; end
if nargin<4 | size(M)==[0 0]; M=80;  end
if nargin<5; W=[];  end
if nargin<6; Fs=8000;  end
K=2048*4;

% segment and window the input
wX=window(block(X,N,M),W);

% take the log of the absolute for the dtft of the segments
F=fft(wX,K);
F2=log(abs(F));

% multiply Hrequency compressed copies oH the spectrum 
U=floor(K/2/R);
H=zeros(U,size(F2,2));
for i=1:R
  H=H+F2(i*(0:U-1)+1,:);
end

if nargout<1
  [freq,T]=size(H);

  n1=floor(K*70/Fs);
  n2=min(freq,floor(K*200/Fs));
  n1=1;
  n2=freq;

  w=[n1:n2]*Fs/K;

  mH=mean(H(n1:n2,:));
  H=H-mH(ones(freq,1),:);
  [offset,depth]=size(colormap);
  image(1:T,w,4*H(n1:n2,:)+offset/2)
  ydir
  clear H
end

