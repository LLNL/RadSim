function [A,y,b]=makeProblem(N,M,K);

%A=max(randn(N,M),0);
A=filter([1 2 3 2 1],1,max(randn(N,M),0));
b=zeros(M,K);
for i1=1:K
  u=floor(min(N,M)*rand);
  i=floor(M*rand(u,1)+1);
  b(i,i1)=exp(4*rand(u,1));
  y(:,i1)=A*b(:,i1);
  y(:,i1)=y(:,i1)+0.1*sqrt(y(:,i1)'*y(:,i1))*randn(N,1)/N;
end


