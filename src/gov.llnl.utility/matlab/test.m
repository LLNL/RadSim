% Import required packages
import java.nio.file.*;               % For Paths.get()
import java.lang.*;                   % For Integer, Double, Long
import gov.llnl.utility.io.tables.*;  % For TablesReader

% Set up the factory
drf=DelimitedReaderFactory();
drf.setDelimiter(',');
drf.setAutomaticFields(1); % Use the column names to get the keys

% Open the file
dr=drf.openFile(Paths.get('.', 'sample.csv'));

% Name the type of a few useful columns
dr.findField('Timestamp').type(Long.TYPE);
dr.findField('GammaCounts').type(Integer.TYPE);
dr.findField('NeutronCounts').type(Integer.TYPE);
dr.findField('Latitude').type(Double.TYPE);
dr.findField('Longitude').type(Double.TYPE);
dr.findField('Orientation').type(Double.TYPE);
dr.findField('Speed').type(Double.TYPE);
dr.findField('Metric').type(Double.TYPE);

% Print all of the available fields
fields=dr.getFields();
for i=1:length(fields)
  fprintf('"%s"\n', fields(i).getKey());
end

% Get a record
%if dr.hasNext()==1
%  rec=dr.next();
%end

% Test extractor
ext=TableExtractor.create(dr);
ext.select({'DeviceID', 'Timestamp', 'Metric'});
ext.fetchAll();

% Plot the metric
plot(ext.getColumn(2));

%  Notes:
%  Getting class names in java can be a challenge.  
%  The easiest for classes is java.lang.Class.forName('classname').
%  Note that java.lang. is required as Class can't be imported due to conflicts.
%  For primatives use java.lang.Integer.TYPE.  

