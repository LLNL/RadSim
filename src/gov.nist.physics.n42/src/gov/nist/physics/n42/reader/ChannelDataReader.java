/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.utility.SpectrumUtilities;
import org.xml.sax.Attributes;

@Reader.Declaration(pkg = N42Package.class, 
        name = "ChannelData", 
        contents = Reader.Contents.TEXT, 
        cls=double[].class,
        typeName = "ChannelDataType")
@Reader.Attribute(name = "compressionCode", type = String.class)
public class ChannelDataReader extends ObjectReader<double[]>
{
  String compression = "None";

  @Override
  public double[] start(ReaderContext context, Attributes attr)
  {
    String compCode = attr.getValue("compressionCode");
    if (compCode != null)
      compression = compCode;
    return null;
  }

  @Override
  public double[] contents(ReaderContext context, String string)
  {
    double[] counts = null;
    if (compression.equals("CountedZeroes"))
    {
      // This fixed the issue of some N42s not following countedZeroes correctly.
      counts = ReaderUtilities.doublesFromCountedZerosString(string);
      counts = SpectrumUtilities.unpackCountedZeros(counts);
    }
    else{
      counts = ReaderUtilities.doublesFromString(string);
    }
    return counts;
  }
}
