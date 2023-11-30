function out=gammacdf(X, a, b)
%  P(x<X) for Gamma with shape a and scale b (rate = 1/b)
%
%  Gamma properties 
%    Exp = ab
%    Var = ab^2
%
%   gammacdf = gammainc(X/b, a)/gamma(a)

out=gammainc(X/b, a);
