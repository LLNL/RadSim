function Q1=make_psd(Q);

Q=(Q+transpose(Q))/2;
[V,D]=eig(Q);
D=diag(D);
D=max(D,0.001);
Q1=V*diag(D)*V';

