
import gov.llnl.math.signal.*;

B=[0.5 0.7 1.1 0.6 0.3];
A=[1 -0.9 0.3];
f1=FilterFIR(B);
f2=FilterIIR(B, A);
x=randn(50,1);
subplot(311)
plot([filter(B,1,x) f1.apply(x)]);
subplot(312)
plot([filter(B,A,x) f2.apply(x)]);
subplot(313)
plot([flipud(filter(B,A,flipud(x))) FilterUtilities.applyReverse(f2,x)]);

