prand=gov.llnl.math.PoissonRandom();
out=prand.draw(10*ones(1000,1));

in=-50*log(rand(10000,4));
rep=100;

%tic;
%for i=1:rep
%  out=fpoisrand(in);
%end
%toc

tic;
for i=1:rep
  out=jpoisrand(in);
end
toc

tic;
for i=1:rep
  out=double(prand.draw(in(:)));
end
toc

