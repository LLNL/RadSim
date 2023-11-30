/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.rtk.RtkPackage;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Internal

@Reader.Declaration(pkg = RtkPackage.class, name = "sourceModel", cls = SphericalModel.class,
        order = Reader.Order.SEQUENCE, referenceable = true)
@Reader.Attribute(name = "title", type = String.class)
public class SourceModelReader extends ObjectReader<SphericalModel>
{

  @Override
  public SphericalModel start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    return new SphericalModel();
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    ReaderBuilder<SphericalModel> builder = this.newBuilder();
    builder.element("geometry")
            .reader(new GeometryReader())
            .call(SphericalModel::setGeometry);
    builder.element("layer")
            .reader(new LayerReader())
            .call((p,v)->p.addLayer(v))
            .unbounded();
    return builder.getHandlers();
  }

}
