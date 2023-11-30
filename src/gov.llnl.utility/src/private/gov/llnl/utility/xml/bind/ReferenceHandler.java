/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomBuilder;
import gov.llnl.utility.xml.bind.Reader.Option;
import gov.llnl.utility.xml.bind.SchemaBuilder;
import java.util.EnumSet;
import java.util.function.BiConsumer;

/**
 *
 * @author nelson85
 * @param <T>
 * @param <T2>
 */
@Internal
@SuppressWarnings("unchecked")
public class ReferenceHandler<T, T2> extends ElementHandlerImpl
{
  ReferenceHandler(String key, EnumSet<Option> options, Class<T2> cls, BiConsumer<T, T2> method)
  {
    super(key, options, cls, method);
    addOption(Option.MUST_REFERENCE);
  }

  @Override
  public void createSchemaElement(SchemaBuilder builder, DomBuilder type) throws ReaderException
  {
    DomBuilder group = type.element("xs:element")
            .attr("name", getName())
            .attr("type", "util:reference-type");
    SchemaBuilderUtilities.applyOptions(group, options);
  }

}
