/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

/**
 *
 * @author nelson85
 */
import gov.nist.physics.n42.data.RadDetectorState;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.Characteristics;
import gov.nist.physics.n42.data.ComplexObject;
import gov.nist.physics.n42.data.Fault;
import gov.nist.physics.n42.data.RadDetectorInformation;
import gov.nist.physics.n42.data.StateVector;
import org.xml.sax.Attributes;

@Reader.Declaration(pkg = N42Package.class,
        name = "RadDetectorState",
        order = Reader.Order.SEQUENCE,
        cls =  RadDetectorState.class,
        typeName = "RadDetectorStateType")
@Reader.Attribute(name ="id", type=String.class, required=false)
@Reader.Attribute(name = "radDetectorInformationReference", type = String.class, required = true)
public class RadDetectorStateReader extends ObjectReader<RadDetectorState>
{
  @Override
  public RadDetectorState start(ReaderContext context, Attributes attr) throws ReaderException
  {
    RadDetectorState out = new RadDetectorState();
    ReaderUtilities.register(context, out, attr);
    String radDetectorInformationReference = attr.getValue("radDetectorInformationReference");
    out.setDetector(context.get(radDetectorInformationReference, RadDetectorInformation.class));
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<RadDetectorState> builder = this.newBuilder();
        builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    //  <xsd:element ref="n42:StateVector" minOccurs="0" maxOccurs="1"/>
    builder.element("StateVector").call(RadDetectorState::setState, StateVector.class).optional();
    //  <xsd:element ref="n42:Fault" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("Fault").call(RadDetectorState::addFault, Fault.class).optional().unbounded();
    //  <xsd:element ref="n42:RadDetectorCharacteristics" minOccurs="0" maxOccurs="unbounded"/>
    builder.element("RadDetectorCharacteristics").call(RadDetectorState::addCharacteristics, Characteristics.class)
            .optional().unbounded();
    //  <xsd:element ref="n42:RadDetectorStateExtension" minOccurs="0" maxOccurs="unbounded"/>
    builder.reader(new ExtensionReader("RadDetectorStateExtension")).nop().optional().unbounded();
    return builder.getHandlers();
  }

}
