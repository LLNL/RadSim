function Y=hpsfilter(X)

[H,F]=hps(X);
imagesc(abs(F));
R=zeros(size(F));
Fs=8000;
n1=floor(2048*70/Fs);
n2=min(size(H,1),floor(2048*200/Fs));
mH=max(H);
H=H-mH(ones(size(H,1),1),:);
R(n1:n2,:)=exp(H(n1:n2,:));
R(2050-(n1:n2),:)=exp(H(n1:n2,:));
imagesc(real(R))
max(max(R))

Y=ifft(R.*F);
Y=real(Y(1:240,:));



