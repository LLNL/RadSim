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
import gov.llnl.utility.xml.bind.Reader.ElementHandlerMap;
import gov.llnl.utility.xml.bind.Reader.Option;
import gov.llnl.utility.xml.bind.Reader.SectionInterface;
import java.util.EnumSet;
import org.xml.sax.Attributes;

@Internal
@SuppressWarnings("unchecked")
class SectionHandler extends ElementHandlerImpl
{
  final SectionInterface section;

  SectionHandler(EnumSet<Option> flags, SectionInterface section)
  {
    super(section.getHandlerKey(), flags, null, null);
    this.section = section;
    Reader.Declaration decl = section.getDeclaration();
    if (decl.contents() == Reader.Contents.TEXT)
      this.addOption(Option.CAPTURE_TEXT);
  }

  @Override
  public Object onStart(ReaderContext context, Attributes attr) throws ReaderException
  {
    // Copy the parentGroup to child
    ElementContextImpl pc = (ElementContextImpl) context.getParentContext();
    ElementContextImpl hc = (ElementContextImpl) context.getCurrentContext();
    hc.targetObject = hc.parentObject;
    hc.state = pc.state;

    // Call the start, but the object created will be ignored.
    section.start(context, attr);
    return null;
  }

  @Override
  public Object onEnd(ReaderContext context) throws ReaderException
  {
    section.end(context);
    return null;
  }

  @Override
  public Object onTextContent(ReaderContext context, String textContent) throws ReaderException
  {
    section.contents(context, textContent);
    return null;
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    return section.getHandlers(context);
  }

  @Override
  public Reader getReader()
  {
    return section;
  }

  @Override
  public void createSchemaElement(SchemaBuilder builder, DomBuilder group) throws ReaderException
  {
    group = section.createSchemaElement(builder, getName(), group, false);
    SchemaBuilderUtilities.applyOptions(group, options);
  }

}
