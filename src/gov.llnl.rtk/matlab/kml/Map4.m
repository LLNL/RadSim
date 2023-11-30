function map2(values,coordinates,tag,style)

if nargin <4
    style=intensityStyle();
end
if nargin < 3
  tag='counts'
end

ftag=sprintf('%s',tag);

%ds = sum(values(:,minpt:maxpt),1);
%ds = values;
ds=values(:,:);
mmm = min(ds);
ds=ds-mmm;
ds = floor(ds/max(ds)*63)+1;

coord = coordinates(:,:);
writeKML(strcat('~/Desktop/RDAK_',ftag,'.kml'),tag,coord,ds,style);
fprintf('%s\n',tag);
end