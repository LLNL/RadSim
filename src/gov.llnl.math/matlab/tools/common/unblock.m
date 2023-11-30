function out=block(in,M);
% UNBLOCK
%  Unchops out a matrix stream back into a vector.
%
%   M shift between windows.

N=size(in,1);
sr=M*(size(in,2)-1)+N;
out=zeros(sr,1);

for i1=0:size(in,2)-1
  out(i1*M+(1:N))=out(i1*M+(1:N))+in(:,i1+1);
end
