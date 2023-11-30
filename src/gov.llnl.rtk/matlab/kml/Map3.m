function map2(values,coordinates,minpt,maxpt,tag,style)

if nargin <6
    style=intensityStyle();
end
if nargin < 5
  tag='counts'
end
if nargin < 4
  maxpt = size(values,2);
end
if nargin < 3
  minpt = 1;
end

ftag=sprintf('%s_%d_%d',tag,minpt,maxpt);

ds = sum(values(:,minpt:maxpt),1);
%ds = values;
mmm = min(ds);
ds=ds-mmm;
ds = floor(ds/max(ds)*63)+1;

coord = coordinates(minpt-4:maxpt-4,:);
%coord = [coord(:,2) coord(:,1) coord(:,3)];

writeKML(strcat('~/Desktop/TFS3_',ftag,'.kml'),tag,coord,ds,style);
fprintf('%s\n',tag);
end