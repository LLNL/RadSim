function out=readstruct(FID, def)

names=fieldnames(def);
for i1=1:length(names)
  field=getfield(def, names{i1});

  count=1;
  trans=0;

  i2=findstr( field, '[');
  i3=findstr( field, ']');
  if ~isempty( i2 )
    count=str2num(field(i2+1:i3-1));
    field=field([1:i2-1 i3+1:end]);
  end

  i4=findstr( field, '''');
  if ~isempty( i4 )
    trans=1;
    field=field([1:i4-1 i4+1:end]);
  end

  i5=findstr( field, '=>');

  value=fread(FID, count, field);

  if trans==1
    eval(['out.' names{i1} '= transpose(value);']);
  else
    eval(['out.' names{i1} '= value;']);
  end
end
