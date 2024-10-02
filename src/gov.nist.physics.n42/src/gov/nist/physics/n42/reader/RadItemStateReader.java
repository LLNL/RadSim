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
import gov.nist.physics.n42.data.RadItemInformation;
import gov.nist.physics.n42.data.RadItemState;
import gov.nist.physics.n42.data.StateVector;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "RadItemState",
        order = Reader.Order.SEQUENCE,
        cls = RadItemState.class,
        typeName = "RadItemStateType")
@Reader.Attribute(name = "id", type = String.class, required = false)

@Reader.Attribute(name = "radItemInformationReference", type = String.class, required = true)
public class RadItemStateReader extends ObjectReader<RadItemState>
{
  @Override
  public RadItemState start(ReaderContext context, Attributes attr) throws ReaderException
  {
    RadItemState out = new RadItemState();
     ReaderUtilities.register(context, out, attr);
    String reference = attr.getValue("radItemInformationReference");
    context.addDeferred(out, RadItemState::setItem, reference, RadItemInformation.class);
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<RadItemState> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    // <xsd:element ref="n42:StateVector" minOccurs="0" maxOccurs="1"/>
    builder.element("StateVector").call(RadItemState::setStateVector, StateVector.class)
            .required();
    // <xsd:element ref="n42:RadItemCharacteristics" minOccurs="0" maxOccurs="unbounded"/>
    builder.reader(new ExtensionReader("RadItemStateExtension"))
            .call((p, v) -> p.addExtension(v))
            .optional()
            .unbounded();
    return builder.getHandlers();
  }
}
