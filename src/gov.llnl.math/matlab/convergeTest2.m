load('problem2.mat')
A=Anonthreat;
y=sample;
W=isigma;
x2=0;
x2 = jnnlsq(A, y, W);
