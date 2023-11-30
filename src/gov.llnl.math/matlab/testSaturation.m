import gov.llnl.math.optimize.*;

gain=20;
offset=5;
saturation=1/100;

x=[0:20]';
y=(gain*x+offset)./(1+saturation*x);

sr=SaturationRegression();
sr.add(x([2 6 18]),y([2 6 18]));
f0=sr.compute();
sr.setConstrainZero(1);
f1=sr.compute();

y0=zeros(length(x),1);
y1=zeros(length(x),1);
for i=1:length(x)
  y0(i)=f0.evaluate(x(i));
  y1(i)=f1.evaluate(x(i));
end

plot(x, [y y0 y1]);
