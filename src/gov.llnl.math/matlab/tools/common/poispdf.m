function out=poispdf(u, v);

out=exp(-u + v.*log(u) - gammaln(v+1));

