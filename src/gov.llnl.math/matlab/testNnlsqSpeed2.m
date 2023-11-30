solver=gov.llnl.math.Nnlsq();

% Step 1 - create the problems
if (1)
  trials=200;
  N=60;
  M=500;

  load test.mat

  for i=1:trials
    Ar=A;
    Yr=y(:,i);
    A=Ar;
    Y{i}=Yr;
    Aj=gov.llnl.math.DoubleMatrix.createFromArray(Ar);
  end
end

save test.mat A y b

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
