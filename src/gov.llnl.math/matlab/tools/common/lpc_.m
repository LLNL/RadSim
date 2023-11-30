function [A,E,K]=lpc_(Y,N,varargin);
% LPC Linear Predictor Coefficients.
%   [A,E,K] = LPC(Y,N) computes the coefficients to best solve
%   
%     Yp(N) = SUM_{i=1}^N A(i) Y(N-i)
%
%   in the mean square sense.  Y can be either a vector or
%   a matrix.  If it is a matrix each input column is considered a
%   row and each output row is the corresponding parameters
%
%     A  the coefficients [1 -A(1) -A(2) ... A(N) ]
%     E  the variance of the prediction error
%     K  column vector of the PARCOR coefficients 
%         (must be (-1,1) for stablity)
%
%  Options
%    LPC(...,'denoise',xcorr(N,S,'biased'))
%      remove the autocorrelation of noise signal N prior to LPC
%      estimation.
%    LPC(..., 'full') 
%      return the full durbin error estimates.


full_err=0;
noise=[];
i1=1;
while i1<length(varargin)
  if ischar(varargin{i1})
    switch lower(varargin{i1})
      case ('full')
        full_err=1;
      case ('denoise')
        noise=varargin{i1+1}(:);
        i1=i1+1;
      otherwise
        error(['Unknown argument ',varargin{i1} ]);
    end
  end
  i1=i1+1;
end

if (isvector(Y))
  if (nargin<2)
    N=length(Y)-1;
  end

  R=xcorr_(Y,N+1,'biased');
  if (~isempty(noise))
    noise=noise_match(noise,length(R)); 
    noise=reshape(noise,size(R,1),size(R,2));
    R=R-noise;
  end
  R=R((0:N+1)+N+2);
  z=(R(1)==0);
  R(1)=R(1)+z;
  
  [A,E,K]=durbin(R,N);
  if (any(abs(K)>1))
    warning('Unstable output from durbin');
    A=[0 zeros(1,length(A)-1)]';
  end
  if (~full_err)
    E=E(length(E));
    E=E.*(~z);
  end
  A=[1; -A];
elseif (ismatrix(Y))
  if (nargin<2)
    N=size(Y,1)-1;
  end

  R=xcorr_(Y,N,'biased','auto');
  if (~isempty(noise))
    noise=noise_match(noise,size(R,1));
    R=R-noise(:,ones(1,size(R,2)));
  end
  R=R((0:N)+N+1,:);
  z=R(1,:)==0;
  R(1,:)=R(1,:)+z;

  [A,E,K]=durbin(R);
  if (~full_err)
    E=E(size(E,1),:);
    E=E.*(~z);
  end
  A=[ones(1,size(A,2)); -A];
end



function out=noise_match(noise,N)

M=length(noise);
diff=floor((N-M)/2);

out=zeros(N,1);
if (diff==0)
  out(:)=noise;
elseif (diff>0)
  out(1+diff:N-diff)=noise;
else
  out(:)=noise(-diff+1:M+diff);
end

