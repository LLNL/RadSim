/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.writer;

import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.ObjectWriter;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.AnalysisAlgorithmSetting;

/**
 *
 * @author her1
 */
public class AnalysisAlgorithmSettingWriter extends ObjectWriter<AnalysisAlgorithmSetting>
{
  public AnalysisAlgorithmSettingWriter()
  {
    super(Options.NONE, "AnalysisAlgorithmSetting", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, AnalysisAlgorithmSetting object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(AnalysisAlgorithmSetting object) throws WriterException
  {
    WriterBuilder builder = newBuilder();

    if (!object.getRemarks().isEmpty())
    {
      for (int i = 0; i < object.getRemarks().size(); ++i)
      {
        builder.element("Remark").putString(object.getRemarks().get(i));
      }
    }

    builder.element("AnalysisAlgorithmSettingName").putString(object.getName());
    builder.element("AnalysisAlgorithmSettingValue").putString(object.getValue());
    builder.element("AnalysisAlgorithmSettingUnits").putString(object.getUnits());
  }

}
