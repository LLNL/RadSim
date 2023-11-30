
for i=1:1000
  a(i)=exp(15*rand-3);
  x(i)=exp(15*rand-3);
  r(i)=gammainc(x(i),a(i));
  r2(i)=gov.llnl.math.SpecialFunctions.gammaP(a(i),x(i));
end
