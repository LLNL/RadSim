function binoqq(data, n, p)

data=sort(data);
ord=(0.5:length(data))/(length(data));

plot(binoinv(ord,n,p),data,'.');
line([data(1) data(end)],[data(1) data(end)],'Color',[0 0 0])
xlabel(sprintf('Binomial Distribution (n=%f, p=%f)',n,p));
ylabel('Measured');
legend('data','ideal','Location','SouthEast');

line([data(1) data(end)],[data(1) data(end)])



