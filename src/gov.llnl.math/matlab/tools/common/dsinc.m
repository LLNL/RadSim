function Y=dsinc(X);
% first derivative of sinc function
X2=X;
X2(find(X2==0))=1e-9;
Y=cos(pi*X2)./X2-sin(pi*X2)./(pi*X2.^2);
