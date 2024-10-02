/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.RadInstrumentState;
import gov.nist.physics.n42.data.StateVector;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.Characteristics;
import gov.nist.physics.n42.data.ComplexObject;
import gov.nist.physics.n42.data.Fault;
import gov.nist.physics.n42.data.RadInstrumentInformation;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "RadInstrumentState",
        order = Reader.Order.SEQUENCE,
        cls = RadInstrumentState.class,
        typeName = "RadInstrumentStateType")
@Reader.Attribute(name = "id", type = String.class, required = false)
@Reader.Attribute(name = "radInstrumentInformationReference", type = String.class)
public class RadInstrumentStateReader extends ObjectReader<RadInstrumentState>
{
  @Override
  public RadInstrumentState start(ReaderContext context, Attributes attr) throws ReaderException
  {
    RadInstrumentState out = new RadInstrumentState();
    ReaderUtilities.register(context, out, attr);
    String radInstrumentInformationReference = attr.getValue("radInstrumentInformationReference");
    if (radInstrumentInformationReference != null)
    {
      out.setInstrument(context.get(radInstrumentInformationReference, RadInstrumentInformation.class));
    }
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<RadInstrumentState> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    //  <xsd:element ref="n42:RadInstrumentModeCode" minOccurs="0" maxOccurs="1"/>
    builder.element("RadInstrumentModeCode")
            .callString(RadInstrumentState::setModeCode)
            .optional();
    //  <xsd:element ref="n42:RadInstrumentModeDescription" minOccurs="0" maxOccurs="1"/>
    builder.element("RadInstrumentModeDescription")
            .callString(RadInstrumentState::setModeDescription)
            .optional();
    //  <xsd:element ref="n42:StateVector" minOccurs="0" maxOccurs="1"/>
    builder.element("StateVector").call(RadInstrumentState::setStateVector, StateVector.class).optional();
    //  <xsd:element ref="n42:Fault" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("Fault").call(RadInstrumentState::addFault, Fault.class).optional().unbounded();
    //  <xsd:element ref="n42:RadInstrumentCharacteristics" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("RadInstrumentCharacteristics")
            .call(RadInstrumentState::addInstrumentCharacteristics, Characteristics.class)
            .optional().unbounded();
    //  <xsd:element ref="n42:RadInstrumentStateExtension" minOccurs="0" maxOccurs="unbounded"/>
    builder.reader(new ExtensionReader("RadInstrumentStateExtension"))
            .call((p, v) -> p.addExtension(v))
            .optional()
            .unbounded();
    return builder.getHandlers();
  }

}
