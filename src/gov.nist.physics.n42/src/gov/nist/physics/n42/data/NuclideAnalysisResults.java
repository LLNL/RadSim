/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.NuclideAnalysisResultsReader;
import gov.nist.physics.n42.writer.NuclideAnalysisResultsWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(NuclideAnalysisResultsReader.class)
@WriterInfo(NuclideAnalysisResultsWriter.class)
public class NuclideAnalysisResults extends ComplexObject
{
  private final List<Nuclide> nuclides = new ArrayList<>();

  public void addNuclide(Nuclide nuclide)
  {
    this.nuclides.add(nuclide);
  }

  public List<Nuclide> getNuclides()
  {
    return nuclides;
  }

}
