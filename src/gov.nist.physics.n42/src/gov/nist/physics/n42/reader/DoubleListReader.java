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
import org.xml.sax.Attributes;

@Reader.Declaration(pkg = N42Package.class, name = "NonNegativeDoubleList", 
        contents = Reader.Contents.TEXT, 
        cls=double[].class,
        typeName = "NonNegativeDoubleListType")
public class DoubleListReader extends ObjectReader<double[]>
{

  @Override
  public double[] start(ReaderContext context, Attributes attr)
  {
    return null;
  }

  @Override
  public double[] contents(ReaderContext context, String string)
  {
    return ReaderUtilities.doublesFromString(string);
  }
}
