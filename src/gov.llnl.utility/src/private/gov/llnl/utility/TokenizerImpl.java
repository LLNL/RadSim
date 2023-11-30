/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import gov.llnl.utility.annotation.Debug;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Simple regular expression tokenizer. Instances of TokenizerImpl are thread
 * safe, but the matcher is not.
 *
 * @author nelson85
 */
class TokenizerImpl extends Tokenizer
{

  TokenDef[] tokenDef;

  /**
   * Create a new Tokenizer with specified patterns.
   *
   * @param patterns
   */
  public TokenizerImpl(String... patterns)
  {
    compile(patterns);
  }

  final void compile(String[] patterns)
  {
    this.tokenDef = Stream.of(patterns)
            .map(p -> new TokenDef("^" + p))
            .toArray(TokenDef[]::new);
  }

  /**
   * Creates a matcher for a string of tokens.
   *
   * @param string
   * @return a new matcher.
   */
  @Override
  public TokenMatcher matcher(String string)
  {
    return new StringTokenMatcher(string, tokenDef);
  }

  /**
   * Creates a matcher for a stream of tokens.
   *
   * @param stream
   * @return a new matcher.
   */
  @Override
  public TokenMatcher matcher(InputStream stream)
  {
    return new StreamTokenMatcher(stream, tokenDef);
  }

  /**
   * Display the list of token patterns that have been defined.
   *
   * @param out
   */
  @Debug
  public void dump(PrintStream out)
  {
    out.println("Tokenizer:");
    int i = 0;
    for (TokenDef token : this.tokenDef)
    {
      out.println("  " + i + " " + token.pattern);
      i++;
    }
  }

  @Override
  public boolean equals(Object o)
  {
    if (!(o instanceof TokenizerImpl))
      return false;
    TokenizerImpl o2 = (TokenizerImpl) o;
    return Arrays.equals(o2.tokenDef, this.tokenDef);
  }
}
