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

if minpt < 5
   minpt=5; 
end
ftag=sprintf('%s_%d_%d',tag,minpt,maxpt);

%ds = sum(values(:,minpt:maxpt),1);
%ds = values;
ds=values(:,minpt:maxpt);
mmm = min(ds);
ds=ds-mmm;
ds = floor(ds/max(ds)*63)+1;

coord = coordinates(minpt-2:maxpt-2,:);
%coord = [coord(:,2) coord(:,1) coord(:,3)];

writeKML(strcat('~/Desktop/TFS3_',ftag,'.kml'),tag,coord,ds,style);
fprintf('%s\n',tag);
end