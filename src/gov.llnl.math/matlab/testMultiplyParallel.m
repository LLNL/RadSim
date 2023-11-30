import gov.llnl.math.DoubleMatrix;

N=2000;
M=200;
A=randn(N,M);
Aj=DoubleMatrix.createFromArray(A);

trials=1000;
for i=1:trials
%  %B{i}=max(randn(N,1),0);
%  B{i}=randn(1,N);
%  j=gov.llnl.math.DoubleMatrix(B{i}(:),1,N);
%  Bj{i}=j;
%
%  C{i}=max(randn(M,1),0);
  C{i}=randn(M,1);
  j=gov.llnl.math.DoubleMatrix(C{i}(:),M,1);
  Cj{i}=j;
end

fprintf('Native\n');
tic
for i=1:trials
  R=A*C{i};
end
toc

fprintf('Vectors\n');
tic
for i=1:trials
  R=gov.llnl.math.DoubleMatrix.multiply(Aj,C{i});
end
toc

fprintf('Parallel\n');
tic
for i=1:trials
  R=gov.llnl.math.DoubleMatrix.multiplyParallel(Aj,C{i},2);
end
toc

tic
for i=1:trials
  R=gov.llnl.math.DoubleMatrix.multiplyParallel(Aj,C{i},4);
end
toc


