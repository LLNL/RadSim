function Y=resample(X,P,Q,varargin);


% FIXME 
%   decimation aliases
%   selection of the spread needs some work.
%   integer upscaling and downscaling could be accelerated.

do_end=0;
spread=10;
flip=0;

for i1=1:length(varargin)
  if ischar(varargin{i1})
    switch varargin{i1}
    case 'end'
      % the states outside of the signal should stay as the end points
      do_end=1;
    end
  end
end

if ismatrix(X)
  len=size(X,1);
  if nargin<3 | isempty(Q)
    Q=len; 
  end;
  X2=X;

  %% NOT COMPLETE
else
  len=length(X);
  if nargin<3|isempty(Q) 
    Q=len; 
  end
  if (size(X,2)>1) flip=1; end;

  X2=X(:);
end

M=size(X2,2);
n_len=floor(len/Q*P);
Y=zeros(n_len,M);

if (do_end)
  for i1=1:n_len
    c=(i1-0.5)*Q/P+0.5;
    i2=min(max(floor(c)+(-spread:spread)',1),len);
    W=sinc(floor(c)-c+(-spread:spread)');
    Y(i1,:)=sum(W(:,ones(1,M)).*X2(i2,:));
  end
else
  for i1=1:n_len
    c=(i1-0.5)*Q/P+0.5;
    i2=(max(floor(c)-spread,1):min(floor(c)+spread,len))';
    W=sinc(i2-c);
    Y(i1,:)=sum(W(:,ones(1,M)).*X2(i2,:));
  end
end

if flip Y=Y.'; end
