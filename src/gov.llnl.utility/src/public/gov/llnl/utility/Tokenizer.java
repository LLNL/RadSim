/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.io.InputStream;
import java.util.Iterator;

/**
 *
 * @author nelson85
 */
public abstract class Tokenizer
{

  public static Tokenizer create(String... patterns)
  {
    return new TokenizerImpl(patterns);
  }

  /**
   * Creates a matcher for this stream of tokens.
   *
   * @param string
   * @return a new matcher.
   */
  public abstract TokenMatcher matcher(String string);

  /**
   * Creates a matcher for a stream of tokens.
   *
   * @param stream
   * @return a new matcher.
   */
  public abstract TokenMatcher matcher(InputStream stream);

  /**
   * Exception thrown when the Token extraction failed.
   */
  public static class TokenException extends RuntimeException
  {
    public TokenException(String msg)
    {
      super(msg);
    }

    public TokenException(String msg, Throwable ex)
    {
      super(msg, ex);
    }
  }

  /**
   *
   * @author nelson85
   */
  public static interface TokenMatcher extends Iterable<Tokenizer.Token>
  {
    /**
     * Get an iterator for extracting tokens.
     *
     * @return an iterator for all extracted tokens.
     */
    @Override
    Iterator<Tokenizer.Token> iterator();

    /**
     * Get the next token. The contents of the previous extracted token is
     * destroyed, so capture all required information before the extracting the
     * next token.
     *
     * @return null if no tokens are available.
     * @throws gov.llnl.utility.Tokenizer.TokenException if the string contains
     * contents that can't be parsed into tokens.
     */
    Tokenizer.Token next() throws TokenException;
  }

  /**
   *
   * @author nelson85
   */
  public static interface Token
  {
    /**
     * Returns the end of the token match.
     *
     * @return then index of the character past the end of the match.
     * @throws IllegalStateException if operation failed.
     */
    int end() throws IllegalStateException;

    /**
     * Returns the end of the token match by a group.
     *
     * @param i is the group number
     * @return then index of the character past the end of the group.
     * @throws IllegalStateException if operation failed.
     */
    int end(int i) throws IllegalStateException;

    /**
     * Get the token that was matched,
     *
     * @return the string that matched this token.
     * @throws IllegalStateException if operation failed.
     */
    String group() throws IllegalStateException;

    /**
     * Get the substring that matches the group.
     *
     * @param i
     * @return the string matched by the group.
     * @throws IllegalStateException if operation failed.
     */
    String group(int i) throws IllegalStateException;

    /**
     * Returns the number of groups that were matched with this token.
     *
     * @return the total groups matched.
     * @throws IllegalStateException if operation failed.
     */
    int groupCount() throws IllegalStateException;

    /**
     * Gets the id associate with this token.
     *
     * @return the id base on the order of token declarations.
     */
    int id();

    /**
     * Returns the start of the token match.
     *
     * @return the index of the first character matched.
     * @throws IllegalStateException if operation failed.
     */
    int start() throws IllegalStateException;

    /**
     * Returns the offset of the first character matched by a group.
     *
     * @param i is the group number.
     * @return the offset of the first character matched by this group.
     * @throws IllegalStateException if operation failed.
     */
    int start(int i) throws IllegalStateException;
  }
  
}
