function Y=rownorm(X);

X=X.^2;
Y=sqrt(sum(X,2));
