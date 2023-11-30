/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.nio.file.Path;
import java.nio.file.Paths;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author her1
 */
public class H2ConverterNGTest
{
  
  public H2ConverterNGTest()
  {
  }

  @BeforeClass
  public static void setUpClass() throws Exception
  {
  }

  @AfterClass
  public static void tearDownClass() throws Exception
  {
  }

  @BeforeMethod
  public void setUpMethod() throws Exception
  {
  }

  @AfterMethod
  public void tearDownMethod() throws Exception
  {
  }

  /**
   * Test of toCSV method, of class H2Converter.
   */
  @Test
  public void testToCSV_3args_1() throws Exception
  {
    // Method calls toCSV(String a, Path b, String c) which is tested below
  }

  /**
   * Test of toCSV method, of class H2Converter.
   */
  @Test
  public void testToCSV_3args_2() throws Exception
  {
    String h2ConnectStr = "jdbc:h2:./test/resources/lc;DATABASE_TO_UPPER=FALSE";
    Path csvOutPath = Paths.get("./test/resources/test.csv");
    String tableName = "\"Info_Manipulation\"";
    H2Converter.toCSV(h2ConnectStr, csvOutPath, tableName);
  }
  
}
