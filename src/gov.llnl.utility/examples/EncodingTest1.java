/*
 * Copyright 2021, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */

import static gov.llnl.utility.ArrayEncoding.decodeDoubles;
import static gov.llnl.utility.ArrayEncoding.encodeDoubles;
import static gov.llnl.utility.ArrayEncoding.encodeDoublesAsFloats;
import java.text.ParseException;
import java.util.zip.DataFormatException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class EncodingTest1
{

  public EncodingTest1()
  {
  }

  // TODO add test methods here.
  // The methods must be annotated with annotation @Test. For example:
  //
  // @Test
  // public void hello() {}
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

//  @Test
//  public void base64EncodingTest()
//  {
//    try
//    {
//      // Test cases taken from WIKIPEDIA
//      String test = "any carnal pleasure.";
//      byte[] testB = test.getBytes("UTF-8");
//      assertEquals(base64Encode(testB, 0, testB.length), "YW55IGNhcm5hbCBwbGVhc3VyZS4=");
//      assertEquals(base64Encode(testB, 0, testB.length - 1), "YW55IGNhcm5hbCBwbGVhc3VyZQ==");
//      assertEquals(base64Encode(testB, 0, testB.length - 2), "YW55IGNhcm5hbCBwbGVhc3Vy");
//      assertEquals(base64Encode(testB, 0, testB.length - 3), "YW55IGNhcm5hbCBwbGVhc3U=");
//      assertEquals(base64Encode(testB, 0, testB.length - 4), "YW55IGNhcm5hbCBwbGVhcw==");
//
//      assertEquals(base64Encode(testB, testB.length - 9, testB.length), "cGxlYXN1cmUu");
//      assertEquals(base64Encode(testB, testB.length - 8, testB.length), "bGVhc3VyZS4=");
//      assertEquals(base64Encode(testB, testB.length - 7, testB.length), "ZWFzdXJlLg==");
//      assertEquals(base64Encode(testB, testB.length - 6, testB.length), "YXN1cmUu");
//      assertEquals(base64Encode(testB, testB.length - 5, testB.length), "c3VyZS4=");
//    }
//    catch (UnsupportedEncodingException ex)
//    {
//      Logger.getLogger(EncodingTest1.class.getName()).log(Level.SEVERE, null, ex);
//    }
//  }
//
//  @Test
//  public void base64DecodingTest()
//  {
//    byte[] b = new byte[]
//    {
//      (byte) 134, (byte) 56, (byte) 249, (byte) 24, (byte) 155, (byte) 35
//    };
//
//    for (int i = 1; i <= b.length; i++)
//    {
//      String e = base64Encode(b, 0, i);
//      byte[] b2 = base64Decode(e);
//      Assert.assertEquals(b2.length, i);
//      for (int j = 0; j < b2.length; j++)
//      {
//        Assert.assertEquals(b[j], b2[j]);
//      }
//    }
//  }
  @Test
  public void decodeDoublesTest() throws DataFormatException, ParseException
  {
    // Fill a test array
    double[] in = new double[200];
    for (int i = 0; i < 200; i++)
    {
      in[i] = 1.0 / (i + 101);
    }

    // Encode the data using each of the available formats
    String u1, u2;
    u1 = encodeDoubles(in);
    u2 = encodeDoublesAsFloats(in);

    // Decode the data
    double[] v1 = decodeDoubles(u1);
    double[] v2 = decodeDoubles(u2);

    // Verify the length is correct
    Assert.assertEquals(v1.length, in.length);
    Assert.assertEquals(v2.length, in.length);

    double err;
    // Check the error (some are lossy)
    err = 0;
    for (int i = 0; i < v1.length; ++i)
    {
      err += Math.abs((v1[i] - in[i]) / in[i]);
    }
    Assert.assertEquals(err, 0.0);

    err = 0;
    for (int i = 0; i < v2.length; ++i)
    {
      err += Math.abs((v2[i] - in[i]) / in[i]);
    }
    Assert.assertTrue(err / v2.length < 2e-7);
  }
}
