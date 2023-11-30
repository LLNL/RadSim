solver=gov.llnl.math.NNLSQScaledConstrained();
input = gov.llnl.math.NNLSQScaledConstrained().InputDoubleMatrix();

% Step 1 - create the problems
if (1)
  trials=20;
  N=50;
  M=1000;

  [A,y,b]=makeProblem(N,M,trials);

  for i=1:trials
    Ar=A;
    Yr=y(:,i);
    A=Ar;
    Y{i}=Yr;
    Aj=gov.llnl.math.DoubleMatrix.createFromArray(Ar);
  end
end

%save test.mat A y b

% Step 2 -test both algorithms
% Benchmark in MATLAB
tic
for i=1:trials
  x{i}=lsqnonneg(A,Y{i});
end
toc

% Benchmark in Java
tic
for i=1:trials
  xj{i}=solver.solve(Aj,Y{i});
end
toc

solver.dispose();
