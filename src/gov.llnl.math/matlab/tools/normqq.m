function normqq(data,mn,sigma);
% normqq(data,mn,sigma);
%   Plot qq vs Normal distribution
%     data - scores
%     mn - mean of normal distribution
%     sigma - stddev of normal distribution

import gov.llnl.math.distribution.*;

data=sort(data);
ord=(0:length(data)-1)/(length(data)-1);
nd=NormalDistribution(mn,sigma);

plot(nd.cdfinv(ord), data,'.');
line([data(1) data(end)],[data(1) data(end)],'Color',[0 0 0])
xlabel(sprintf('Normal Distribution (mean=%f, var=%f)',mn,sigma));
ylabel('Measured');
legend('data','ideal','Location','SouthEast');
