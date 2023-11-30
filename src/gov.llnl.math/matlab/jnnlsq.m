function [x] = jnnlsq(A, y, W)
%
%
%
%

global solver;
if isempty(solver)
	solver = gov.llnl.math.Nnlsq();
	solver.setParallel(0);
end

solver.useScaling=0;

%x = solver.solve(gov.llnl.math.DoubleMatrix.createFromArray(A), y, diag(W));
x = solver.solve(gov.llnl.math.DoubleMatrix.createFromArray(A), y, gov.llnl.math.DoubleMatrix.createFromArray(W));
