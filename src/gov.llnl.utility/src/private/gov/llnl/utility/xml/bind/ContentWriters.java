/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.WriterException;

/**
 *
 * @author nelson85
 */
@Internal
public class ContentWriters
{
  static public class PrimitiveWriter<Obj> extends ObjectWriter<Obj>
  {
    Marshaller marshaller;

    public PrimitiveWriter(Marshaller marshaller)
    {
      super(Options.NONE, "none", UtilityPackage.getInstance());
      this.marshaller = marshaller;
    }

    @Override
    public void attributes(WriterAttributes attributes, Obj object) throws WriterException
    {
    }

    @Override
    @SuppressWarnings("unchecked")
    public void contents(Obj object) throws WriterException
    {
      this.addContents(marshaller.marshall(object, getContext().getMarshallerOptions()));
    }

  }
}
