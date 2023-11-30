import gov.llnl.math.*;
import gov.llnl.math.optimize.*;
import gov.llnl.rtk.calibration.*;
x=[0:100]';
y=10*exp(-(x-50).^2/20)+0.1*(100-x)+0.5*randn(101,1);
%y=y/sqrt(sum(y.^2));

%lr=LinearRegression();
%lr.add(x(50:end), y(50:end));
%pf=lr.compute();
%u=zeros(101,1);
%w=zeros(101,1);
%hold off;
%plot([y])
%hold on;
%for j=1:3
%  lr.clear();
%  for i=1:length(x)
%    u(i)=pf.evaluate(x(i));
%  end
%  for i=50:length(x)
%    w(i)=SpecialFunctions.logistic(u(i), y(i), 100);
%    lr.add(x(i), y(i), w(i)); %SpecialFunctions.logistic(u(i), x(i), 0.1));
%  end
%  plot([u])
%  pf=lr.compute();
%end
%
plot(x,y);
gf=GaussianFitter();
ff.iterations=0;
gf.limitRange=1
gf.setRegionOfInterest(0,100);
mu=gf.fitPeak(y);

% sweep test
R=zeros(81,3);
F=[];
for i=0:60
  gf.setRegionOfInterest(i,i+40);
  R(i+1,1)=gf.fitPeak(y);
  R(i+1,2)=gf.getPeakIntensity();
  R(i+1,3)=gf.sigma;
  F=[F gf.getFit()];
end

