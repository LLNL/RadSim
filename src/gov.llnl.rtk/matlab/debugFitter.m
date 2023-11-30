import gov.llnl.utility.*;
import gov.llnl.math.*;
import gov.llnl.rtk.calibration.*;
import java.nio.file.*;

gf1=SerializationUtilities.load(Paths.get('.','gf196644488.ser.gz'));
gf1.regionStart=38;
gf1.regionEnd=1000;
gf1.fitPeak(gf1.input)
