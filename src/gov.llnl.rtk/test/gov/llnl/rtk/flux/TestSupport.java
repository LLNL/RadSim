/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.flux;

import gov.llnl.utility.proto.MessageEncoding;
import gov.llnl.utility.proto.ProtoException;
import java.io.IOException;
import java.io.InputStream;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class TestSupport
{

  public static <T> T loadResource(String name, MessageEncoding<T> encoding)
  {
    try (final InputStream is = TestSupport.class.getClassLoader()
            .getResourceAsStream("gov/llnl/rtk/resources/" + name))
    {
      return encoding.parseStreamGZ(is);
    }
    catch (ProtoException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  void testResources()
  {
//    loadFluxResource("flux.bin");
    loadResource("fluxBinned.bin", FluxEncoding.getInstance());
//    loadFluxResource("fluxSpectrum.bin");
//    loadFluxResource("fluxTrapezoid.bin");
//    loadFluxResource("results/toSpectrum.bin");
//    loadFluxResource("results/toTrapezoid.bin");
  }

}
