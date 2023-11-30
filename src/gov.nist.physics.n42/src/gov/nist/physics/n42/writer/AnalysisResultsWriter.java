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
import gov.nist.physics.n42.data.AnalysisAlgorithmVersion;
import gov.nist.physics.n42.data.AnalysisResultStatusCode;
import gov.nist.physics.n42.data.AnalysisResults;
import gov.nist.physics.n42.data.Fault;
import gov.nist.physics.n42.data.RadAlarm;
import java.time.Instant;

/**
 *
 * @author her1
 */
public class AnalysisResultsWriter extends ObjectWriter<AnalysisResults>
{
  public AnalysisResultsWriter()
  {
    super(Options.NONE, "AnalysisResults", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, AnalysisResults object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());

    String radMeasurementRef = WriterUtilities.getObjectReferences(object.getMeasurements(), getContext());
    if (radMeasurementRef != null)
      attributes.add("radMeasurementReferences", radMeasurementRef);

    String radMeasurementGroupRef = WriterUtilities.getObjectReferences(object.getMeasurementGroups(), getContext());
    if (radMeasurementGroupRef != null)
      attributes.add("radMeasurementGroupReferences", radMeasurementGroupRef);

    // FIXME
    // attributes.add("derivedDataReferences", ""); 
  }

  @Override
  public void contents(AnalysisResults object) throws WriterException
  {
    WriterBuilder builder = newBuilder();

    WriterUtilities.writeRemarkContents(builder, object);

    if (object.getStartDateTime() != null)
    {
      builder.element("AnalysisStartDateTime")
              .contents(Instant.class)
              .put(object.getStartDateTime());
    }

    if (object.getAlgorithmName() != null)
    {
      builder.element("AnalysisAlgorithmName").putString(object.getAlgorithmName());
    }

    if (object.getAlgorithmCreatorName() != null)
    {
      builder.element("AnalysisAlgorithmCreatorName").putString(object.getAlgorithmCreatorName());
    }

    if (object.getAlgorithmVersion() != null)
    {
      WriteObject<AnalysisAlgorithmVersion> wo1 = builder
              .element("AnalysisAlgorithmVersion")
              .writer(new AnalysisAlgorithmVersionWriter());
      for (AnalysisAlgorithmVersion o : object.getAlgorithmVersion())
      {
        wo1.put(o);
      }
    }

    if (object.getAlgorithmSettings() != null)
    {
      WriteObject<AnalysisAlgorithmSetting> wo2 = builder
              .element("AnalysisAlgorithmSetting")
              .writer(new AnalysisAlgorithmSettingWriter());
      for (AnalysisAlgorithmSetting o : object.getAlgorithmSettings())
      {
        wo2.put(o);
      }
    }

    if (object.getStatusCode() != null)
    {
      builder.element("AnalysisResultStatusCode")
              .contents(AnalysisResultStatusCode.class)
              .put(object.getStatusCode());
    }

    if (object.getConfidence() != null)
    {
      builder.element("AnalysisConfidenceValue").putDouble(object.getConfidence());
    }

    if (object.getDescription() != null)
    {
      builder.element("AnalysisResultDescription").putString(object.getDescription());
    }

    if (object.getRadAlarm() != null)
    {
      WriteObject<RadAlarm> wo3 = builder
              .element("RadAlarm")
              .writer(new RadAlarmWriter());
      for (RadAlarm o : object.getRadAlarm())
      {
        wo3.put(o);
      }
    }

    if (object.getNuclideAnalysisResults() != null)
    {
      builder.element("NuclideAnalysisResults").writer(new NuclideAnalysisResultsWriter()).put(object.getNuclideAnalysisResults());
    }

    if (object.getSpectrumPeakAnalysisResults() != null)
    {
      builder.element("SpectrumPeakAnalysisResults").writer(new SpectrumPeakAnalysisResultsWriter()).put(object.getSpectrumPeakAnalysisResults());
    }

    if (object.getGrossCountAnalysisResults() != null)
    {
      builder.element("GrossCountAnalysisResults").writer(new GrossCountAnalysisResultsWriter()).put(object.getGrossCountAnalysisResults());
    }

    if (object.getDoseAnalysisResults() != null)
    {
      builder.element("DoseAnalysisResults").writer(new DoseAnalysisResultsWriter()).put(object.getDoseAnalysisResults());
    }

    if (object.getExposureAnalysisResults() != null)
    {
      builder.element("ExposureAnalysisResults").writer(new ExposureAnalysisResultsWriter()).put(object.getExposureAnalysisResults());
    }

    if (!object.getFaults().isEmpty())
    {
      WriteObject<Fault> wo4 = builder
              .element("Fault")
              .writer(new FaultWriter());
      for (Fault o : object.getFaults())
      {
        wo4.put(o);
      }
    }

    // AnalysisResultsExtension
  }

}
