N=500;
M=500;
P=50;
A=randn(N,M);
Aj=gov.llnl.math.DoubleMatrix.createFromArray(A);
trials=100;
for i=1:trials
  %C{i}=max(randn(M,1),0);
  C{i}=randn(M,P);
  j=gov.llnl.math.DoubleMatrix.createFromArray(C{i});
  Cj{i}=j;
end

tic
for i=1:trials
  R=A*C{i};
end
toc


tic
for i=1:trials
  R=gov.llnl.math.DoubleMatrix.multiply(Aj,Cj{i});
end
toc

%tic
%for i=1:trials
%  R=gov.llnl.math.DoubleMatrix.multiplyV2(Aj,Cj{i});
%end
%toc

