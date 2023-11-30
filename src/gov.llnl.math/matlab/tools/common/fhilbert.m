function out=fhilbert(in);
% FHILBERT fast hilbert transform

h1=0.94167;
h2=0.53239;
h3=0.186540;
h4=0.7902015;
%B=[conv([-h4 0 1],[-h3 0 1]) 0] + i*conv([-h2 0 1],[0 -h1 0 1]);
%A=conv([1 0 -h4],[1 0 -h3]) + i*conv([1 0 -h2],[1 0 -h1]);
%out=filter(B,A,in);
out=filter(conv([-h4 0 1],[-h3 0 1]),conv([1 0 -h4],[1 0 -h3]),in)+ ...
    i*filter(conv([-h2 0 1],[0 -h1 0 1]),conv([1 0 -h2],[1 0 -h1]),in);

