function out=expcdf(T, b)
% P(t<T) for exponential function with mean b (rate 1/b)
%
%  Mean   = b
%  Median = -b log 0.5
%  Var    = b^2

out=1-exp(-T./b);
