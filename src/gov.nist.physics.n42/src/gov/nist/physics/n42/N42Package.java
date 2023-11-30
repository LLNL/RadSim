/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42;

import gov.llnl.utility.PackageResource;
import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.xml.bind.Schema;
import gov.llnl.utility.xml.bind.SchemaManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This singleton holds the information on this package.
 *
 * Java does not allow directly accessing a classes package. Thus we need a
 * singleton to hold package related variables.
 *
 * @author nelson85
 */
@Schema(namespace = "http://physics.nist.gov/N42/2011/N42",
        schema = "http://physics.nist.gov/n42/schema/n42.xsd",
        prefix = "n42")
@Schema.Using(UtilityPackage.class)
public class N42Package extends PackageResource
{
  final static public N42Package SELF = new N42Package();

  private N42Package()
  {
    try
    {
      URL schema = this.getClass().getClassLoader().getResource("gov/nist/physics/n42/schema/n42.xsd");
      SchemaManager.getInstance().alias(new URI("http://physics.nist.gov/N42/2011/n42.xsd"), schema);
    }
    catch (URISyntaxException ex)
    {
      Logger.getLogger(N42Package.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  static public N42Package getInstance()
  {
    return SELF;
  }
}
