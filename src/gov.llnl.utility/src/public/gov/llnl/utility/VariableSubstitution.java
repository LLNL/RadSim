/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

/**
 *
 * @author nelson85
 */
public class VariableSubstitution
{
  Tokenizer tokenizer = Tokenizer.create("[$]\\{(\\S+)\\}", "\\$", "[^$]+");
  Map<String, String> substitutions;
  Writer output = null;

  public VariableSubstitution(Map<String, String> substitutions)
  {
    this.substitutions = substitutions;
  }

  public VariableSubstitution()
  {
    this.substitutions = new ArrayMap<>();
  }

  public void setOutput(Writer writer)
  {
    output = writer;
  }

  public void setOutput(OutputStream writer)
  {
    output = new BufferedWriter(new OutputStreamWriter(writer));
  }

  public void execute(String input) throws IOException
  {
    for (Tokenizer.Token token : tokenizer.matcher(input))
    {
      if (token.id() == 0)
      {
        String sub = substitutions.get(token.group(1));
        if (sub != null)
          output.write(sub);
      }
      else
      {
        output.write(token.group());
      }
    }
    output.flush();
  }

  public void execute(InputStream input) throws IOException
  {
    for (Tokenizer.Token token : tokenizer.matcher(input))
    {
      if (token.id() == 0)
      {
        String sub = substitutions.get(token.group(1));
        if (sub != null)
          output.write(sub);
      }
      else
      {
        output.write(token.group());
      }
    }
    output.flush();
  }

  public VariableSubstitution put(String key, String value)
  {
    this.substitutions.put(key, value);
    return this;
  }

  public VariableSubstitution put(String key, Object value)
  {
    this.substitutions.put(key, value.toString());
    return this;
  }

  public VariableSubstitution put(String key, int value)
  {
    this.substitutions.put(key, Integer.toString(value));
    return this;
  }

  public String substitute(String input)
  {
    try
    {
      ByteArrayOutputStream os = new ByteArrayOutputStream(input.length() + 100);
      setOutput(os);
      execute(input);
      return new String(os.toByteArray());
    }
    catch (IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  public static String substituteString(String input, Map<String, String> substitutions)
  {
    try
    {
      VariableSubstitution vs = new VariableSubstitution(substitutions);
      ByteArrayOutputStream os = new ByteArrayOutputStream(input.length() + 100);
      vs.setOutput(os);
      vs.execute(input);
      return new String(os.toByteArray());
    }
    catch (IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

}
