import java.nio.file.*;
import gov.llnl.utility.*;
import gov.llnl.math.algebra.*;

% Create a nnlsq
nnlsq=Nnlsq();

% We already have core, so don't generate an initial one
nnlsq.DUMP_CORE=0;

% Use the serializer to load the previous core
serializer=Serializer();
input=serializer.load(Paths.get('.','nnlsq000b7949.ser'));

% Process the core
try
  x=nnlsq.solve(input);
catch ex
end
ex=ex.ExceptionObject;
context=ex.getContext();
regressorSet=context(1);

try
  throw "fail"
catch ex1
end
clear ex1;

