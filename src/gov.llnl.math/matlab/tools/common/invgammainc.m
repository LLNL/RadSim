function x=gammaincinv(w,a);

ga=exp(1/a*(gammaln(a+1)+log(w)));
x=real(ga + ga^2/(1+a) + (3*a+5)/(2*(a+1)^2*(a+2))*ga^3);
for i=1:30
  f=w-gammainc(x,a);
  if (abs(f)<min(1e-6,0.1*(1-w)))
    break
  end
  fp=-exp(-x+(a-1)*log(x));
  x=x-3.2*f/fp;
end
