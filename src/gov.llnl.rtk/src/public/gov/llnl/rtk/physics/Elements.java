/* 
 * Copyright (c) 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.utility.ReflectionUtilities;

/**
 * Library holding all elements.
 * 
 * This is a singleton.
 * 
 * @author nelson85
 */
public interface Elements
{
  /**
   * Get a element by name.
   *
   * @return
   */
  static Element get(String name)
  {
    return MANAGER.getElement(name);
  }

  static Element getElement(int z)
  {
    return MANAGER.getElement(z);
  }

//<editor-fold desc="internal" defaultstate="collapsed">
  interface ElementManager
  {
    Element getElement(String name);

    Element getElement(int z);
  }

  ElementManager MANAGER
          = ReflectionUtilities.getConstructor("gov.llnl.rtk.physics.ElementManagerImpl", ElementManager.class).get();
//</editor-fold>
}
