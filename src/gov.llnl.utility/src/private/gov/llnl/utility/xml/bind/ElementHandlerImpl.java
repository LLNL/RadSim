/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.Reader.ElementHandler;
import gov.llnl.utility.xml.bind.Reader.Option;
import java.util.EnumSet;
import java.util.function.BiConsumer;

/**
 * Element handlers provide the mapping to elements and call the method hooks.
 *
 * @author nelson85
 * @param <T>
 * @param <T2>
 */
@Internal
class ElementHandlerImpl<T, T2> implements ElementHandler
{
  final String key;
  final BiConsumer<T, T2> method;
  EnumSet<Option> options;   // options used are OPTIONAL, REQUIRED, UNBOUNDED, ANY_OTHER, ANY_ALL
  final Class<T2> resultCls;

  // Hints for validation
  ElementGroup parentGroup;
  ElementHandler nextHandler;

  /**
   * Create a new ElementHandler.
   *
   * @param key is the name of element.
   * @param options are options for this instance of element. (unbounded,
   * optional, etc)
   * @param resultCls is the type to be produced by this element.
   * @param method is the method that is called when this element is create.
   */
  public ElementHandlerImpl(String key, EnumSet<Option> options,
          Class<T2> resultCls,
          BiConsumer<T, T2> method)
  {
    this.key = key;
    this.method = method;
    this.options = options;
    this.resultCls = resultCls;
  }

  @Override
  public final void addOption(Option option)
  {
    if (option == null)
      return;
    if (options == null)
      this.options = EnumSet.of(option);
    else
      this.options.add(option);
    // These options are mutually exclusive so the exclusive option must be removed.
    if (option == Option.REQUIRED)
      this.options.remove(Option.OPTIONAL);
    else if (option == Option.OPTIONAL)
      this.options.remove(Option.REQUIRED);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onCall(ReaderContext context, Object parent, Object child) throws ReaderException
  {
    if (method == null)
      return;
    try
    {
      method.accept((T) parent, (T2) child);
    }
    catch (RuntimeException ex)
    {
      wrapException(ex, parent);
    }
  }

  /**
   * Returns the handlers to be used to interpret all elements that appear
   * within this element. If no elements may appear within this element then the
   * getHandlers returns null.
   *
   * @return the handlers or null if no child elements allowed.
   * @throws ReaderException
   */
  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    return null;
  }

  @Override
  public String getKey()
  {
    return key;
  }

  @Override
  public String getName()
  {
    String[] contents = key.split("#");
    return contents[0];
  }

  @Override
  public Reader getReader()
  {
    return null;
  }

  @Override
  public EnumSet<Option> getOptions()
  {
    return options;
  }

  @Override
  public Class getObjectClass()
  {
    return this.resultCls;
  }

  /**
   * @return the parentGroup
   */
  @Override
  public ElementGroup getParentGroup()
  {
    return parentGroup;
  }

  /**
   * @return the nextHandler
   */
  @Override
  public ElementHandler getNextHandler()
  {
    return nextHandler;
  }

  @Override
  public void setParentGroup(ElementGroup parentGroup)
  {
    this.parentGroup = parentGroup;
  }

  @Override
  public void setNextHandler(ElementHandler handler)
  {
    this.nextHandler = handler;
  }

  /**
   * Convert a RuntimeException from a user method to ReaderException.
   *
   * @param ex is the incoming RuntimeException.
   * @param parent is the object which was to be called.
   * @throws ReaderException always.
   */
  public static void wrapException(RuntimeException ex, Object parent) throws ReaderException
  {
    ReaderException rex;
    if (ex.getCause() != null)
    {
      // If the cause is already a reader exception, then we just need to unwrap it.
      if (ex.getCause() instanceof ReaderException)
        rex = (ReaderException) ex.getCause();
      else
        // Otherwise wrap the exception.
        rex = new ReaderException(ex.getCause());
    }
    else if (parent == null)
      rex = new ReaderException("Parent object was not constructed by start.", ex);
    else
      rex = new ReaderException(ex);
    throw rex;
  }

  @Override
  public boolean hasOption(Option option)
  {
    if (options == null)
      return false;
    return options.contains(option);
  }

}
