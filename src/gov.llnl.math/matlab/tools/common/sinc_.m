function Y=sinc(X);
Y=zeros(size(X));
X2=pi*X;
X2(find(X2==0))=1e-15;
Y=sin(X2)./X2;
