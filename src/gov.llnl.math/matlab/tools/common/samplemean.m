function out=samplemean(v, p);

%
%  v = value, p=density

out=sum(v.*p)/sum(p);
