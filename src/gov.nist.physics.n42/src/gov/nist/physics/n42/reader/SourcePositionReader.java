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
import gov.nist.physics.n42.data.ComplexObject;
import gov.nist.physics.n42.data.GeographicPoint;
import gov.nist.physics.n42.data.LocationDescription;
import gov.nist.physics.n42.data.Origin;
import gov.nist.physics.n42.data.Quantity;
import gov.nist.physics.n42.data.RelativeLocation;
import gov.nist.physics.n42.data.SourcePosition;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "SourcePosition",
        order = Reader.Order.SEQUENCE,
        cls = SourcePosition.class,
        typeName = "SourcePositionType")
public class SourcePositionReader extends ObjectReader<SourcePosition>
{

  @Override
  public SourcePosition start(ReaderContext context, Attributes attr) throws ReaderException
  {
    SourcePosition out = new SourcePosition();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<SourcePosition> builder = this.newBuilder();
        builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    ReaderBuilder<SourcePosition> grp = builder.group(Order.CHOICE, Option.REQUIRED);
    grp.element("GeographicPoint").call(SourcePosition::setValue, GeographicPoint.class);
    grp.element("LocationDescription").call(SourcePosition::setValue, LocationDescription.class);
    grp.element("RelativeLocation").call(SourcePosition::setValue, RelativeLocation.class);
    return builder.getHandlers();
  }

}

//    <xsd:sequence>
//      <xsd:choice minOccurs="1" maxOccurs="1">
//        <xsd:element ref="n42:GeographicPoint" minOccurs="0" maxOccurs="1"/>
//        <xsd:element ref="n42:LocationDescription" minOccurs="0" maxOccurs="1"/>
//        <xsd:element ref="n42:RelativeLocation" minOccurs="0" maxOccurs="1"/>
//      </xsd:choice>
//    </xsd:sequence>
