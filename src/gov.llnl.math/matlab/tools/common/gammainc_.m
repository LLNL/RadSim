function s=foo(x,a)
s=0;
r=x^a*exp(-x)/a;
for n=0:50
  s=s+r;
  r=r*x/(a+n+1);
  r
end

