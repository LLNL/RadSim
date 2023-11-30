/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for creating altered copies strings.
 *
 * These act on CharSequences so that they will work on String, StringBuffer,
 * and StringBuilder. The utilities include two types of operations. In place
 * operations work on StringBuilder and alter as they go and have the suffix
 * "Assign". Copy operators create a new copy of the sequence as they operate.
 *
 * @author nelson85
 */
public class CharSequenceUtilities
{
//<editor-fold desc="substitute" defaultstate="collapsed">
  /**
   * Replace specified occurrences of a regular expression with a string.
   *
   * @param str is the string to preform substitution on.
   * @param from is a regular expression to be replaced.
   * @param to is the string to replace with, or null if it is to be removed.
   * @param occurrenceStart is the start of the occurrence range to substitute.
   * @param occurrenceEnd is the end of the occurrence range to substitute, or
   * -1 if the range is unlimited.
   * @return a new string builder containing the replaced string.
   */
  static public StringBuilder substitute(CharSequence str,
          String from, String to,
          int occurrenceStart, int occurrenceEnd)
  {
    Pattern p = Pattern.compile(from);
    Matcher m = p.matcher(str);
    StringBuilder out = new StringBuilder();
    int pos = 0;
    int occurrence = 0;
    while (m.find())
    {
      if (occurrence >= occurrenceStart)
      {
        out.append(str.subSequence(pos, m.start()));
        if (to != null)
          out.append(to);
      }
      else
      {
        out.append(str.subSequence(pos, m.end()));
      }
      pos = m.end();
      occurrence++;
      if (occurrenceEnd != -1 && occurrence >= occurrenceEnd)
        break;
    }
    out.append(str.subSequence(pos, str.length()));
    return out;
  }

  /**
   * Replace specified occurrences of a regular expression with a function based
   * on the string.
   *
   * @param str is the string to preform substitution on.
   * @param from is a regular expression to be replaced.
   * @param to is the function producing the string to replace with.
   * @param occurrenceStart is the start of the occurrence range to substitute.
   * @param occurrenceEnd is the end of the occurrence range to substitute, or
   * -1 if the range is unlimited.
   * @return a new StringBuilder containing the replaced string.
   */
  static public StringBuilder substituteFunction(CharSequence str,
          String from,
          Function<String, String> to,
          int occurrenceStart,
          int occurrenceEnd)
  {
    Pattern p = Pattern.compile(from);
    Matcher m = p.matcher(str);
    StringBuilder out = new StringBuilder();
    int pos = 0;
    int occurrence = 0;
    while (m.find())
    {
      if (occurrence >= occurrenceStart)
      {
        out.append(str.subSequence(pos, m.start()));
        out.append(to.apply(m.group()));
      }
      else
      {
        out.append(str.subSequence(pos, m.end()));
      }
      pos = m.end();
      occurrence++;
      if (occurrenceEnd != -1 && occurrence >= occurrenceEnd)
        break;
    }
    out.append(str.subSequence(pos, str.length()));
    return out;
  }

  /**
   * Replace all occurrences of a regular expression with a string.
   *
   * This method operates in place thus does not copy the string.
   *
   * @param str is the StringBuilder to preform substitution on.
   * @param from is a regular expression to find.
   * @param to is the string to replace with, or null to remove.
   * @param occurrenceStart is the start of the occurrence range to substitute.
   * @param occurrenceEnd is the end of the occurrence range to substitute, or
   * -1 if the range is unlimited.
   * @return the modified StringBuilder.
   */
  static public StringBuilder substituteAssign(StringBuilder str,
          String from, String to,
          int occurrenceStart, int occurrenceEnd)
  {
    Pattern p = Pattern.compile(from);
    Matcher m = p.matcher(str);
    int pos = 0;
    int occurrence = 0;
    while (m.find(pos))
    {
      if (occurrence >= occurrenceStart)
      {
        if (to == null)
        {
          str.delete(m.start(), m.end());
          pos = m.end() - from.length();
        }
        else
        {
          str.replace(m.start(), m.end(), to);
          pos = m.end() - from.length() + to.length();
        }
      }
      else
      {
        pos = m.end();
      }
      occurrence++;
      if (occurrenceEnd != -1 && occurrence >= occurrenceEnd)
        break;
    }
    return str;
  }

  /**
   * Replace all occurrences of a regular expression with a string.
   *
   * This method operates in place thus does not copy the string.
   *
   * @param str is the StringBuilder to preform substitution on.
   * @param from is a regular expression to find.
   * @param to is the function producing the string to replace with.
   * @param occurrenceStart is the start of the occurrence range to substitute.
   * @param occurrenceEnd is the end of the occurrence range to substitute, or
   * -1 if the range is unlimited.
   * @return the modified StringBuilder.
   */
  static public StringBuilder substituteFunctionAssign(StringBuilder str,
          String from, Function<String, String> to,
          int occurrenceStart, int occurrenceEnd)
  {
    Pattern p = Pattern.compile(from);
    Matcher m = p.matcher(str);
    int pos = 0;
    int occurrence = 0;
    while (m.find(pos))
    {
      if (occurrence >= occurrenceStart)
      {
        String result = to.apply(m.group());
        str.replace(m.start(), m.end(), result);
        pos = m.end() - from.length() + result.length();
      }
      else
      {
        pos = m.end();
      }
      occurrence++;
      if (occurrenceEnd != -1 && occurrence >= occurrenceEnd)
        break;
    }
    return str;
  }
//</editor-fold>
//<editor-fold desc="translate" defaultstate="collapsed">

  /**
   * Translate a set of characters in a string to a different set.
   *
   * @param str is the string to perform substitution on.
   * @param from is a list of characters to replace.
   * @param to is a list of characters for replacement, or null they are to be
   * removed.
   * @return a new StringBuilder containing the replaced string.
   */
  static public StringBuilder translateAll(CharSequence str, CharSequence from, CharSequence to)
  {
    if (to != null)
      if (from.length() != to.length())
        throw new RuntimeException("To and from character list lengths do not match.");

    Pattern p = Pattern.compile(getMatchChars(from));
    Matcher m = p.matcher(str);
    StringBuilder out = new StringBuilder();
    int pos = 0;
    while (m.find(pos))
    {
      out.append(str.subSequence(pos, m.start()));
      if (to != null)
      {
        out.append(findReplacement(str.charAt(m.start()), from, to));
      }
      pos = m.end();
    }
    out.append(str.subSequence(pos, str.length()));
    return out;
  }

  /**
   * Translate a set of characters in a string to a different set.
   *
   * The input StringBuilder is modified during the operation.
   *
   * @param str is a StringBuilder to perform substitution on.
   * @param from is a list of characters to replace.
   * @param to is a list of characters for replacement, or null they are to be
   * removed.
   * @return the replaced StringBuilder.
   */
  static public StringBuilder translateAllAssign(StringBuilder str, CharSequence from, CharSequence to)
  {
    if (to != null)
      if (from.length() != to.length())
        throw new RuntimeException("To and from character list do not match.");

    Pattern p = Pattern.compile(getMatchChars(from));
    Matcher m = p.matcher(str);
    int pos = 0;
    while (m.find(pos))
    {
      if (to != null)
      {
        str.setCharAt(m.start(), findReplacement(str.charAt(m.start()), from, to));
        pos = m.end();
      }
      else
      {
        str.deleteCharAt(m.start());
        pos = m.start();
      }
    }
    return str;
  }
//</editor-fold>
//<editor-fold desc="internal" defaultstate="collapsed">

  static private char findReplacement(char c, CharSequence from, CharSequence to)
  {
    for (int i = 0; i < from.length(); ++i)
    {
      if (c == from.charAt(i))
      {
        return to.charAt(i);
      }
    }
    throw new RuntimeException("No match");
  }

  static private String getMatchChars(CharSequence from)
  {
    StringBuilder patternList = new StringBuilder();
    patternList.append("[");
    for (int i = 0; i < from.length(); ++i)
    {
      char c = from.charAt(i);
      if (c == '-')
        patternList.append("\\-");
      if (c == '^')
        patternList.append("\\^");
      if (c == '[')
        patternList.append("\\[");
      if (c == ']')
        patternList.append("\\]");
      else
        patternList.append(c);
    }
    patternList.append("]");
    return patternList.toString();
  }
//</editor-fold>

//  static public void main(String[] args)
//  {
//    System.out.println(substitute(new StringBuilder("Cats and dog are dogmatic."), "dog", null,0,1));
//    System.out.println(substitute(new StringBuilder("Cats and dog are dogmatic."), "dog", null,1,2));
//    System.out.println(substitute(new StringBuilder("Cats and dog are dogmatic."), "dog", "dogdog", 0,-1));
//    System.out.println(substitute(new StringBuilder("Cats and dog are dogmatic."), "dog", null,0,-1));
//    System.out.println(CharSequenceUtilities.substituteFunction(new StringBuilder("Cats and dog are dogmatic."), "dog", String::toUpperCase, 0, -1));
//    System.out.println(substituteAssign(new StringBuilder("Cats and dog are dogmatic."), "dog", "dogdog", 0, -1));
//    System.out.println(substituteAssign(new StringBuilder("Cats and dog are dogmatic."), "dog", null, 0, -1));
//    System.out.println(translateAll(new StringBuilder("Caats and dog are dogmatic."), "abcd", "ABCD"));
//    System.out.println(translateAll(new StringBuilder("Caats and dog are dogmatic."), "abcd", null));
//    System.out.println(translateAllAssign(new StringBuilder("Caats and dog are dogmatic."), "abcd", "ABCD"));
//    System.out.println(translateAllAssign(new StringBuilder("Caats and dog are dogmatic."), "abcd", null));
//    System.out.println(translateAllAssign(new StringBuilder("Foo.Bar"), ".", null));
//    System.out.println(translateAllAssign(new StringBuilder("Foo.Bar"), ".", "_"));
//  }
}
