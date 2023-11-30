function out=chipdf(x, m)
% probablity density for chi^2 distribution with m degrees of freedom
%
%   Exp = m
%   Var = 2m 
%
%     Chi^2(m) is Gamma(m/2, 2)

out=gammapdf(x, m/2, 2);
