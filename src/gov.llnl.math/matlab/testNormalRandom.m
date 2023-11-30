import gov.llnl.math.random.*;

nr=NormalRandom();

trials=10000000;
tic 
d1=nr.drawArray(trials);
toc

tic
d2=randn(trials,1);
toc

% FIXME how do I know if these are really normally distributed?
% K-S Test only checks the center of the distribution
% QQ plot between 
v1=sort(d1);
v2=sort(d2);
u=(1:length(d1))/length(d1);
plot(norminv(u),v1,'.',norminv(u),v2,'g.')

