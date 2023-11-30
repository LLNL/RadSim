/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import gov.llnl.utility.PackageResource;
import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.annotation.Singleton;
import gov.llnl.utility.xml.bind.Schema;
import java.util.logging.Logger;

/**
 *
 * @author nelson85
 */
@Singleton
@Schema(namespace = "http://math.llnl.gov",
        schema = "http://math.llnl.gov/schema/math.xsd",
        prefix = "math")
@Schema.Using(UtilityPackage.class)
public final class MathPackage extends PackageResource
{
  private static final MathPackage INSTANCE = new MathPackage();
  private static final Logger LOGGER = INSTANCE.getLogger();

  private MathPackage()
  {
  }

  static public MathPackage getInstance()
  {
    return INSTANCE;
  }

}
