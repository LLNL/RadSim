import gov.llnl.math.spline.*;

channel=[0, 32, 82, 202, 488, 860, 1024]';
energy=[0, 100, 238, 583, 1460, 2614, 3120]';

cs=NaturalCubicSpline.create(channel,energy)


x=[-200:1143]' ;


cs2=mkNCS(channel, energy);
%plot(x,[filter([1 -1],1,cs.interpolate(x)) filter([1 -1],1,interpNCS(cs2, x))]);
 cs.endBehavior=CubicSpline.CLAMP;
y1=cs.interpolate(x);
 cs.endBehavior=CubicSpline.LINEAR;
y2=cs.interpolate(x);
 cs.endBehavior=CubicSpline.CUBIC;
y3=cs.interpolate(x);
plot(x,[y1 y2 y3]);


