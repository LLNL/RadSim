/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.nist.physics.n42.data.GeographicPoint;
import gov.nist.physics.n42.N42Package;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.Quantity;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "GeographicPoint",
        order = Reader.Order.SEQUENCE,
        cls = GeographicPoint.class,
        typeName = "GeographicPointType" )
public class GeographicPointReader extends ObjectReader<GeographicPoint>
{

  @Override
  public GeographicPoint start(ReaderContext context, Attributes attr)
  {
    GeographicPoint out = new GeographicPoint();
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  { 
    Reader.ReaderBuilder<GeographicPoint> builder = this.newBuilder();
    //  <xsd:element ref="n42:LatitudeValue" minOccurs="1" maxOccurs="1"/>
    builder.element("LatitudeValue").call(GeographicPoint::setLatitude, Quantity.class).required();
    //  <xsd:element ref="n42:LongitudeValue" minOccurs="1" maxOccurs="1"/>
    builder.element("LongitudeValue").call(GeographicPoint::setLongitude, Quantity.class).required();
    //  <xsd:element ref="n42:ElevationValue" minOccurs="0" maxOccurs="1"/>
    builder.element("ElevationValue").callDouble(GeographicPoint::setElevation).optional();
    //  <xsd:element ref="n42:ElevationOffsetValue" minOccurs="0" maxOccurs="1"/>
    builder.element("ElevationOffsetValue").callDouble(GeographicPoint::setElevationOffset).optional();
    //  <xsd:element ref="n42:GeoPointAccuracyValue" minOccurs="0" maxOccurs="1"/>
    builder.element("GeoPointAccuracyValue").callDouble(GeographicPoint::setGeoPointAccuracy).optional();
    //  <xsd:element ref="n42:ElevationAccuracyValue" minOccurs="0" maxOccurs="1"/>
    builder.element("ElevationAccuracyValue").callDouble(GeographicPoint::setElevationAccuracy).optional();
    //  <xsd:element ref="n42:ElevationOffsetAccuracyValue" minOccurs="0" maxOccurs="1"/>
    builder.element("ElevationOffsetAccuracyValue").callDouble(GeographicPoint::setElevationOffsetAccuracy).optional();
    return builder.getHandlers();
  }

}
