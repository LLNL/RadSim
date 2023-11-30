function out=flipdim(in,dim)
%  FLIPDIM  flip a matrix along dimention dim.
%    FLIPDIM(X,DIM) for matrices returns the matrix X flipped 
%      along dimension DIM. 
%    FLIPDIM(X) for matrices flips along the first non-singleton
%      direction
%    FLIPDIM(X) for a vector flips the vector.

if nargin<1, error('Requires at least one argument'); end
if nargin<2 
  dim=find(size(in)~=1);
  dim=dim(1);
end

if (dim>ndims(in))
  out=in;
  return;
end
index=cell(1,max(dim,ndims(in)));
index(:)={':'};
index(dim)={size(in,dim):-1:1};
out=in(index{:});
