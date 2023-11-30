import gov.llnl.math.random.*;


random=BinomialRandom(1000,0.3);

out=random.draw(10000);
hist(out,[0:1000]);

binoqq(out,1000,0.3);

