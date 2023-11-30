/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.nist.physics.n42.data.GeographicPoint;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.StateVector;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.LocationDescription;
import gov.nist.physics.n42.data.Orientation;
import gov.nist.physics.n42.data.Quantity;
import gov.nist.physics.n42.data.RelativeLocation;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "StateVector",
        order = Reader.Order.SEQUENCE,
        cls = StateVector.class,
        typeName = "StateVectorType")
public class StateVectorReader extends ObjectReader<StateVector>
{

  @Override
  public StateVector start(ReaderContext context, Attributes attr)
  {
    StateVector out = new StateVector();
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<StateVector> builder = this.newBuilder();
    ReaderBuilder<StateVector> choice = builder.group(Order.CHOICE, Option.OPTIONAL);
    choice.element("GeographicPoint").call(StateVector::setCoordinate, GeographicPoint.class);
    choice.element("LocationDescription").call(StateVector::setLocationDescription, LocationDescription.class);
    choice.element("RelativeLocation").call(StateVector::setRelativeLocation, RelativeLocation.class);
    builder.element("Orientation").call(StateVector::setOrientation, Orientation.class).optional();
    builder.element("SpeedValue").call(StateVector::setSpeed, Quantity.class).optional();
    //<xsd:choice minOccurs="0" maxOccurs="1">
    //<xsd:element ref="n42:GeographicPoint" minOccurs="0" maxOccurs="1"/>
    //<xsd:element ref="n42:LocationDescription" minOccurs="0" maxOccurs="1"/>
    //<xsd:element ref="n42:RelativeLocation" minOccurs="0" maxOccurs="1"/>
    //</xsd:choice>
    //<xsd:element ref="n42:Orientation" minOccurs="0" maxOccurs="1"/>
    //<xsd:element ref="n42:SpeedValue" minOccurs="0" maxOccurs="1"/>

    return builder.getHandlers();
  }

}
