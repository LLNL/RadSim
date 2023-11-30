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
import gov.nist.physics.n42.data.Nuclide;
import gov.nist.physics.n42.data.NuclideIdentificationConfidenceDescription;
import gov.nist.physics.n42.data.NuclideIdentificationConfidenceUncertainty;
import gov.nist.physics.n42.data.NuclideIdentificationConfidenceValue;
import gov.nist.physics.n42.data.Quantity;

/**
 *
 * @author her1
 */
public class NuclideWriter extends ObjectWriter<Nuclide>
{
  public NuclideWriter()
  {
    super(Options.NONE, "Nuclide", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, Nuclide object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(Nuclide object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    builder.element("NuclideIdentifiedIndicator").putBoolean(object.isIdentifiedIndicator());
    builder.element("NuclideName").putString(object.getName());

    if (object.getActivity() != null)
    {
      builder.element("NuclideActivityValue").contents(Quantity.class).put(object.getActivity());
    }

    if (object.getActivityUncertainty() != null)
    {
      builder.element("NuclideActivityUncertaintyValue").contents(Quantity.class).put(object.getActivityUncertainty());
    }

    if (object.getMinimumDetectableActivity() != null)
    {
      builder.element("NuclideMinimumDetectableActivityValue").contents(Quantity.class).put(object.getMinimumDetectableActivity());
    }

    if (!object.getConfidence().isEmpty())
    {
      for (int i = 0; i < object.getConfidence().size(); ++i)
      {
        if (object.getConfidence().get(i).getClass().equals(gov.nist.physics.n42.data.NuclideIdentificationConfidenceDescription.class))
        {
          NuclideIdentificationConfidenceDescription obj = (NuclideIdentificationConfidenceDescription) object.getConfidence().get(i);
          builder.element("NuclideIDConfidenceDescription").putString(obj.getValue());
        }
        else if (object.getConfidence().get(i).getClass().equals(gov.nist.physics.n42.data.NuclideIdentificationConfidenceValue.class))
        {
          NuclideIdentificationConfidenceValue obj = (NuclideIdentificationConfidenceValue) object.getConfidence().get(i);
          builder.element("NuclideIDConfidenceValue").putString(Double.toString(obj.getValue()));
        }
        else if (object.getConfidence().get(i).getClass().equals(gov.nist.physics.n42.data.NuclideIdentificationConfidenceUncertainty.class))
        {
          NuclideIdentificationConfidenceUncertainty obj = (NuclideIdentificationConfidenceUncertainty) object.getConfidence().get(i);
          builder.element("NuclideIDConfidenceUncertaintyValue").putDouble(obj.getValue());
        }
      }
    }

    if (object.getCategory() != null)
    {
      builder.element("NuclideCategoryDescription").putString(object.getCategory());
    }

    if (object.getSourceGeometry() != null)
    {
      builder.element("NuclideSourceGeometryCode").putString(object.getSourceGeometry().toString());
    }

    if (object.getSourcePosition() != null)
    {
      builder.element("SourcePosition").writer(new SourcePositionWriter()).put(object.getSourcePosition());
    }

    if (object.getShieldingAtomicNumber() != null)
    {
      builder.element("NuclideShieldingAtomicNumber").putDouble(object.getShieldingAtomicNumber());
    }

    if (object.getShieldingArealDensity() != null)
    {
      builder.element("NuclideShieldingArealDensityValue").putDouble(object.getShieldingArealDensity());
    }
    // NuclideExtension
  }

}
