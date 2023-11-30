function out=gammapdf(x, a, b)
%  probability density of Gamma at x with shape a and scale b
%    1/b is referred to as the rate.
%
%  Gamma properties 
%    Exp = ab
%    Var = ab^2

out=1/gamma(a)*b^-a * x.^(a-1) .* exp(-x/b);

