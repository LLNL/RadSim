function out=gumbelcdf(in, mu, beta);
out=exp(-exp(-(in-mu)/beta));

