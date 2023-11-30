function chi2qq(data,df);

data=sort(data);
ord=(0:length(data)-1)/(length(data)-1);

plot(chi2inv(ord,df),data,'.');
line([data(1) data(end)],[data(1) data(end)],'Color',[0 0 0])
xlabel(sprintf('ChiSquared (df=%f)',df));
ylabel('Measured');
legend('data','ideal','Location','SouthEast');
