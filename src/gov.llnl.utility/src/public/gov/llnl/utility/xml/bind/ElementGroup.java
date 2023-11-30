/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.xml.DomBuilder;
import gov.llnl.utility.xml.bind.Reader.Option;
import java.util.EnumSet;

/**
 *
 * @author nelson85
 */
public interface ElementGroup
{
  DomBuilder createSchemaGroup(DomBuilder type);

  EnumSet<Option> getElementOptions();

  /**
   * @return the parent
   */
  ElementGroup getParent();

}
