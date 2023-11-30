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
import gov.nist.physics.n42.data.DerivedData;
import gov.nist.physics.n42.data.DoseRate;
import gov.nist.physics.n42.data.ExposureRate;
import gov.nist.physics.n42.data.GrossCounts;
import gov.nist.physics.n42.data.Spectrum;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "DerivedData",
        order = Reader.Order.SEQUENCE,
        cls = DerivedData.class,
        typeName = "DerivedDataType")
@Reader.Attribute(name = "id", type = String.class, required = true)
public class DerivedDataReader extends ObjectReader<DerivedData>
{

  @Override
  public DerivedData start(ReaderContext context, Attributes attr) throws ReaderException
  {
    DerivedData out = new DerivedData();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<DerivedData> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    //<xsd:element ref="n42:MeasurementClassCode" minOccurs="1" maxOccurs="1"/>
    builder.element("MeasurementClassCode").callString(DerivedData::setMeasurementClassCode).required();
    //<xsd:element ref="n42:StartDateTime" minOccurs="1" maxOccurs="1"/>
    builder.element("StartDateTime").callString(DerivedData::setStartDateTime).required();
    //<xsd:element ref="n42:RealTimeDuration" minOccurs="1" maxOccurs="1"/>
    builder.element("RealTimeDuration").callString(DerivedData::setRealTimeDuration).required();
    
    //<xsd:element ref="n42:Spectrum" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("Spectrum").call(DerivedData::addSpectrum, Spectrum.class).optional().unbounded();
    //<xsd:element ref="n42:GrossCounts" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("GrossCounts").call(DerivedData::addGrossCounts, GrossCounts.class).optional().unbounded();
    //<xsd:element ref="n42:DoseRate" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("DoseRate").call(DerivedData::addDoseRate, DoseRate.class).optional().unbounded();
    //<xsd:element ref="n42:TotalDose" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("TotalDose").call(DerivedData::addTotalDose, DoseRate.class).optional().unbounded();
    //<xsd:element ref="n42:ExposureRate" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("ExposureRate").call(DerivedData::addExposureRate, ExposureRate.class).optional().unbounded();
    //<xsd:element ref="n42:TotalExposure" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("TotalExposure").call(DerivedData::addTotalExposure, ExposureRate.class).optional().unbounded();
    //<xsd:element ref="n42:DerivedDataExtension" minOccurs="0" maxOccurs="unbounded"/>
    builder.reader(new ExtensionReader("DerivedDataExtension")).nop().optional().unbounded();
    return builder.getHandlers();
  }

}

//  <xsd:extension base="n42:ReqIdComplexObjectType">
//    <xsd:sequence>
//      <xsd:element ref="n42:MeasurementClassCode" minOccurs="1" maxOccurs="1"/>
//      <xsd:element ref="n42:StartDateTime" minOccurs="1" maxOccurs="1"/>
//      <xsd:element ref="n42:RealTimeDuration" minOccurs="1" maxOccurs="1"/>
//      <xsd:element ref="n42:Spectrum" minOccurs="0" maxOccurs="unbounded"/>
//      <xsd:element ref="n42:GrossCounts" minOccurs="0" maxOccurs="unbounded"/>
//      <xsd:element ref="n42:DoseRate" minOccurs="0" maxOccurs="unbounded"/>
//      <xsd:element ref="n42:TotalDose" minOccurs="0" maxOccurs="unbounded"/>
//      <xsd:element ref="n42:ExposureRate" minOccurs="0" maxOccurs="unbounded"/>
//      <xsd:element ref="n42:TotalExposure" minOccurs="0" maxOccurs="unbounded"/>
//      <xsd:element ref="n42:DerivedDataExtension" minOccurs="0" maxOccurs="unbounded"/>
//    </xsd:sequence>
//  </xsd:extension>
//</xsd:complexContent>
//</xsd:complexType>
