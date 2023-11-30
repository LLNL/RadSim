function [A,E,K]= levinson(R,N)
%  LEVINSON  solves Toellipze matrix using levinson-durbin algorithm
%    Based on description from "Fundimentals of Speech Recognition"
%    (Compatable with matlabs implementation)
%
%    Matrix must be in form
%
%     | r(0)   r(1)*  ...  r(N-1)*|  | a(1) |   | - r(1) | 
%     | r(1)   r(0)   ...  r(N-2)*|  | a(2) |   | - r(2) | 
%     | r(2)   r(1)   ...  r(N-3)*|  | a(3) |   | - r(3) | 
%     |  .           .      .     |  |   .  | = | -  .   |
%     |  .             .    .     |  |   .  |   | -  .   | 
%     |  .               .  .     |  |   .  |   | -  .   | 
%     | r(N-1) r(N-2) ...  r(0)   |  | a(N) |   | - r(N) | 
%
%    R is vector [ r(0) .. r(N) ]
%    N is the order of recursion (default lenght(R)-1)  
%   
%    Returns
%      A - prediction coef [ 1 -a(1) ... -a(N) ]
%      E - prediction error for iteration N
%      K - reflection coef

if (isvector(R))
  if (nargin<2)
    N=length(R)-1;
  end
  [A,E,K]=durbin(R,N);
  A=[1 -A.'];
  E=E(length(E));
  K=-K;
else
  if (nargin<2)
    N=size(R,1)-1;
  end
  [A,E,K]=durbin(R,N);
  A=[ones(size(R,2),1) -A.'];
  E=E(size(E,1),:).';
  K=-K;
end


