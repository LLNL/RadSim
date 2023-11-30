/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.LocationDescription;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "LocationDescription",
        contents = Reader.Contents.TEXT,
        cls = LocationDescription.class,
        typeName = "LocationDescriptionType")
public class LocationDescriptionReader extends ObjectReader<LocationDescription>
{
    @Override
    public LocationDescription contents(ReaderContext context, String textContents) throws ReaderException
    {
      return new LocationDescription(textContents);
    }

  
}
