function binoks(data, n, p)

h=hist(data,[0:n]);
hc=cumsum(h)/length(data);

tc=binocdf(0:n,n,p);
size(hc)
size(tc)
ks=max(abs(hc-tc))

plot([hc'-tc'])


