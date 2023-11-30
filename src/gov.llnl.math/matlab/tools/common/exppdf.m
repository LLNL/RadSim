function out=exppdf(t, b)
% probability density for exponential function with mean b (rate 1/b)
%
%  Exp = b
%  Var = b^2

out=1/b.*exp(-t./b);
