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
import gov.nist.physics.n42.data.Origin;
import gov.nist.physics.n42.data.Quantity;
import gov.nist.physics.n42.data.RelativeLocation;
import org.xml.sax.Attributes;

/**
 *
 * @author pham21
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "RelativeLocation",
        order = Reader.Order.SEQUENCE,
        cls = RelativeLocation.class,
        typeName = "RelativeLocationType")
public class RelativeLocationReader extends ObjectReader<RelativeLocation>
{

  @Override
  public RelativeLocation start(ReaderContext context, Attributes attr) throws ReaderException
  {
    RelativeLocation out = new RelativeLocation();
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    /*
    <xsd:element ref="n42:RelativeLocationAzimuthValue" minOccurs="0" maxOccurs="1"/>
    <xsd:element ref="n42:RelativeLocationInclinationValue" minOccurs="0" maxOccurs="1"/>
    <xsd:element ref="n42:DistanceValue" minOccurs="0" maxOccurs="1"/>
    <xsd:element ref="n42:Origin" minOccurs="1" maxOccurs="1"/>
    */
    Reader.ReaderBuilder<RelativeLocation> builder = this.newBuilder();
    builder.element("RelativeLocationAzimuthValue").call(RelativeLocation::setAzimuth, Quantity.class).optional();
    builder.element("RelativeLocationInclinationValue").call(RelativeLocation::setInclination, Quantity.class).optional();
    builder.element("DistanceValue").call(RelativeLocation::setDistance, Quantity.class).optional();
    builder.element("Origin").call(RelativeLocation::setOrigin, Origin.class ).required();
    return builder.getHandlers();
  }

}