function Y=d2sinc(X);
% second derivative of sinc function
Y=zeros(size(X));
X2=X;
s1=find(abs(X2)>1e-3);
s2=find(abs(X2)<=1e-3);
% we have to handle this in 2 different ways.
Y(s1)=-sin(pi*X2(s1))*pi./X2(s1) - 2*cos(pi*X2(s1))./(X2(s1).^2) + 2*sin(pi*X2(s1))./(pi*X2(s1).^3);
Y(s2)=-pi^2/3 +0.1*pi^4*X2(s2).^2;
