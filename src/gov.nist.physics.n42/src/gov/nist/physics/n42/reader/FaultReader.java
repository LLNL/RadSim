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
import gov.nist.physics.n42.data.Fault;
import gov.nist.physics.n42.data.FaultSeverityCode;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "Fault",
        order = Reader.Order.SEQUENCE,
        cls = Fault.class,
        typeName = "FaultType")
@Reader.Attribute(name = "id", type = String.class, required = false)
public class FaultReader extends ObjectReader<Fault>
{
  @Override
  public Fault start(ReaderContext context, Attributes attr) throws ReaderException
  {
    Fault out = new Fault();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<Fault> builder = this.newBuilder();
        builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    //  <xsd:element ref="n42:FaultCodeValue" minOccurs="1" maxOccurs="1"/>
    builder.element("FaultCodeValue").callString(Fault::setCode).required();
    //  <xsd:element ref="n42:FaultDescription" minOccurs="1" maxOccurs="1"/>
    builder.element("FaultDescription").callString(Fault::setDescription).required();
    //  <xsd:element ref="n42:FaultSeverityCode" minOccurs="1" maxOccurs="1"/>
    builder.element("FaultSeverityCode").call(Fault::setSeverityCode, FaultSeverityCode.class).required();
    //  <xsd:element ref="n42:FaultExtension" minOccurs="0" maxOccurs="unbounded"/>
    builder.reader(new ExtensionReader("FaultExtension")).nop().optional().unbounded();
    return builder.getHandlers();
  }

}
