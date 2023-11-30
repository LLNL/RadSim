function writeKML(filename,title,coord,intensity,style)

fid=fopen(filename,'w');
fprintf(fid,'<kml xmlns:xsd="http://schemas.opengis.net/kml/2.2.0/ogckml22.xsd" xmlns:xmlns="http://www.opengis.net/kml/2.2/" version="1.0">\n');
fprintf(fid,'  <Document>\n');
fprintf(fid,'    <name>%s</name>\n',title);
fprintf(fid,'    <open>1</open>\n');
fprintf(fid,'    <Folder>\n');
fprintf(fid,'      <name>SpatialPointsDataFrame</name>\n');

for i=1:style.styles
  color=dec2hex(style.alpha*256*256*256+...
    floor(style.cmap(i,3)*255)*256*256+...
    floor(style.cmap(i,2)*255)*256+...
    floor(style.cmap(i,1)*255));
fprintf(fid,'      <Style id="pnt%d">\n',i);
%fprintf(fid,'        <LabelStyle>\n');
%fprintf(fid,'          <scale>%f</scale>\n',style.sizes(i));
%fprintf(fid,'        </LabelStyle>\n');
fprintf(fid,'        <IconStyle>\n');
fprintf(fid,'          <color>#%s</color>\n',color);
fprintf(fid,'          <scale>%f</scale>\n',style.scale(i));
fprintf(fid,'          <Icon>\n');
fprintf(fid,'            <href>http://maps.google.com/mapfiles/kml/pal2/icon18.png</href>\n');
fprintf(fid,'          </Icon>\n');
fprintf(fid,'        </IconStyle>\n');
%fprintf(fid,'        <BalloonStyle>\n');
%fprintf(fid,'          <text>$[description]</text>\n');
%fprintf(fid,'        </BalloonStyle>\n');
fprintf(fid,'      </Style>\n');
end

for i=1:size(coord,1)
fprintf(fid,'      <Placemark>\n');
fprintf(fid,'        <name></name>\n');
fprintf(fid,'        <styleUrl>#pnt%d</styleUrl>\n',intensity(i));
fprintf(fid,'        <Point>\n');
fprintf(fid,'          <extrude>1</extrude>\n');
fprintf(fid,'          <altitudeMode>clampToGround</altitudeMode>\n');
%C=textscan( coord{i} ,'%f%f%f%d','Delimiter',',');
%fprintf(fid,'          <coordinates>%f,%f,%f</coordinates>\n',C{2},C{1},C{3});
fprintf(fid,'          <coordinates>%f,%f,%f</coordinates>\n',coord(i,2),coord(i,1),coord(i,3));
%fprintf(fid,'          <coordinates>%s</coordinates>\n',coord{i});
fprintf(fid,'        </Point>\n');
fprintf(fid,'      </Placemark>\n');
end

fprintf(fid,'    </Folder>\n');
fprintf(fid,'  </Document>\n');
fprintf(fid,'</kml>\n');

fclose(fid);
