function dumpFields(object)

names=fieldnames(object);
for i=1:length(names)
  field=names{i};
  value=getfield(object, field);
  type=object.getClass.getField(field).getType().getName();

  if isjava(value)
    fprintf('  %s: %s\n', field, value.toString());
  elseif prod(size(value))>1
      fprintf('  %s:\n    [', field);
      fprintf('%s ',value);
      fprintf(']\n',value);
  elseif type.equals('int')==1
      fprintf('  %s: %d\n', field, value);
  elseif type.equals('long')==1
      fprintf('  %s: %d\n', field, value);
  elseif type.equals('double')==1
      fprintf('  %s: %f\n', field, value);
  else
      fprintf('  %s: (%s) %s\n', field, type, value);
  end
end
