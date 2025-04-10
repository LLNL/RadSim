/*
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class NuclidesNGTest
{

  public NuclidesNGTest()
  {
  }

  @Test
  public void testGet_String()
  {
    assertEquals(Nuclides.get("Pu239").toString(), "Pu239");
    assertEquals(Nuclides.get("Tc99m").toString(), "Tc99m");
    assertEquals(Nuclides.get("99TC").toString(), "Tc99");
    assertEquals(Nuclides.get("99TCM").toString(), "Tc99m");
    assertEquals(Nuclides.get("Tc-99m").toString(), "Tc99m");
    assertEquals(Nuclides.get("38KM").toString(), "K38m");
    assertEquals(Nuclides.get("38Km").toString(), "K38m");
    assertEquals(Nuclides.get("K-38m").toString(), "K38m");
    assertEquals(Nuclides.get("Y-80m2").toString(), "Y80m2");
    assertEquals(Nuclides.get("Y80m2").toString(), "Y80m2");
    assertEquals(Nuclides.get("80YM2").toString(), "Y80m2");
  }

  @Test
  public void testGet_3args()
  {
      Element Tc = Elements.get("Tc");
      assertEquals(Nuclides.get(Tc.getAtomicNumber(), 99, 1).toString(), "Tc99m");
  }

  @Test
  public void testGetIsomers()
  {
    List<Nuclide> isomers = Nuclides.getIsomers(39, 80);
    assertEquals(isomers.get(0).toString(), "Y80");
    assertEquals(isomers.get(1).toString(), "Y80m");
    assertEquals(isomers.get(2).toString(), "Y80m2");
  }


}
