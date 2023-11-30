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

/**
 *
 * @author nelson85
 */
@Internal
@SuppressWarnings("unchecked")
class DeferredAction
{
  ReaderContextImpl readerContext;
  ElementContextImpl elementContext;
  Object parent;
  ElementHandlerImpl base;
  boolean copy;

  public DeferredAction(ReaderContextImpl context, ElementHandler base,
          Object parent, boolean copy)
  {
    this.base = (ElementHandlerImpl) base;
    this.readerContext = context;
    this.elementContext = context.currentContext;
    this.parent = parent;
    this.copy = copy;
  }

  void executeDeferred(Object child) throws ReaderException
  {  
    // Previous readerContext is required for sections to work properly
    
    // The child for this context does not exist because we were called 
    // out of turn.
    this.elementContext.childContext = null;
    this.elementContext.targetObject = parent;
    
    // Push state
    ElementContextImpl restore = readerContext.currentContext;
    readerContext.currentContext = this.elementContext;
    
    // Execute all
    base.onCall(readerContext, parent, child);
    
    // Restore state
    readerContext.currentContext = restore;
  }

}
