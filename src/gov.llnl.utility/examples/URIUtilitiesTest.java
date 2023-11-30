/*
 * Copyright 2021, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
import gov.llnl.utility.URIUtilities;
import static gov.llnl.utility.URIUtilities.exists;
import static gov.llnl.utility.URIUtilities.resolve;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author nelson85
 */
public class URIUtilitiesTest
{

  static public void main(String[] args) throws URISyntaxException
  {
    Object s = new URIUtilities();
    URL foo = s.getClass().getClassLoader().getResource("builder/example/resources/detector.xml");
    URI uri = foo.toURI();
    System.out.println(exists(uri));
    URI uri2 = resolve(uri, "bob");
    System.out.println(exists(uri2));
  }

}
