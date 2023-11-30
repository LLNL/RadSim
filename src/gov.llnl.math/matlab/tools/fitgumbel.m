function [mu, beta]=fit(R, p1, p2);

import gov.llnl.math.distribution.*;
gd=GumbelDistribution.fit(R, p1, p2);
mu=gd.mu;
beta=gd.beta;

gumbelqq(R,mu,beta,p1,p2);

