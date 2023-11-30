/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

import gov.llnl.rtk.physics.Nuclide;
import gov.llnl.rtk.physics.Nuclides;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nelson85
 */
public class EnsdfParser
{

  public static List<EnsdfDataSet> parseFile(Path path) throws IOException
  {
    ArrayList<String> lines = new ArrayList<>();
    try ( BufferedReader br = Files.newBufferedReader(path))
    {
      br.lines().forEach(lines::add);
    }
    return parseLines(lines);
  }

  public static List<EnsdfDataSet> parseStream(InputStream is) throws IOException
  {
    ArrayList<String> lines = new ArrayList<>();
    try ( InputStreamReader isr = new InputStreamReader(is))
    {
      BufferedReader br = new BufferedReader(isr);
      br.lines().forEach(lines::add);
    }
    return parseLines(lines);
  }

  public static List<EnsdfDataSet> parseLines(Collection<String> linesIn)
  {
    LineSource lines = new IterableLineSource(linesIn);
    return parseLineSource(lines);
  }

  /**
   * Back end for reading an ENSDF file.
   *
   * This operates line by line as ENSDF is a card stack format.
   *
   * @param lines
   * @return
   */
  public static List<EnsdfDataSet> parseLineSource(LineSource lines)
  {
    List<EnsdfDataSet> sets = new ArrayList<>();
    while (!lines.isEmpty())
    {
      EnsdfDataSet dataSet = new EnsdfDataSet();
      EnsdfIdentification identification = parseEnsdfIdentifier(dataSet, lines);
      parseEnsdfGeneral(dataSet, lines);
      parseEnsdfComments(dataSet, lines, identification);
      parseEnsdfQPN(dataSet, lines);
      parseEnsdfUnassigned(dataSet, lines);
      parseEnsdfLevels(dataSet, lines);
      parseEnsdfEnd(dataSet, lines);
      sets.add(dataSet);
    }
    return sets;
  }

  /**
   * Assert if a character at a position is a blank.
   *
   * Throws if the assertion fails.
   *
   * @param line
   * @param c
   */
  static void assertBlank(String line, char c)
  {
    if (c != ' ')
      throw new RuntimeException("Expected blank: " + c + ":" + line);
  }

  /**
   * Assert if a region is a blank.
   *
   * Throws if the assertion fails.
   *
   * @param line
   * @param c
   */
  static void assertBlank(String line, String part)
  {
    if (!part.isBlank())
      throw new RuntimeException("Expected blank: " + part + ":" + line);
  }

  static EnsdfQValue parseEnsdfQ(EnsdfDataSet dataSet, String line)
  {
    assertBlank(line, line.charAt(6));
    assertBlank(line, line.charAt(8));
    EnsdfQuantity QM = new EnsdfQuantity(line.substring(9, 19), line.substring(19, 21));
    EnsdfQuantity SN = new EnsdfQuantity(line.substring(21, 29), line.substring(29, 31));
    EnsdfQuantity SP = new EnsdfQuantity(line.substring(31, 39), line.substring(39, 41));
    EnsdfQuantity QA = new EnsdfQuantity(line.substring(39, 49), line.substring(49, 55));
    String QREF = line.substring(55, 80).strip();
    dataSet.QValue = new EnsdfQValue(dataSet, QM, SN, SP, QA, QREF);
    return dataSet.QValue;
  }

  static EnsdfParent parseEnsdfParent(EnsdfDataSet dataSet, String line)
  {
    assertBlank(line, line.charAt(6));
    String NUCID = line.substring(0, 5).strip();
    EnsdfQuantity E = new EnsdfQuantity(line.substring(9, 19), line.substring(19, 21));
    String J = line.substring(21, 39).strip();
    EnsdfTimeQuantity T = new EnsdfTimeQuantity(line.substring(39, 49), line.substring(49, 55));
    assertBlank(line, line.substring(55, 64));
    EnsdfQuantity QP = new EnsdfQuantity(line.substring(64, 74), line.substring(74, 76));
    String ION = line.substring(76, 80).strip();
    Nuclide nuclide = Nuclides.get(NUCID);
    if (nuclide != null && !E.field.equals("0.0"))
    {
//      // Assume always M1 for now
//      nuclide = Nuclides.get(NUCID + "m");
//      This code was used to decide when nuclide this record was for by
//      looking at the halflife.   Some ENSDF records are for the metastable state.
//       Search the meta stable list to find the appropriate metastable isomer
      List<Nuclide> isomers = Nuclides.getIsomers(nuclide.getAtomicNumber(), nuclide.getMassNumber());
      nuclide = null;
      for (Nuclide nuclide2 : isomers)
      {
        if (Math.abs(nuclide2.getHalfLife() - T.value) < 0.1 * T.value)
        {
          nuclide = nuclide2;
          break;
        }
      }
    }
    if (nuclide == null)
    {
      if (NUCID.equals("1NN"))
        nuclide = Nuclides.get(0, 1, 0);
      if (nuclide == null)
        System.out.println("Missing nuclide " + NUCID + " " + E.field);
    }

    EnsdfParent parent = new EnsdfParent(dataSet, nuclide, NUCID, E, J, T, QP, ION);
    dataSet.parents.add(parent);
    return parent;
  }

  /**
   * Parse the Normalization record.
   *
   * @param dataSet
   * @param line
   * @return
   */
  static EnsdfNormalization parseEnsdfNormalization(EnsdfDataSet dataSet, String line)
  {
    assertBlank(line, line.charAt(6));
    EnsdfQuantity NR = new EnsdfQuantity(line.substring(9, 19), line.substring(19, 21));
    EnsdfQuantity NT = new EnsdfQuantity(line.substring(21, 29), line.substring(29, 31));
    EnsdfQuantity BR = new EnsdfQuantity(line.substring(31, 39), line.substring(39, 41));
    EnsdfQuantity NB = new EnsdfQuantity(line.substring(41, 49), line.substring(49, 55));
    EnsdfQuantity NP = new EnsdfQuantity(line.substring(55, 62), line.substring(76, 80));
    assertBlank(line, line.substring(76, 80));
    EnsdfNormalization normalization = new EnsdfNormalization(dataSet, NR, NT, BR, NB, NP);
    dataSet.normalizations.add(normalization);
    return normalization;
  }

  static EnsdfProductionNormalization parseEnsdfProductionNorm(EnsdfDataSet dataSet, String line)
  {
    assertBlank(line, line.charAt(5));
    EnsdfQuantity NRBR = new EnsdfQuantity(line.substring(9, 19), line.substring(19, 21));
    EnsdfQuantity NTBR = new EnsdfQuantity(line.substring(21, 29), line.substring(29, 31));
    if (!line.substring(31, 41).isBlank())
    {
      System.out.println("Bad production norm record " + line);
    }
    EnsdfQuantity NBBR = new EnsdfQuantity(line.substring(41, 49), line.substring(49, 55));
    EnsdfQuantity NP = new EnsdfQuantity(line.substring(55, 62), line.substring(63, 65));
//    assertBlank(line, line.substring(65, 77));
    char COM = line.charAt(77);
    char OPT = line.charAt(78);
    dataSet.productionNormalization = new EnsdfProductionNormalization(dataSet, NRBR, NTBR, NBBR, NP, COM, OPT);
    return dataSet.productionNormalization;
  }

  /**
   * Parse the Level record.
   *
   * @param dataSet
   * @param line
   * @return
   */
  static EnsdfLevel parseEnsdfLevel(EnsdfDataSet dataSet, String line)
  {
    assertBlank(line, line.charAt(6));
    assertBlank(line, line.charAt(8));
    EnsdfQuantity E = new EnsdfQuantity(line.substring(9, 19), line.substring(19, 21));
    String J = line.substring(21, 39).strip();
    EnsdfTimeQuantity T = new EnsdfTimeQuantity(line.substring(39, 49), line.substring(49, 55));
    String L = line.substring(55, 64).strip();
    EnsdfQuantity S = new EnsdfQuantity(line.substring(64, 74), line.substring(74, 76));
    char C = line.charAt(76);
    String MS = line.substring(77, 79).strip();
    char Q = line.charAt(79);
    EnsdfLevel level = new EnsdfLevel(dataSet, E, J, T, L, S, C, MS, Q);
    dataSet.levels.add(level);
    return level;
  }

  /**
   * Parse the beta record
   *
   * @param dataSet
   * @param level
   * @param line
   * @return
   */
  static EnsdfBeta parseEnsdfBeta(EnsdfDataSet dataSet, EnsdfEmissions emissions, EnsdfLevel level, String line)
  {
    assertBlank(line, line.charAt(6));
    assertBlank(line, line.charAt(8));
    EnsdfQuantity E = new EnsdfQuantity(line.substring(9, 19), line.substring(19, 21));
    EnsdfQuantity IB = new EnsdfQuantity(line.substring(21, 29), line.substring(29, 31));
    EnsdfQuantity LOGFT = new EnsdfQuantity(line.substring(31, 49), line.substring(49, 55));
    assertBlank(line, line.substring(55, 76));
    char C = line.charAt(76);
    String UN = line.substring(77, 79).strip();
    char Q = line.charAt(79);
    EnsdfBeta beta = new EnsdfBeta(dataSet, E, IB, LOGFT, C, UN, Q);
    emissions.beta.add(beta);
    beta.level = level;
    return beta;
  }

  /**
   * Parse the electron capture record.
   *
   * @param dataSet
   * @param level
   * @param line
   * @return
   */
  static EnsdfElectronCapture parseEnsdfElectronCapture(EnsdfDataSet dataSet, EnsdfEmissions emissions, EnsdfLevel level, String line)
  {
    assertBlank(line, line.charAt(6));
    assertBlank(line, line.charAt(8));
    EnsdfQuantity E = new EnsdfQuantity(line.substring(9, 19), line.substring(19, 21));
    EnsdfQuantity IB = new EnsdfQuantity(line.substring(21, 29), line.substring(29, 31));
    EnsdfQuantity IE = new EnsdfQuantity(line.substring(31, 39), line.substring(39, 41));
    EnsdfQuantity LOGFT = new EnsdfQuantity(line.substring(41, 49), line.substring(49, 55));
    EnsdfQuantity TI = new EnsdfQuantity(line.substring(64, 74), line.substring(74, 76));
    char C = line.charAt(76);
    String UN = line.substring(77, 79).strip();
    char Q = line.charAt(79);
    EnsdfElectronCapture ec = new EnsdfElectronCapture(dataSet,
            E, IB, IE, LOGFT, TI, C, UN, Q);
    emissions.ec.add(ec);
    ec.level = level;
    return ec;
  }

  /**
   * Parse the alpha record.
   *
   * @param dataSet
   * @param level
   * @param line
   * @return
   */
  static EnsdfAlpha parseEnsdfAlpha(EnsdfDataSet dataSet, EnsdfEmissions emissions, EnsdfLevel level, String line)
  {
    assertBlank(line, line.charAt(6));
    assertBlank(line, line.charAt(8));
    EnsdfQuantity E = new EnsdfQuantity(line.substring(9, 19), line.substring(19, 21));
    EnsdfQuantity IA = new EnsdfQuantity(line.substring(21, 29), line.substring(29, 31));
    EnsdfQuantity HF = new EnsdfQuantity(line.substring(31, 39), line.substring(39, 41));
    assertBlank(line, line.substring(41, 76));
    char C = line.charAt(76);
    String UN = line.substring(77, 79).strip();
    char Q = line.charAt(79);
    EnsdfAlpha alpha = new EnsdfAlpha(dataSet, E, IA, HF, C, UN, Q);
    emissions.alpha.add(alpha);
    alpha.level = level;
    return alpha;
  }

  /**
   * Parse a delayed particle record.
   *
   * @param dataSet
   * @param level
   * @param line
   * @return
   */
  static EnsdfParticle parseEnsdfParticle(EnsdfDataSet dataSet, EnsdfEmissions emissions, EnsdfLevel level, String line)
  {
    char delayed = line.charAt(7);
    assertBlank(line, line.charAt(6));
    EnsdfParticleType PART = EnsdfParticleType.fromCode(line.charAt(8));
    EnsdfQuantity E = new EnsdfQuantity(line.substring(9, 19), line.substring(19, 21));
    EnsdfQuantity IP = new EnsdfQuantity(line.substring(21, 29), line.substring(29, 31));
    String EI = line.substring(31, 39).strip();
    EnsdfQuantity T = new EnsdfQuantity(line.substring(39, 49), line.substring(49, 55));
    String L = line.substring(55, 64).strip();
    assertBlank(line, line.substring(64, 76));
    char C = line.charAt(76);
    char COIN = line.charAt(77);
    char B = line.charAt(78);
    char Q = line.charAt(79);
    EnsdfParticle particle = new EnsdfParticle(dataSet, delayed, PART, E, IP, EI, T, L, C, COIN, B, Q);
    emissions.particles.add(particle);
    particle.level = level;
    return particle;
  }

  /**
   * Parse a gamma transition record.
   *
   * @param dataSet
   * @param level
   * @param line
   * @return
   */
  static EnsdfGamma parseEnsdfGamma(EnsdfDataSet dataSet, EnsdfEmissions emissions, EnsdfLevel level, String line)
  {
    if (line.length() < 80)
    {
      System.out.println("Short line " + line);
    }
    assertBlank(line, line.charAt(6));
    assertBlank(line, line.charAt(8));
    EnsdfQuantity E = new EnsdfQuantity(line.substring(9, 19), line.substring(19, 21));
    EnsdfQuantity RI = new EnsdfQuantity(line.substring(21, 29), line.substring(29, 31));
    String M = line.substring(31, 41).strip();
    EnsdfQuantity MR = new EnsdfQuantity(line.substring(41, 49), line.substring(49, 55));
    EnsdfQuantity CC = new EnsdfQuantity(line.substring(55, 62), line.substring(62, 64));
    EnsdfQuantity TI = new EnsdfQuantity(line.substring(64, 74), line.substring(74, 76));
    char C = line.charAt(76);
    char COIN = line.charAt(77);
    assertBlank(line, line.charAt(78));
    char Q = line.charAt(79);
    EnsdfGamma gamma = new EnsdfGamma(dataSet, E, RI, M, MR, CC, TI, C, COIN, Q);
    emissions.gamma.add(gamma);
    gamma.level = level;
    return gamma;
  }

//  static void storeEnsdfTComment(EnsdfDataSet dataSet, String line)
//  {
//      dataSet.TComments.add(line);
//  } delete this to merge with master branch
  /**
   * Parse the identification record.
   *
   * @param dataSet
   * @param lines
   */
  static EnsdfIdentification parseEnsdfIdentifier(EnsdfDataSet dataSet, LineSource lines)
  {
    String line = lines.pop();
    Nuclide target = Nuclides.get(line.substring(0, 5).strip());
    String NUCID = line.substring(0, 5).strip();
    assertBlank(line, line.substring(5, 9));
    String DSID = line.substring(9, 39).strip();
    String DSREF = line.substring(39, 65).strip();
    String PUB = line.substring(65, 74).strip();
    String DATE = line.substring(74, 80).strip();
    dataSet.identification = new EnsdfIdentification(dataSet, target,
            NUCID, DSID, DSREF, PUB, DATE);

    // Check for a continuation line
    line = lines.pop();
    if (line.charAt(5) == '2')
    {
      System.out.println("Handle continuation " + line);
    } else
    {
      lines.push(line);
    }

    return dataSet.identification;
  }

  /**
   * Parse the general section. \
   *
   * @param dataSet
   * @param lines
   */
  static void parseEnsdfGeneral(EnsdfDataSet dataSet, LineSource lines)
  {
    EnsdfHistory last = null;
    while (!lines.isEmpty())
    {
      String line = lines.pop();
      char CONT = line.charAt(5);
      char TYPE = line.charAt(7);
      if (TYPE == 'H')
      {
        // FIXME typo in 123I -> 123TE
        if (line.charAt(6) == '2')
        {
          System.out.println("Bad history record " + line);
          continue;
        }
        assertBlank(line, line.charAt(8));
        assertBlank(line, line.charAt(6));
        String HIST = line.substring(9, 80);
        if (CONT == ' ')
        {
          last = new EnsdfHistory(dataSet, HIST);
          dataSet.history.add(last);
        } else
        {
          last.value.append(HIST);
        }
      } else
      {
        lines.push(line);
        return;
      }
    }
  }

  /**
   * Parse comments.
   *
   * @param dataSet
   * @param lines
   */
  static void parseEnsdfComments(EnsdfDataSet dataSet, LineSource lines, EnsdfRecord target)
  {
    while (!lines.isEmpty())
    {
      String line = lines.pop();
      if (line.isBlank())
      {
        lines.push(line);
        break;
      }
      char CONT = line.charAt(5);
      char TYPE = line.charAt(6);
      if (isComment(TYPE))
      {
        target.comments.add(new EnsdfComment(dataSet, TYPE, CONT, line.substring(8)));
        // deal with comment
      } else
      {
        lines.push(line);
        return;
      }
    }
  }

  /**
   * Parse the QPN record.
   *
   * @param dataSet
   * @param lines
   */
  static void parseEnsdfQPN(EnsdfDataSet dataSet, LineSource lines)
  {
    String line = null;
    try
    {
      EnsdfRecord last = null;
      while (!lines.isEmpty())
      {
        line = lines.pop();
        if (line.isBlank())
        {
          lines.push(line);
          return;
        }
        if (isComment(line.charAt(6)))
        {
          lines.push(line);
          parseEnsdfComments(dataSet, lines, last);
          continue;
        }

        if (line.charAt(6) == 'P' && line.charAt(7) == 'N')
        {
          if (line.charAt(5) != ' ')
          {
            // FIXME comment on production norm
            continue;
          }
          last = parseEnsdfProductionNorm(dataSet, line);
          continue;
        }
        char TYPE = line.charAt(7);
        switch (TYPE)
        {
          case 'Q':
            last = parseEnsdfQ(dataSet, line);
            break;
          case 'P':
            last = parseEnsdfParent(dataSet, line);
            break;
          case 'N':
            last = parseEnsdfNormalization(dataSet, line);
            break;
          default:
            lines.push(line);
            return;
        }
      }
    } catch (Exception ex)
    {
      throw new RuntimeException("Fail on QPN in " + line, ex);
    }
  }

  /**
   * Parse a continuation record.
   *
   * @param dataSet
   * @param last
   * @param line
   */
  static void parseEnsdfContinuation(EnsdfDataSet dataSet, EnsdfExtendable last, String line)
  {
    char TYPE = line.charAt(7);
    if (last == null)
      throw new RuntimeException("Unexpected record: " + line);
    if (TYPE != last.type)
      throw new RuntimeException("Type mismatch on continuation:" + line + " " + last);
    String[] parts = line.substring(9).split("\\$");

    // Quantity with uncertainty (optional reference)
    Pattern pattern1a = Pattern.compile("([-()%1-9:A-z+/]+)([=<>]| AP| G[ET]| L[TE]| \\|\\?) *((?:\\(\\+\\))?[-+.0-9Ee?]+(?:\\+[XYZ])?(?: [SYCAEV]+)?)( +[-+0-9]+)?( *\\([^)]*\\))?");
    // Quantity with uncertainty in parathesis (optional reference)
    Pattern pattern1b = Pattern.compile("([-()%1-9:A-z+/]+)([=<>]) *\\(([-+.0-9Ee]+)( +[-+0-9]+)\\)( *\\([^)]*\\))?");

    // Quantity without uncertainty (optional reference)
    Pattern pattern2 = Pattern.compile("([-%1-9:A-z+/ ]+)([=<>]) *([-()+.INFTXYZ0-9Ee/?]+)( *\\([^)]*\\))?");

    // FLAG
    Pattern pattern3 = Pattern.compile("FLAG=([&@A-z0-9|!#~]+)( *\\([^)]*\\))?");

    // Ratio (Ie.  K:L=1:1)
    Pattern pattern7 = Pattern.compile("([KLMNOC+123:]+)(?:=| AP)([-+0-9+:. ]+)( *\\([^)]*\\))?");

    // CONF=
    Pattern pattern9 = Pattern.compile("(CONF)=([-()0-9A-z +,/]+)");

    // Two limits   "A LT 5 GT 2"
    Pattern pattern10 = Pattern.compile("([A-z]+) ((?: *[LG][TE] [-+0-9. ]+)+)");

    // "EAV="   seems like an error in ENSDF
    Pattern pattern11 = Pattern.compile("([A-z]+)=");

    for (String part : parts)
    {
      part = part.strip();
      if (part.isEmpty())
        continue;

      Matcher matcher = pattern3.matcher(part);
      if (matcher.matches())
      {
        last.continuation.put(matcher.group(1),
                new EnsdfQuantity(matcher.group(2), null));
        continue;
      }

      matcher = pattern1a.matcher(part);
      if (matcher.matches())
      {
        if ("=".equals(matcher.group(3)))
          last.continuation.put(matcher.group(1),
                  new EnsdfQuantity(matcher.group(3), matcher.group(4), matcher.group(5)));
        else
          last.continuation.put(matcher.group(1),
                  new EnsdfInequality(matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5)));
        continue;
      }
      matcher = pattern1b.matcher(part);
      if (matcher.matches())
      {
        if ("=".equals(matcher.group(3)))
          last.continuation.put(matcher.group(1),
                  new EnsdfQuantity(matcher.group(3), matcher.group(4), matcher.group(5)));
        else
          last.continuation.put(matcher.group(1),
                  new EnsdfInequality(matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5)));
        continue;
      }

      matcher = pattern2.matcher(part);
      if (matcher.matches())
      {
        if ("=".equals(matcher.group(3)))
          last.continuation.put(matcher.group(1),
                  new EnsdfQuantity(matcher.group(3), null, matcher.group(4)));
        else
          last.continuation.put(matcher.group(1),
                  new EnsdfInequality(matcher.group(2), matcher.group(3), null, matcher.group(4)));
        continue;
      }

      matcher = pattern7.matcher(part);
      if (matcher.matches())
      {
        last.continuation.put(matcher.group(1),
                new EnsdfRatio(matcher.group(2), matcher.group(3)));
        continue;
      }
      matcher = pattern9.matcher(part);
      if (matcher.matches())
      {
        last.continuation.put(matcher.group(1),
                new EnsdfQuantity(matcher.group(2), null, null));
        continue;
      }

      matcher = pattern10.matcher(part);
      if (matcher.matches())
      {
        last.continuation.put(matcher.group(1),
                new EnsdfQuantity(matcher.group(2), null, null));
        continue;
      }

      // This one seems wrong
      matcher = pattern11.matcher(part);
      if (matcher.matches())
      {
        last.continuation.put(matcher.group(1),
                new EnsdfQuantity(null, null, null));
        continue;
      }

      System.out.println("Unrecognized part \"" + part + "\"");
//      throw new RuntimeException("Unrecognized part: \"" + part + "\" : line=" + line);
    }
  }

  /**
   * Parse an unassigned records.
   *
   * @param dataSet
   * @param lines
   */
  static void parseEnsdfUnassigned(EnsdfDataSet dataSet, LineSource lines)
  {
    EnsdfUnassigned unassigned = dataSet.unassigned;
    boolean first = true;
    EnsdfEmission last = null;
    while (!lines.isEmpty())
    {
      String line = lines.pop();
      if (line.isBlank())
      {
        lines.push(line);
        return;
      }
      char CONT = line.charAt(5);
      char COM = line.charAt(6);
      if (isComment(COM))
      {
        lines.push(line);
        parseEnsdfComments(dataSet, lines, last);
        continue;
      }
      char TYPE = line.charAt(7);

      // If there is anything in the continuation character then 
      // this record is a continuation of the last.
      if (CONT != ' ')
      {
        parseEnsdfContinuation(dataSet, last, line);
        continue;
      }
      switch (TYPE)
      {
        case 'B':
          last = parseEnsdfBeta(dataSet, unassigned, null, line);
          break;
        case 'E':
          last = parseEnsdfElectronCapture(dataSet, unassigned, null, line);
          break;
        case 'A':
          last = parseEnsdfAlpha(dataSet, unassigned, null, line);
          break;
        case ' ':
        case 'D':
          last = parseEnsdfParticle(dataSet, unassigned, null, line);
          break;
        case 'G':
          last = parseEnsdfGamma(dataSet, unassigned, null, line);
          break;
        case 'L':
          // Start of L records are the end of the unassigned
          lines.push(line);
          return;
        default:
          throw new RuntimeException("Bad line:" + line);
      }
      first = false;
    }
    if (first)
    {
      dataSet.unassigned = unassigned;
    }
  }

  /**
   * Parse the levels section.
   *
   * @param dataSet
   * @param lines
   */
  static void parseEnsdfLevels(EnsdfDataSet dataSet, LineSource lines)
  {
    EnsdfLevel level = null;
    EnsdfExtendable last = null;
    while (!lines.isEmpty())
    {
      String line = lines.pop();
      if (line.isBlank())
        return;
      char CONT = line.charAt(5);
      if (isComment(line.charAt(6)))
      {
        lines.push(line);
        parseEnsdfComments(dataSet, lines, last);
        continue; // EAT COMMENT
      }
      assertBlank(line, line.charAt(6));
      char TYPE = line.charAt(7);

      if (CONT != ' ')
      {
        parseEnsdfContinuation(dataSet, last, line);
        continue;
      }
      switch (TYPE)
      {
        case 'L':
          // Start a new level
          level = parseEnsdfLevel(dataSet, line);
          last = level;
          break;
        case 'B':
          last = parseEnsdfBeta(dataSet, level, level, line);
          break;
        case 'E':
          last = parseEnsdfElectronCapture(dataSet, level, level, line);
          break;
        case 'A':
          last = parseEnsdfAlpha(dataSet, level, level, line);
          break;
        case ' ':
        case 'D':
          last = parseEnsdfParticle(dataSet, level, level, line);
          break;
        case 'G':
          last = parseEnsdfGamma(dataSet, level, level, line);
          break;
        default:
          throw new RuntimeException("Bad line:" + line);
      }
    }
  }

  /**
   * Consume all blank lines after a record.
   *
   * @param record
   * @param lines
   */
  static void parseEnsdfEnd(EnsdfDataSet record, LineSource lines)
  {
    while (!lines.isEmpty())
    {
      String line = lines.pop();
      if (line.isBlank())
        continue;
      lines.push(line);
      break;
    }
  }

  private static boolean isComment(char charAt)
  {
    return charAt == 'C' || charAt == 'D' || charAt == 'T'
            || charAt == 'c' || charAt == 'd' || charAt == 't';
  }

}
