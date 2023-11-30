function out=chi2pdf(X, m)
% out=chi2pdf(X, m)
% P(x<X) for chi^2 distribution with m degrees of freedom
%
%  Chi^2 properties 
%    Exp = m
%    Var = 2m 
%
%    Chi^2(m) is Gamma(m/2, 2)

out=gammacdf(X, m/2, 2);
