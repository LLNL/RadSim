function [A,E,K,G]= durbin(R,N)
%  DURBIN  solves Toellipze matrix using levinson-durbin algorithm
%    [A,E,K,G]=DURBIN(R,N)
%
%    Based on description from "Fundimentals of Speech Recognition"
%
%    Matrix must be in form
%
%     | r(0)   r(1)*  ...  r(N-1)*|  | a(1) |   | r(1) | 
%     | r(1)   r(0)   ...  r(N-2)*|  | a(2) |   | r(2) | 
%     | r(2)   r(1)   ...  r(N-3)*|  | a(3) |   | r(3) | 
%     |  .           .      .     |  |   .  | = |  .   |
%     |  .             .    .     |  |   .  |   |  .   | 
%     |  .               .  .     |  |   .  |   |  .   | 
%     | r(N-1) r(N-2) ...  r(0)   |  | a(N) |   | r(N) | 
%
%    where 
%      R is vector [ r(0) .. r(N) ]
%      N is the order of recursion (default lenght(R)-1)  
%
%    if R is a matrix it solves each column independently.
%    Returns are always in the form of a column vector or
%    matrix of column vectors.
%   
%    Returns
%      A - LPC coefficients 
%      E - prediction errors for each N
%      K - PARCOR coefficients 
%      G - log area ratios
%
%  See LPC, PARCOR2LPC

% To be consistant with sum and other multrow operators, we
% operate on column vectors.

% always work with the same dimension
if (isvector(R))
  if (size(R,1)~=length(R))
    R=R.';
  end
end

rows=size(R,1);
cols=size(R,2);

if (nargin<2)
  N=rows-1;
end


% initialize variables.
E=zeros(N+1,cols);
A=zeros(N,cols);
Ap=zeros(N,cols);
K=zeros(N,cols);

E(1,:)=R(1,:);
for i=1:N
  K(i,:)=(R(i+1,:)-sum( A(1:i-1,:).*R(abs(i-(1:i-1)+1),:),1 ))./E(i,:);
  A(i,:)=K(i,:);
  A(1:i-1,:)=A(1:i-1,:)-(ones(i-1,1)*K(i,:)).*A(i-[1:i-1],:);
  E(i+1,:)=(1-K(i,:).^2).*E(i,:);
end

E=E(2:size(E,1),:);

% compose area ratios if requested.
if (nargout>3)
  G=log(1-K)-log(1+K);
end

