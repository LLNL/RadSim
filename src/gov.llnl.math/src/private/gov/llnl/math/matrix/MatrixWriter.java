/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.MathPackage;
import gov.llnl.utility.ArrayEncoding;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.ObjectWriter;

/**
 * Writer for storing a Matrix in xml document.
 *
 * @author nelson85
 */
@Internal
public class MatrixWriter extends ObjectWriter<Matrix>
{
  public MatrixWriter()
  {
    super(Options.NONE, "matrix", MathPackage.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, Matrix object) throws WriterException
  {
    attributes.add("major", "column");
    attributes.add("rows", object.rows());
    attributes.add("columns", object.columns());
  }

  @Override
  public void contents(Matrix object) throws WriterException
  {
    // Gzip on encoding will handle sparse matrix representations
    // so no special handling is required.
    this.addContents(ArrayEncoding.encodeDoubles(object.flatten()));
  }
}
