function out=ismatrix(in);

out=ndims(in)==2 & size(in,1)>1 & size(in,2)>1;
