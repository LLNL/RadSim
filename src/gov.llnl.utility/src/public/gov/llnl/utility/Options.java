/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * Simple implementation of an options parse.
 *
 * @author nelson85
 */
public class Options
{
  TreeMap<String, OptionContent> options_ = new TreeMap<>();
  String arguments_[] = null;
  String error_ = null;

  public Options()
  {
  }

  public class OptionContent
  {
    String name;
    boolean hasArgument;
    boolean specified;
    String value;
  };

  public String getError()
  {
    return error_;
  }

  public int getNumArguments()
  {
    if (arguments_ == null)
      return 0;
    return arguments_.length;
  }

  public String getArgument(int num)
  {
    return arguments_[num];
  }

  public List<String> getArguments()
  {
    if (arguments_==null)
      return Arrays.asList();
    return Arrays.asList(arguments_);
  }

  public void setOptionString(String getopt_string)
  {
    for (int index = 0; index < getopt_string.length(); ++index)
    {
      boolean has_arg;
      has_arg = (index + 1 < getopt_string.length()) && ":".equals(getopt_string.substring(index, index + 1));
      addOption(getopt_string.substring(index, index + 1), has_arg, "");
      if (has_arg)
      {
        index++;
      }
    }
  }

  /**
   * Add option that does not take an argument.
   *
   * @param key option name
   */
  public void addOption(String key)
  {
    addOption(key, false, "");
  }

  /**
   * Add option with argument.
   *
   * @param key
   * @param has_argument
   */
  public void addOption(String key,
          boolean has_argument)
  {
    addOption(key, has_argument, "");
  }

  /**
   * add option with default_value
   *
   * @param key
   * @param has_argument
   * @param default_value
   */
  public void addOption(String key,
          boolean has_argument,
          String default_value)
  {
    OptionContent oc = new OptionContent();
    oc.name = key;
    oc.hasArgument = has_argument;
    oc.specified = false;
    oc.value = default_value;
    options_.put(key, oc);
  }

  public boolean parse(String argv[])
  {
    int argc = argv.length;
    int index;
    String opt;
    String value = null;

    OUTER:
    for (index = 0; index < argc; ++index)
    {
      boolean hasValue = false;
      boolean is_short = false;
      String arg = argv[index];
      if (!arg.startsWith("-"))
      {
        break;
      }
      else
      {
        if (arg.length() == 2)
        {
          // handle -- 
          if (arg.equals("--"))
            break;
          // handle -[A-z]
          is_short = true;
          opt = arg.substring(1);
        }
        else if (arg.length() == 1)
        {
          // has only "-"
          break;
        }
        else
        {
          opt = arg.substring(2);
        }

        //int eq;
        //eq = opt.find('=');
        int eq = opt.indexOf("=");

        if (eq > 0)
        {
          // opt= "FOO=BAR" => opt="FOO" , value="BAR"
          value = opt.substring(eq + 1);
          opt = opt.substring(0, eq);
          hasValue = true;
        }
        if (is_short && opt.length() > 1)
        {
          String s1 = "Long option specified with -. (";
          String s2 = ")";
          error_ = (s1.concat(opt)).concat(s2);
          // error_=std::string("Long option specified with -. (")+opt+std::string(")");
          return false;
        }

        OptionContent entry = options_.get(opt);

        // User specified a value that does not exist for this program
        if (entry == null)
        {
          String s1 = "Invalid Option. (";
          String s2 = ")";
          error_ = (s1.concat(opt)).concat(s2);

          // error_=std::string("Invalid option. (")+opt+std::string(")");
          return false;
        }

        // Mark that this option was given by the user
        entry.specified = true;

        // If the option required a value we must look for either
        //  --option=value or  --option value forms
        if (entry.hasArgument)
        {
          if (hasValue)
          {
            entry.value = value;
          }
          else if (index < argc - 1)
          {
            index++;
            // iter -> second.value = argv[index];
            entry.value = argv[index];
          }
          else
          {
            // There was no = and we have run out of arguments
            String s1 = "Option requires argument. (";
            String s2 = ")";
            error_ = (s1.concat(opt)).concat(s2);
            //error_=std::string("Option requires argument (")+opt+std::string(")");
            return false;
          }
        }
      }
    }

    if (index < argc)
    {
      arguments_ = new String[argc - index];
      // arguments_.resize(argc - index); // no need to rezise with java
      for (int i = 0; i < argc - index; ++i)
      {
        arguments_[i] = String.valueOf(argv[index + i]);
      }
    }
    return true;
  }

  public boolean isOptionSpecified(String key)
  {
    OptionContent entry = options_.get(key);
    if (entry == null)
      return false;
    return entry.specified;
  }

  public String getOptionValue(String key)
  {
    OptionContent entry = options_.get(key);
    if (entry == null)
      return "";
    return entry.value;
  }
}
