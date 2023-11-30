function out=samplevar(v,p);
v=v(:);
p=p(:);
u=samplemean(v,p);
out=sum((v-u).^2.*p)/(sum(p)-1);
%out=sum((v-sum(v.*p)).^2 .* p);
