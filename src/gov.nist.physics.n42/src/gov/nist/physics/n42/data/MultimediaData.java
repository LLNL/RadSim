/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.nist.physics.n42.reader.MultimediaDataReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * A data type to provide data about a multimedia file, and optionally inclusion
 * of the file content within the instant xml document.
 *
 * @author nelson85
 */
@ReaderInfo(MultimediaDataReader.class)
public class MultimediaData extends ComplexObject
{
  private final List<RadItemInformation> items = new ArrayList<>();
  
  public void addRadItem(RadItemInformation item)
  {
    this.items.add(item);
  }
  
}
