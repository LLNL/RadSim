function gumbelqq(data,mn,sigma,p1,p2);

data=sort(data);
ord=(0:length(data)-1)/(length(data)-1);

plot(gumbelinv(ord,mn,sigma),data,'.');
line([data(1) data(end)],[data(1) data(end)],'Color',[0 0 0])
xlabel(sprintf('Gumbel Distribution (mu=%f, beta=%f)',mn,sigma));
ylabel('Measured');
legend('data','ideal','Location','SouthEast');

if nargin>3
  i0=floor(length(data)*p1);
  i1=floor(length(data)*p2);
  hold on;
  plot(gumbelinv(ord(i0:i1),mn,sigma),data(i0:i1),'r.');
  hold off;
end
