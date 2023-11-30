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
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import java.text.ParseException;
import org.xml.sax.Attributes;

/**
 * Reader for Matrix.
 *
 * This currently produces array type matrices and does not store sparse or
 * diagonal matrix in a compressed form. If a different memory layout is
 * required it should be cast when the load is complete.
 *
 * @author nelson85
 */
@Internal
@Reader.Declaration(pkg = MathPackage.class, name = "matrix",
        cls = Matrix.class,
        document = true, referenceable = true,
        contents = Reader.Contents.TEXT)
@Reader.Attribute(name = "major", type = String.class) // declare the order that elements appear in the file
@Reader.Attribute(name = "rows", type = Integer.class) // declare how many rows the data has
@Reader.Attribute(name = "columns", type = Integer.class) // declare how many columns the data has
public class MatrixReader extends ObjectReader<Matrix>
{
  private static class State
  {
    String major = "row";
    int rows = 1;
    int columns = 1;
    double[] data;
  }

  private static State getState(ReaderContext context)
  {
    return (State) context.getState();
  }

  @Override
  public Matrix start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    State state = new State();
    context.setState(state);
   
    if (attributes.getValue("rows") != null)
      state.rows = Integer.parseInt(attributes.getValue("rows"));
    if (attributes.getValue("columns") != null)
      state.columns = Integer.parseInt(attributes.getValue("columns"));
    if (attributes.getValue("major") != null)
      state.major = attributes.getValue("major");
    return null;
  }

  @Override
  public Matrix end(ReaderContext context) throws ReaderException
  {
    State state = getState(context);
    if (state.data.length != state.rows * state.columns)
      throw new ReaderException("size mismatch");
    if ("column".equals(state.major))
      return new MatrixColumnArray(state.data, state.rows, state.columns);
    if ("row".equals(state.major))
      return new MatrixColumnArray(state.data, state.rows, state.columns);
    throw new ReaderException("Unknown order");
  }

  @Override
  public Matrix contents(ReaderContext context, String textContents) throws ReaderException
  {
    try
    {
      State state = getState(context);
      state.data = ArrayEncoding.decodeDoubles(textContents);
      return null;
    }
    catch (ParseException ex)
    {
      throw new ReaderException("Error decoding double[]\n " + textContents, ex);
    }
  }

}
