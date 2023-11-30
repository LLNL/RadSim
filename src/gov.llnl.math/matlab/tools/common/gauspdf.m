function out=gauspdf(x, u, v);

out=(2*pi*v)^-0.5*exp(-(x-u).^2/2/v);

