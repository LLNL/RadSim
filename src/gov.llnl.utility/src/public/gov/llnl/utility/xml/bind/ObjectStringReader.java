/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.io.ReaderException;
import org.xml.sax.Attributes;

/**
 * Convenience class to serve as the base class for anything than needs text
 * content.
 *
 * FIXME: this class should be removed in favor of the annotation processor
 * which can verify that all of the required classes are created properly.
 *
 * <p>
 * Example:
 * <pre>
 * {@code
 *   @Reader.Declaration(cls=Foo.class)
 *   public class FooReader extends ObjectStringReader<Foo>
 *   {
 *
 *     public Object start(ReaderContext context, Attributes attributes) throws ReaderException
 *     {
 *       return new Foo();
 *     }
 * 
 *     public Foo contents(ReaderContext context, Foo object, String textContents) throws ReaderException
 *     {
 *        throw new UnsupportedOperationException("Not supported yet.");
 *     }
 * 
 *   }
 * }
 * </pre>
 *
 * @param <Component> is the class for objects produced by this Reader.
 */
public abstract class ObjectStringReader<Component>
        extends ObjectReader<Component>
{

  /**
   * {@inheritDoc}
   * <p>
   * start() is optional for classed derived from ObjectStringReader.
   * <p>
   */
  @Override
  public Component start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    return null;
  }

  /**
   * {@inheritDoc}
   * <p>
   * All classes derived from ObjectStringReader must implement contents().
   */
  @Override
  abstract public Component contents(ReaderContext context, String textContents)
          throws ReaderException;

  /**
   * {@inheritDoc}
   * <p>
   * {@code end} is optional for ObjectStringReader.
   */
  @Override
  public Component end(ReaderContext context) throws ReaderException
  {
    return null;
  }

  /**
   * {@inheritDoc}
   * <p>
   * ObjectStringReader is for objects that do not contain child elements. All
   * classes derived from ObjectString may not implement getHandlers().
   */
  @Override
  final public ElementHandlerMap getHandlers(ReaderContext context)
  {
    return null;
  }

//<editor-fold desc="schema">
  @Override
  public void createSchemaType(SchemaBuilder builder) throws ReaderException
  {
    SchemaBuilderUtilities.createSchemaTypeDefaultString(this, builder);
  }

//</editor-fold>
}
