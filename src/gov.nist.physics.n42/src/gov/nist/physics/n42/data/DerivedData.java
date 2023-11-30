/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.DerivedDataReader;
import gov.nist.physics.n42.writer.DerivedDataWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(DerivedDataReader.class)
@WriterInfo(DerivedDataWriter.class)
public class DerivedData extends ComplexObject
{
  private String measurementClassCode;
  private String startDateTime;
  private String realTimeDuration;
  private final List<Spectrum> spectrum = new ArrayList<>();
  private final List<GrossCounts> grossCounts = new ArrayList<>();
  private final List<DoseRate> doseRate = new ArrayList<>();
  private final List<DoseRate> totalDose = new ArrayList<>();
  private final List<ExposureRate> exposureRate = new ArrayList<>();
  private final List<ExposureRate> totalExposure = new ArrayList<>();

  public String getMeasurementClassCode()
  {
    return measurementClassCode;
  }

  public String getStartDateTime()
  {
    return startDateTime;
  }

  public String getRealTimeDuration()
  {
    return realTimeDuration;
  }

  public List<Spectrum> getSpectrum()
  {
    return spectrum;
  }

  public List<GrossCounts> getGrossCounts()
  {
    return grossCounts;
  }

  public List<DoseRate> getDoseRate()
  {
    return doseRate;
  }

  public List<DoseRate> getTotalDose()
  {
    return totalDose;
  }

  public List<ExposureRate> getExposureRate()
  {
    return exposureRate;
  }

  public List<ExposureRate> getTotalExposure()
  {
    return totalExposure;
  }

  public void setMeasurementClassCode(String value)
  {
    this.measurementClassCode = value;
  }

  public void setStartDateTime(String value)
  {
    this.startDateTime = value;
  }

  public void setRealTimeDuration(String value)
  {
    this.realTimeDuration = value;
  }

  public void addSpectrum(Spectrum value)
  {
    this.spectrum.add(value);
  }

  public void addGrossCounts(GrossCounts value)
  {
    this.grossCounts.add(value);
  }

  public void addDoseRate(DoseRate value)
  {
    this.doseRate.add(value);
  }

  public void addTotalDose(DoseRate value)
  {
    this.totalDose.add(value);
  }

  public void addExposureRate(ExposureRate value)
  {
    this.exposureRate.add(value);
  }

  public void addTotalExposure(ExposureRate value)
  {
    this.totalExposure.add(value);
  }

  @Override
  public void visitReferencedObjects(Consumer<ComplexObject> visitor)
  {
    this.doseRate.forEach(p -> p.visitReferencedObjects(visitor));
    this.exposureRate.forEach(p -> p.visitReferencedObjects(visitor));
    this.grossCounts.forEach(p -> p.visitReferencedObjects(visitor));
    this.spectrum.forEach(p -> p.visitReferencedObjects(visitor));
    this.totalDose.forEach(p -> p.visitReferencedObjects(visitor));
    this.totalExposure.forEach(p -> p.visitReferencedObjects(visitor));
  }

}
