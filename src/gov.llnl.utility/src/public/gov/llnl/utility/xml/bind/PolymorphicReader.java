/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.PackageResource;
import gov.llnl.utility.io.ReaderException;
import org.xml.sax.Attributes;

/**
 * Reader for handling many different types that all derive from a common base
 * class.
 *
 * @param <T>
 */
public abstract class PolymorphicReader<T> extends ObjectReader<T>
{

  /**
   * Polymorphic readers require a modified Reader.Declaration as they are a
   * specific generated type.
   *
   * @return
   */
  @Override
  public Reader.Declaration getDeclaration()
  {
    Declaration decl = this.getClass().getAnnotation(Reader.Declaration.class);
    return new ReaderDeclarationImpl(decl)
    {
      @Override
      public Class<? extends PackageResource> pkg()
      {
        return decl.pkg();
      }

      @Override
      public String name()
      {
        return decl.name();
      }

      @Override
      public Order order()
      {
        return Order.CHOICE;
      }

      @Override
      public String schema()
      {
        return Reader.Declaration.NULL;
      }
    };
  }

//  @Override
//  abstract public Class<T> getObjectClass();
  /**
   * Get the list of readers supported by this object.
   *
   * @return
   * @throws ReaderException
   */
  abstract public ObjectReader<? extends T>[] getReaders()
          throws ReaderException;

  /**
   * Utility function for creating a group of readers with the same base type.
   *
   * @param <Obj>
   * @param readers
   * @return
   */
  @SuppressWarnings("unchecked")
  static protected <Obj> ObjectReader<? extends Obj>[]
          group(ObjectReader<? extends Obj>... readers)
  {
    return readers;
  }

  @SuppressWarnings("unchecked")
  static protected <Obj> ObjectReader<? extends Obj>[]
          of(Class<? extends Obj>... obj)
          throws ReaderException
  {
    ObjectReader[] out = new ObjectReader[obj.length];
    for (int i = 0; i < obj.length; i++)
    {
      out[i] = ObjectReader.create(obj[i]);
    }
    return out;
  }

//<editor-fold desc="internal" defaultstate="collapsed">
  @Override
  final public T start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    return null;
  }

  @Override
  final public T end(ReaderContext context) throws ReaderException
  {
    return (T) context.getState();
  }

  @Override
  @SuppressWarnings("unchecked")
  final public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    ReaderBuilder<T> rb = newBuilder();
    rb.readers((Class<T>)getObjectClass(), getReaders())
            .callContext((c,o,v)->c.setState(v));
    return rb.getHandlers();
  }
 
//</editor-fold>
}
