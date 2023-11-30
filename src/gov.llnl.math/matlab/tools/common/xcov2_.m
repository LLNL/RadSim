function R=xcov2_(X,varargin)
%
% XCOv2 - cross correlation in 2 dimensions
%   R = XCORR2(X) for matrix X returns the 2d autocorrelation of the 
%      matrix with itself.
%
%   R = XCORR(X) for matrix MxN X return a matrix 2M+1 by N^2
%     containing the correlation between each column.
%
%   R = XCORR(X,Y) for matrix X and Y return the correlation 
%     between X and Y defined as
%
%     R(n1,n2) = SUM_{m1=0}^{M-1} {SUM_{m2=0}^{N-1} X*(n1,n2) Y(n1-m1,n2-m2)
%
%   This is an approximation of the true statistical correlation
%   R(n1,n2) = E( X(n1,n2) Y(n1-m1,n2-m2) ).
%
%   Options:
%
%     R = XCORR(...,opt) specify a normalization
%       opt is one of
%        'none'   no normalization is performed (default)
%        'biased' divides all terms by N-1 
%        'coef' divides the sequence by R(0).

%        'unbiased' divides all terms by the number of members of the 
%                   sequences used to form that estimate.

% Parse the input arguments
hasY=0;
opt='none';
% parse arguments
for i1=1:length(varargin)
  if isnumeric(varargin{i1})
%     if (size(varargin{i1})==[1 1])
%       lag=varargin{i1};
%     else
       Y=varargin{i1};
       hasY=1;
%     end
  else
    if (strcmp(varargin{i1},'auto'))
      auto=1;
    else
      opt=varargin{i1};
    end
  end
end

[N M]=size(X);
zX=zeros(size(X));
fX=fft2([X-mean(mean(X)) zX; zX zX]);
if (hasY)
  fY=fft([Y-mean(mean(Y)) zX; zX zX]);
else
  fY=fX;
end
R=ifft2(fX.*conj(fY));
R0=R(1);
R=fftshift(R);
R=R(2:N*2,2:M*2);

% real with the numerical inaccuracies of fft
if (hasY)
  if (isreal(X)&isreal(Y))
     R=real(R);
  end
else
  if (isreal(X))
    R=real(R);
  end
end

% handle biasing
switch lower(opt)
  case 'biased'
    R=R./(N-1)/(M-1);
% case 'unbiased'
%   if (size(R,1)>size(R,2))
%     R=R./(N-1-abs(-lag:lag));
%   else
%     R=R./(N-1-abs(-lag:lag)');
%   end
  case {'coeff','coef'}
    R=R./R0;
%    otherwise
%      ;
end

