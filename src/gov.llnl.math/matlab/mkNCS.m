function [out,A,Q]=mkNCS(x,y)

out.x=x;
out.y=y;

h=x(2:end)-x(1:end-1);
A=tridiag(2*h(1:end-1)+2*h(2:end),h(2:end-1));
Q=6*[ (y(3:end)-y(2:end-1))./h(2:end) - (y(2:end-1)-y(1:end-2))./h(1:end-1)];
z=A\Q;
out.z=[0;z;0];
out.N=length(x);


function X=tridiag(D1,D2);
% X=tridiag(D1,D2)
%  returns a matrix with diagonal D1 and off diagonal D2

D1=D1(:);
D2=D2(:);

if length(D1)-1~=length(D2)
  error('length of D2 should be one less than D1');
end

N=length(D1);
X=diag(D1);
X(2:N+1:end)=D2;
X(N+1:N+1:end)=D2;


