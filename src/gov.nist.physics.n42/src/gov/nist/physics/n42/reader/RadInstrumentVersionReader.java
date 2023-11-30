/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.RadInstrumentVersion;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.ComplexObject;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "RadInstrumentVersion",
        order = Reader.Order.SEQUENCE,
        cls = RadInstrumentVersion.class,
        typeName = "RadInstrumentVersionType")
public class RadInstrumentVersionReader extends ObjectReader<RadInstrumentVersion>
{
  private static class State
  {
    String name;
    String version;
  }

  private State getState(ReaderContext context)
  {
    return (State) context.getState();

  }

  @Override
  public RadInstrumentVersion start(ReaderContext context, Attributes attr)
  {
    context.setState(new State());
    return null;
  }

  @Override
  public RadInstrumentVersion end(ReaderContext context)
  {
    State state = getState(context);
    return new RadInstrumentVersion(state.name, state.version);
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<RadInstrumentVersion> builder = this.newBuilder();
    builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
    builder.element("RadInstrumentComponentName").contents(String.class).callContext((c, o, v) -> getState(c).name = v);
    builder.element("RadInstrumentComponentVersion").contents(String.class).callContext((c, o, v) -> getState(c).version = v);
    return builder.getHandlers();
  }

}
