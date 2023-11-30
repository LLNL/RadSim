/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.SpectrumPeakAnalysisResultsReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.SpectrumPeakAnalysisResultsWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(SpectrumPeakAnalysisResultsReader.class)
@WriterInfo(SpectrumPeakAnalysisResultsWriter.class)
public class SpectrumPeakAnalysisResults extends ComplexObject
{
  private final List<SpectrumPeak> peaks = new ArrayList<>();

  public List<SpectrumPeak> getPeaks()
  {
    return peaks;
  }

  public void addPeak(SpectrumPeak peak)
  {
    this.peaks.add(peak);
  }

}
