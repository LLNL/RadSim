function O=gammaln2(z);

% replacement for gammaln from matlab 

% Simplified Lanczos Approximation
% http://www.rskey.org/gamma.htm
Q = fliplr([75122.6331530, 80916.6278952, 36308.2951477, 8687.24529705, 1168.92649479, 83.8676043424, 2.50662827511]);

O1=log(polyval(Q, z));
O2=log(prod(z+(0:6)));
O3=(z+0.5)*log(z+5.5);
O4=-(z+5.5);
O=O1-O2+O3+O4;


%                             x^n
%  gammainc = x^a e^-x Sum -----------
%                      n=0 a (a+1) ... (a+n)
%

