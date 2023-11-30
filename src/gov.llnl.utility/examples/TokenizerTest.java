
import gov.llnl.utility.Tokenizer;

/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
/**
 *
 * @author nelson85
 */
public class TokenizerTest
{

  static public void main(String[] args)
  {
    {
      String parseThis = "this\\ has  some\\\\    esc\\aped strings";
      Tokenizer tokenizer = Tokenizer.create("\\\\ ", "\\\\.", " +", "[^\\\\ ]+");
      Tokenizer.TokenMatcher matcher = tokenizer.matcher(parseThis);
      for (Tokenizer.Token token : matcher)
      {
        System.out.print("(" + token.group() + ")");
      }
      System.out.println();
    }
    {
      String parseThis = "this ${subst i,j,${} ${contents}}";
      Tokenizer tokenizer = Tokenizer.create(
              "\\$\\{(?:(?:[^\\$}]*)(?:\\\\[$}])*)*?\\}",
              "\\\\\\$", "[^$\\\\]+", "[$]");
      Tokenizer.TokenMatcher matcher = tokenizer.matcher(parseThis);
      for (Tokenizer.Token token : matcher)
      {
        System.out.print("(" + token.group() + ")");
      }
      System.out.println();

    }

    {
      String parseThis = "this} ${ ${subst i,j,\\} \\${ ${contents}}";
      Tokenizer expansionTokenizer = Tokenizer.create(
              "[$][{]", "[}]", "\\\\[$}]", "[^${}\\\\]+", "[$]", "[{]", "[\\\\]");
      Tokenizer.TokenMatcher matcher = expansionTokenizer.matcher(parseThis);
      int count = 0;
      for (Tokenizer.Token token : matcher)
      {
        if (token.id() == 0)
          count++;
        System.out.print(" (" + count + " " + token.group() + ")");
        if (token.id() == 1)
          count--;
      }
      System.out.println(" final=" + count);

    }
  }
}
