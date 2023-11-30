import gov.llnl.math.*;
import gov.llnl.math.random.*;

gn=GammaRandom();
gn.setSeed(floor(1e6*rand));

trials=1000000;
df=10;

% Produce a list of random numbers
tic
d1=gn.drawArray(df/2,2,trials);
%d1=chi2rnd(df,trials,1);
toc

tic
d1=StatisticalUtilities.chiSquareTransformArray(d1,df);
%d1=chi2rnd(df,trials,1);
toc

v1=sort(d1);
u=(1:trials)/trials;
m1=min(v1);
m2=max(v1);

% QQ plot
plot(norminv(u),v1,'.')
line([m1 m2],[m1 m2])

