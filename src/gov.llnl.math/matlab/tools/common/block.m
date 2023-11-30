function out=block(in,N,M,opt);
% block
%  Chops out an input stream into a block of char.
%  Input should be a column vector.
%
%   N    size of windows (input will be zero padded to fit.
%   M    shift between windows.
%   opt 
%     'nopad' don't include all the data.

nopad=0;

if (nargin<3)
  M=N;
end

if (ismatrix(in))
%  error('matrix blocking not supported');
  F=ceil(size(in,1)/M);
  out=zeros(N,size(in,2),F);
  in=[in;zeros(N,size(in,2))];
  for f=1:F
    out(:,:,f)=in((1:N)+(f-1)*M,:);
  end
  return
end

% vectors
n=length(in);
if (size(in,1)~=length(in))
  in=in.';
end

if (nargin>3 & strcmp(opt,'nopad')) nopad=1; end

if (M==N)
  if (mod(n,N)>0)
    if (nopad)
      in=in(1:length(in)-mod(n,N));
    else
      in=[in  ; zeros(N-mod(n,N),1) ];
    end
  end
  out=reshape(in,N,length(in)/N);
else
  if (nopad)
    F=floor((n-N)/M);
  else
    F=ceil(n/M);
  end
  out=zeros(N,F);
  in=[in ; zeros(N,1)];
  for i=0:F-1
    out(:,i+1)=in([1:N]+i*M);
  end
end
