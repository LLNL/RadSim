/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.RadInstrumentQualityControlReader;
import gov.nist.physics.n42.writer.RadInstrumentQualityControlWriter;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(RadInstrumentQualityControlReader.class)
@WriterInfo(RadInstrumentQualityControlWriter.class)
public class RadInstrumentQualityControl extends ComplexObject
{
    private Instant inspectionDateTime;
  private boolean inCalibrationIndicator;
  
  /**
   * @return the inspectionDateTime
   */
  public Instant getInspectionDateTime()
  {
    return inspectionDateTime;
  }

  /**
   * @return the inCalibrationIndicator
   */
  public boolean isInCalibrationIndicator()
  {
    return inCalibrationIndicator;
  }


  public void setInspectionDateTime(Instant inspectionDateTime)
  {
    this.inspectionDateTime = inspectionDateTime;
  }

  public void setInCalibrationIndicator(boolean value)
  {
    this.inCalibrationIndicator = value;
  }
  
}
