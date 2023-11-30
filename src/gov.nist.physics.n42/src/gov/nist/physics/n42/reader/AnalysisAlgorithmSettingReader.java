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
import gov.nist.physics.n42.data.AnalysisAlgorithmSetting;
import gov.nist.physics.n42.data.ComplexObject;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "AnalysisAlgorithmSetting",
        order = Reader.Order.SEQUENCE,
        cls = AnalysisAlgorithmSetting.class,
        typeName = "AnalysisAlgorithmSettingType")
@Reader.Attribute(name = "id", type = String.class, required = false)
public class AnalysisAlgorithmSettingReader extends ObjectReader<AnalysisAlgorithmSetting>
{

  @Override
  public AnalysisAlgorithmSetting start(ReaderContext context, Attributes attr) throws ReaderException
  {
    AnalysisAlgorithmSetting out = new AnalysisAlgorithmSetting();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<AnalysisAlgorithmSetting> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    // <xsd:element ref="n42:AnalysisAlgorithmSettingName" minOccurs="1" maxOccurs="1"/>
    builder.element("AnalysisAlgorithmSettingName").callString(AnalysisAlgorithmSetting::setName).required();
    // <xsd:element ref="n42:AnalysisAlgorithmSettingValue" minOccurs="1" maxOccurs="1"/>
    builder.element("AnalysisAlgorithmSettingValue").callString(AnalysisAlgorithmSetting::setValue).required();
    // <xsd:element ref="n42:AnalysisAlgorithmSettingUnits" minOccurs="1" maxOccurs="1"/>
    builder.element("AnalysisAlgorithmSettingUnits").callString(AnalysisAlgorithmSetting::setUnits).required();
    return builder.getHandlers();
  }

}
