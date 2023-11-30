/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.RadInstrumentVersionReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.RadInstrumentVersionWriter;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(RadInstrumentVersionReader.class)
@WriterInfo(RadInstrumentVersionWriter.class)
public class RadInstrumentVersion extends ComplexObject
{

  private final String componentName;
  private final String componentVersion;

  public RadInstrumentVersion(String componentName, String componentVersion)
  {
    this.componentName = componentName;
    this.componentVersion = componentVersion;
  }

  /**
   * @return the componentName
   */
  public String getComponentName()
  {
    return componentName;
  }

  /**
   * @return the componentVersion
   */
  public String getComponentVersion()
  {
    return componentVersion;
  }

}
