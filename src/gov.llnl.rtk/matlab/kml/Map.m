function map(out2,i,minpt,maxpt)
tag='';
if nargin < 4
  maxpt = size(out2{i}.D,2);
end
if nargin < 3
  minpt = 1;
end

ds = sum(out2{i}.D(:,minpt:maxpt),1);
ds = floor(ds/max(ds)*63)+1;

coord = [out2{i}.coord(minpt:maxpt,2) out2{i}.coord(minpt:maxpt,1) out2{i}.altitude(minpt:maxpt)];

writeKML(strcat('~/Desktop/',out2{i}.label,tag,'.kml'),'Counts',coord,ds,intensityStyle());

end