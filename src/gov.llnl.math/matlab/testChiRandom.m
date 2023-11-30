import gov.llnl.math.random.*;

gn=GammaRandom();
gn.setSeed(floor(1e6*rand));

trials=100000;
df=10;

tic
d1=gn.drawArray(df/2,2,trials);
%d1=chi2rnd(df,trials,1);
toc

v1=sort(d1);
u=(1:trials)/trials;
m1=min(v1);
m2=max(v1);

% QQ plot
plot(chi2inv(u,df),v1,'.')
line([m1 m2],[m1 m2])

