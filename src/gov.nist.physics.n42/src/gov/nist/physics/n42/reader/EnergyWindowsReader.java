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
import gov.nist.physics.n42.data.EnergyWindows;
import org.xml.sax.Attributes;

/**
 *
 * @author monterial1
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "EnergyWindows",
        order = Reader.Order.SEQUENCE,
        cls = EnergyWindows.class,
        typeName = "EnergyWindowsType")
@Reader.Attribute(name = "id", type = String.class, required = true)
public class EnergyWindowsReader extends ObjectReader<EnergyWindows>
{

  @Override
  public EnergyWindows start(ReaderContext context, Attributes attr) throws ReaderException
  {
    EnergyWindows out = new EnergyWindows();
    ReaderUtilities.register(context, out, attr);
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<EnergyWindows> builder = this.newBuilder();
    builder.element("WindowStartEnergyValues")
            .call(EnergyWindows::setStartEnergyValues, double[].class)
            .required();
    builder.element("WindowEndEnergyValues")
            .call(EnergyWindows::setEndEnergyValues, double[].class)
            .required();

    // Footer
    //<xsd:element ref="n42:RadInstrumentDataExtension" minOccurs="0" maxOccurs="unbounded"/>
    return builder.getHandlers();
  }

}
