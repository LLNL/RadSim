/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.nist.physics.n42.data.EnergyCalibration;
import gov.nist.physics.n42.N42Package;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.ComplexObject;
import java.time.Instant;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "EnergyCalibration",
        order = Reader.Order.SEQUENCE,
        cls = EnergyCalibration.class,
        typeName = "EnergyCalibrationType")
@Reader.Attribute(name = "id", type = String.class, required = true)
public class EnergyCalibrationReader extends ObjectReader<EnergyCalibration>
{
  @Override
  public EnergyCalibration start(ReaderContext context, Attributes attr) throws ReaderException
  {
    EnergyCalibration out = new EnergyCalibration();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    //    <xsd:sequence>
//      <xsd:choice minOccurs="1" maxOccurs="1">
//      </xsd:choice>
//      <xsd:element ref="n42:EnergyValues" minOccurs="0" maxOccurs="1"/>
//      <xsd:element ref="n42:EnergyDeviationValues" minOccurs="0" maxOccurs="1"/>
//    </xsd:sequence>
    ReaderBuilder<EnergyCalibration> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
//      <xsd:choice minOccurs="1" maxOccurs="1">
    ReaderBuilder<EnergyCalibration> grp = builder.group(Order.CHOICE, Option.REQUIRED);
//        <xsd:element ref="n42:CoefficientValues" minOccurs="0" maxOccurs="1"/>
    grp.element("CoefficientValues").call(EnergyCalibration::setCoefficients, double[].class);
//        <xsd:element ref="n42:EnergyBoundaryValues" minOccurs="0" maxOccurs="1"/>
    grp.element("EnergyBoundaryValues").call(EnergyCalibration::setEnergyBoundaries, double[].class);
//      <xsd:element ref="n42:EnergyValues" minOccurs="0" maxOccurs="1"/>
    grp.element("EnergyValues").call(EnergyCalibration::setEnergy, double[].class).optional();
//      <xsd:element ref="n42:EnergyDeviationValues" minOccurs="0" maxOccurs="1"/>
    grp.element("EnergyDeviationValues").call(EnergyCalibration::setEnergyDeviation, double[].class).optional();
//      <xsd:element ref="n42:CalibrationDateTime" minOccurs="0" maxOccurs="1"/>
    builder.element("CalibrationDateTime").call(EnergyCalibration::setCalibrationDateTime, Instant.class).optional();
    return builder.getHandlers();
  }

}
