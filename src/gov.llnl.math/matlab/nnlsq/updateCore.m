% Converts the core files we have archived to the current version

import gov.llnl.utility.*;
import java.nio.file.*;

serializer=Serializer();
serializer.renameClass('gov.llnl.math.algebra.Nnlsq','gov.llnl.math.Nnlsq')
files=dir('*.ser');
for i=1:length(files)
  serializer.convert(Paths.get(files(i).folder, files(i).name));
end

