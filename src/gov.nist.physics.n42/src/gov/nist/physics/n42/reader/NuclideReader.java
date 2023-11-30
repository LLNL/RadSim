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
import gov.nist.physics.n42.data.Nuclide;
import gov.nist.physics.n42.data.NuclideIdentificationConfidenceDescription;
import gov.nist.physics.n42.data.NuclideIdentificationConfidenceUncertainty;
import gov.nist.physics.n42.data.NuclideIdentificationConfidenceValue;
import gov.nist.physics.n42.data.Quantity;
import gov.nist.physics.n42.data.SourceGeometryCode;
import gov.nist.physics.n42.data.SourcePosition;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "Nuclide",
        order = Reader.Order.SEQUENCE,
        cls = Nuclide.class,
        typeName = "NuclideType")
@Reader.Attribute(name = "id", type = String.class, required = false)
public class NuclideReader extends ObjectReader<Nuclide>
{
  @Override
  public Nuclide start(ReaderContext context, Attributes attr) throws ReaderException
  {
    Nuclide out = new Nuclide();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {

    Reader.ReaderBuilder<Nuclide> builder = this.newBuilder();
        builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("NuclideIdentifiedIndicator")
            .callBoolean(Nuclide::setIdentifiedIndicator)
            .required();
    builder.element("NuclideName")
            .callString(Nuclide::setName)
            .required();
    builder.element("NuclideActivityValue")
            .call(Nuclide::setActivity, Quantity.class).optional();
    builder.element("NuclideActivityUncertaintyValue")
            .call(Nuclide::setActivityUncertainty, Quantity.class)
            .optional();
    builder.element("NuclideMinimumDetectableActivityValue")
            .call(Nuclide::setMinimumDetectableActivity, Quantity.class)
            .optional();

    ReaderBuilder<Nuclide> grp = builder.group(Order.CHOICE, Option.OPTIONAL, Option.UNBOUNDED);
    grp.element("NuclideIDConfidenceDescription")
            .callString((Nuclide p, String v) -> p.addConfidence(new NuclideIdentificationConfidenceDescription(v)));
    grp.element("NuclideIDConfidenceValue")
            .callDouble((Nuclide p, Double v) -> p.addConfidence(new NuclideIdentificationConfidenceValue(v)));
    grp.element("NuclideIDConfidenceUncertaintyValue")
            .callDouble((Nuclide p, Double v) -> p.addConfidence(new NuclideIdentificationConfidenceUncertainty(v)));

    builder.element("NuclideCategoryDescription")
            .callString(Nuclide::setCategory)
            .optional();
    builder.element("NuclideSourceGeometryCode")
            .call(Nuclide::setSourceGeometryCode, SourceGeometryCode.class)
            .optional();
    builder.element("SourcePosition").call(Nuclide::setSourcePosition, SourcePosition.class).optional();
    builder.element("NuclideShieldingAtomicNumber")
            .callDouble(Nuclide::setShieldingAtomicNumber).optional();
    builder.element("NuclideShieldingArealDensityValue")
            .callDouble(Nuclide::setShieldingArealDensity).optional();
    builder.reader(new ExtensionReader("NuclideExtension")).nop().optional().unbounded();
    return builder.getHandlers();
  }

}
