
solver=gov.llnl.math.Nnlsq();

% The regressors
A=rand(20,300);

% The truth
xt=zeros(300,1);
xt(floor(1+300*rand(10,1)))=rand(10,1);

% Make the observation with noise
Y=A*xt+0.2*randn(20,1);

% Solve using matlab
%x=lsqnonneg(A,Y);
%mse=(sum([Y-A*x].^2))

%% Convert matrix to use for java
Aj=gov.llnl.math.DoubleMatrix.createFromArray(A(:),20,300);
%xj=solver.solve(Aj, Y);
%mse=(sum([Y-A*xj].^2))

W=ones(size(Y));
xj1=solver.solve(Aj, Y,W);
solver.workspace.getMSE
W(:)=4;
xj2=solver.solve(Aj, Y,W);
solver.workspace.getMSE

plot([Y A*xj1 A*xj2]);


