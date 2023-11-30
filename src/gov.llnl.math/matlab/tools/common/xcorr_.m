function R=xcorr(X,varargin);
%
% XCORR - cross correlation
%   R = XCORR(X) for vector X returns the autocorrelation of the 
%      vector with itself.
%
%   R = XCORR(X) for matrix MxN X return a matrix 2M+1 by N^2
%     containing the correlation between each column.
%
%   R = XCORR(X,'auto') for matrix NxM X returns the autocorrelation
%     for each column.
%
%   R = XCORR(X,Y) for vectors X and Y return the correlation 
%     between X and Y defined as
%
%     R(n) = SUM_{m=0}^{N-1} X*(n) Y(n-m)
%
%   R = XCORR(X,Y) for vectors X and matrix Y return the correlation 
%     between X and each column of Y
%
%   This is an approximation of the true statistical correlation
%   R(n) = E( X(n) Y(n-m) ).
%
%   Outputs:
%     R is vector of lags [ R(-N) R(1+N) ... R(0) ... R(N-1) R(N) ]
%
%   Options:
%
%     R = XCORR(...,N) return only the lags corresponding to 
%     the first N lags. (defaults to length(X)-1)
%
%
%     R = XCORR(...,opt) specify a normalization
%       opt is one of
%        'none'   no normalization is performed (default)
%        'biased' divides all terms by N-1 
%        'unbiased' divides all terms by the number of members of the 
%                   sequences used to form that estimate.
%        'coef' divides the sequence by R(0).

% TODO This function computes a lot of values which it just throws away
%  it should be optimized to only generate the requested values.
% TODO add positive only function
%     R = XCORR(...,'pos') only returns the positive lags.

% initialize variables
lag=i;
Y=X;
opt='none';
auto=0;

% parse arguments
for i1=1:length(varargin)
  if isnumeric(varargin{i1})
     if (size(varargin{i1})==[1 1])
       lag=varargin{i1};
     else
       Y=varargin{i1};
     end
  else
    if (strcmp(varargin{i1},'auto'))
      auto=1;
    else
      opt=varargin{i1};
    end
  end
end

if (all(size(X)>2))
  if (lag==i)
    lag=size(X,1)-1;
  end

  if (auto==1)
    R=zeros(2*lag+1,size(X,2));
    for i1=1:size(X,2)
      R(:,i1)=xcorr_(X(:,i1),lag,opt);
    end
  else
    R=zeros(2*lag+1,size(X,2)^2);
    for i1=1:size(X,2)
      for i2=1:size(X,2)
        R(:,(i1-1)*size(X,2)+i2)=xcorr_(X(:,i2),X(:,i1),lag,opt);
      end
    end
  end

elseif (all(size(Y)>2))
  if (lag==i)
    lag=size(X,1)-1;
  end

  R=zeros(2*lag+1,size(Y,2));
  for i1=1:size(Y,2)
    R(:,i1)=xcorr_(X,Y(:,i1),lag,opt);
  end
else
  % vectors is easy just convolve one with the other.
  %  Note: a faster implementation uses the FFT but 
  %   that leads to numerical inaccuracies where all real
  %   sequences form imaginary correlations.
  R=conv(X,reverse(conj(Y)));

  if (length(X)~=length(Y))
    if (length(X)>length(Y))
      R=fpad(R,length(X)-length(Y));
    else
      R=rpad(R,length(Y)-length(X));
    end
  end

  % work from the center
  C=floor(length(R)/2)+1;
  if (lag~=i)
    if (lag==C-1)
      ;
    elseif (lag>C-1)
      R=fpad(rpad(R,lag-C+1),lag-C+1);
    elseif (lag==1)
      R=R(C);
    else
      R=R(C+(-lag:lag));
    end
  else
    lag=C-1;
  end

  % normalize
  N=floor(max(length(X),length(Y)))+1;
  switch lower(opt)
    case 'biased'
      R=R./(N-1);
    case 'unbiased'
      if (size(R,1)>size(R,2))
        R=R./(N-1-abs(-lag:lag));
      else
        R=R./(N-1-abs(-lag:lag)');
      end
    case {'coeff','coef'}
      R=R./R(lag+1);
    case {'norm'}
      R=R./max(abs(R));
  %    otherwise
  %      ;
  end
end
