/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class N42PackageNGTest
{
  
  public N42PackageNGTest()
  {
  }

  @Test
  public void testGetInstance()
  {
    N42Package result = N42Package.getInstance();
    assertNotNull(result);
  }
  
}
