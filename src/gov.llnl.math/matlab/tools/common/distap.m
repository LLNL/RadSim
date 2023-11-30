function dist=distap(A);

N=size(A,1);
dist=A(reshape(ones(N-1,1)*(1:N),(N-1)*N,1),:) ...
    -A(reshape(flipud(nchoosek(1:N,N-1))',N*(N-1),1),:);

dist=sum(dist.^2,2).^0.5;

