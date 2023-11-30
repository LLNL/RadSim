/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.euclidean;

import gov.llnl.math.MathPackage;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = MathPackage.class, name = "versor",
        cls = Versor.class,
        referenceable = true,
        contents = Reader.Contents.NONE)
@Reader.Attribute(name = "x", type = double.class)
@Reader.Attribute(name = "y", type = double.class)
@Reader.Attribute(name = "z", type = double.class)
@Reader.Attribute(name = "angle", type = double.class)
public class VersorReader extends ObjectReader<Versor>
{
  @Override
  public Versor start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    double x = Double.parseDouble(attributes.getValue("x"));
    double y = Double.parseDouble(attributes.getValue("y"));
    double z = Double.parseDouble(attributes.getValue("z"));
    double angle = Double.parseDouble(attributes.getValue("angle"));
    return Versor.of(Vector3.of(x, y, z), angle);
  }

}
