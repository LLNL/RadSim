/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.writer;

import gov.llnl.utility.io.ReaderException;
import gov.nist.physics.n42.data.RadInstrumentData;
import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.DocumentReader;
import gov.llnl.utility.xml.bind.DocumentWriter;
import gov.llnl.utility.xml.bind.ObjectWriter;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.AnalysisResults;
import gov.nist.physics.n42.data.DerivedData;
import gov.nist.physics.n42.data.EfficiencyCalibration;
import gov.nist.physics.n42.data.EnergyCalibration;
import gov.nist.physics.n42.data.EnergyWindows;
import gov.nist.physics.n42.data.FWHMCalibration;
import gov.nist.physics.n42.data.RadDetectorInformation;
import gov.nist.physics.n42.data.RadItemInformation;
import gov.nist.physics.n42.data.RadMeasurement;
import gov.nist.physics.n42.data.RadMeasurementGroup;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Writer for RadInstrumentData.
 *
 */
public class RadInstrumentDataWriter extends ObjectWriter<RadInstrumentData>
{
  public RadInstrumentDataWriter()
  {
    super(Options.NONE, "RadInstrumentData", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, RadInstrumentData object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());

    if (object.getDocUUID() != null)
      attributes.add("n42DocUUID", object.getDocUUID());
    if (object.getDocDateTime() != null)
      attributes.add("n42DocDateTime", object.getDocDateTime());
  }

  @Override
  public void contents(RadInstrumentData object) throws WriterException
  {
    // Set double marshaller
    DoubleMarshaller marshall = new DoubleMarshaller();
    getContext().setMarshaller(marshall);
    
    // Fix referencing first
    object.getAnalysisResults().forEach(p -> p.visitReferencedObjects(WriterUtilities::ensureId));
    object.getMeasurements().forEach(p -> p.visitReferencedObjects(WriterUtilities::ensureId));
    object.getDerivedData().forEach(p -> p.visitReferencedObjects(WriterUtilities::ensureId));

    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);

    if (object.getDataCreatorName() != null)
    {
      builder.element("RadInstrumentDataCreatorName").putString(object.getDataCreatorName());
    }

    builder.element("RadInstrumentInformation").writer(new RadInstrumentInformationWriter()).put(object.getInstrument());

    WriteObject<RadDetectorInformation> wo1 = builder
            .element("RadDetectorInformation")
            .writer(new RadDetectorInformationWriter());
    wo1.putAll(object.getDetectors());

    WriteObject<RadItemInformation> wo2 = builder
            .element("RadItemInformation")
            .writer(new RadItemInformationWriter());
    wo2.putAll(object.getItems());

    if (!object.getEnergyCalibration().isEmpty())
    {
      WriteObject<EnergyCalibration> wo5 = builder
              .element("EnergyCalibration")
              .writer(new EnergyCalibrationWriter());
      wo5.putAll(object.getEnergyCalibration());
    }

    if (!object.getFWHMCalibration().isEmpty())
    {
      WriteObject<FWHMCalibration> wo6 = builder
              .element("FWHMCalibration")
              .writer(new FWHMCalibrationWriter());
      wo6.putAll(object.getFWHMCalibration());
    }

    if (!object.getTotalEfficiencyCalibration().isEmpty())
    {
      WriteObject<EfficiencyCalibration> wo7 = builder
              .element("TotalEfficiencyCalibration")
              .writer(new EfficiencyCalibrationWriter());
      wo7.putAll(object.getTotalEfficiencyCalibration());
    }

    if (!object.getFullEnergyPeakEfficiencyCalibration().isEmpty())
    {
      WriteObject<EfficiencyCalibration> wo8 = builder
              .element("FullEnergyPeakEfficiencyCalibration")
              .writer(new EfficiencyCalibrationWriter());
      wo8.putAll(object.getFullEnergyPeakEfficiencyCalibration());
    }

    if (!object.getIntrinsicFullEnergyPeakEfficiencyCalibration().isEmpty())
    {
      WriteObject<EfficiencyCalibration> wo9 = builder
              .element("IntrinsicFullEnergyPeakEfficiencyCalibration")
              .writer(new EfficiencyCalibrationWriter());
      wo9.putAll(object.getIntrinsicFullEnergyPeakEfficiencyCalibration());
    }

    if (!object.getIntrinsicSingleEscapePeakEfficiencyCalibration().isEmpty())
    {
      WriteObject<EfficiencyCalibration> wo10 = builder
              .element("IntrinsicSingleEscapePeakEfficiencyCalibration")
              .writer(new EfficiencyCalibrationWriter());
      wo10.putAll(object.getIntrinsicSingleEscapePeakEfficiencyCalibration());
    }

    if (!object.getIntrinsicDoubleEscapePeakEfficiencyCalibration().isEmpty())
    {
      WriteObject<EfficiencyCalibration> wo11 = builder
              .element("IntrinsicDoubleEscapePeakEfficiencyCalibration")
              .writer(new EfficiencyCalibrationWriter());
      wo11.putAll(object.getIntrinsicDoubleEscapePeakEfficiencyCalibration());
    }

    if (!object.getEnergyWindows().isEmpty())
    {
      WriteObject<EnergyWindows> wo12 = builder
              .element("EnergyWindows")
              .writer(new EnergyWindowsWriter());
      wo12.putAll(object.getEnergyWindows());
    }
    
    if (!object.getMeasurementGroups().isEmpty())
    {
      WriteObject<RadMeasurementGroup> wo4 = builder
              .element("RadMeasurementGroup")
              .writer(new RadMeasurementGroupWriter());
      wo4.putAll(object.getMeasurementGroups());
    }

    if (!object.getMeasurements().isEmpty())
    {
      WriteObject<RadMeasurement> wo3 = builder
              .element("RadMeasurement")
              .writer(new RadMeasurementWriter());
      wo3.putAll(object.getMeasurements());
    }

    if (!object.getDerivedData().isEmpty())
    {
      WriteObject<DerivedData> wo13 = builder
              .element("DerivedData")
              .writer(new DerivedDataWriter());
      wo13.putAll(object.getDerivedData());
    }

    if (!object.getAnalysisResults().isEmpty())
    {
      WriteObject<AnalysisResults> wo14 = builder
              .element("AnalysisResults")
              .writer(new AnalysisResultsWriter());
      wo14.putAll(object.getAnalysisResults());
    }

    // RadInstrumentDataExtension
  }

}
