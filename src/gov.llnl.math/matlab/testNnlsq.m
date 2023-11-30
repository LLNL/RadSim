
solver=gov.llnl.math.Nnlsq();

% The regressors
A=rand(20,300);

% The truth
xt=zeros(300,1);
xt(floor(1+300*rand(10,1)))=rand(10,1);

% Make the observation with noise
Y=A*xt+0.2*randn(20,1);

% Solve using matlab
x=lsqnonneg(A,Y);
mse=(sum([Y-A*x].^2))

% Convert matrix to use for java
solver.useScaling=1
Aj=gov.llnl.math.DoubleMatrix.createFromArray(A(:),20,300);
xj=solver.solve(Aj, Y);
mse=(sum([Y-A*xj].^2))
fprintf('reported MSE %e\n',solver.workspace.getMSE)

%solver2=gov.llnl.math.NnlsqMemoryModified();
%xj=solver2.solve(Aj, Y);
%mse=sqrt(sum([Y-A*xj].^2))

