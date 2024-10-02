/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import gov.llnl.utility.annotation.Matlab;
import java.lang.reflect.Method;

/**
 * Implementations of special functions we have required. Does not include exp,
 * expm1, log, log1p, sinh, cosh, tanh, sqrt, cbrt, pow, asin, and acos which
 * are all part of java.lang.Math.
 *
 * @author nelson85
 */
public class SpecialFunctions
{

  /**
   * Approximation for erfinv.
   *
   * Constructed using Mathematica.
   *
   * @param x
   * @return the err function inverse.
   */
  public static double erfinv(double x)
  {
    if (x == 0)
      return 0;
    if (x < 0)
      return -erfinv(-x);

    double w = -Math.log((1 - x) * (1 + x));
    double n, d;
    if (w < 3.45)
    {
      /*
        m = SetPrecision[1.5, 30]
        u = SetPrecision[
             PadeApproximant[
             InverseErf[Sqrt[1 - Exp[-w]]]/Sqrt[1 - Exp[-w]] , {w, m, {6, 6}}], 30]
        Print[NumberForm[CoefficientList[Numerator[u //. (-m + w) -> y], y], 25]]
        Print[NumberForm[CoefficientList[Denominator[u //. (-m + w) -> y], y], 25]]
       */
      w -= 1.5;
      n = 1.252063840583442036433263 + w * (0.5380584190819900833977914
              + w * (0.1221526179597124238796725
              + w * (0.01779255957279165722217822
              + w * (0.001691358689944868285247066
              + w * (0.00009787393428978440954689497
              + w * (2.676026826385220778859289e-6))))));
      d = 1. + w * (0.2297773998532498655277880
              + w * (0.05091211763264040457508388
              + w * (0.005539857443464884765033239
              + w * (0.0004494148677735703259827607
              + w * (0.00001828648164965060337400090
              + w * (2.154490000287383085706267e-7))))));
      return n * x / d;
    }
    if (w > 10.3)
    {
      /*
        m = SetPrecision[4, 30]
        u =SetPrecision[PadeApproximant[InverseErf[Sqrt[1 - Exp[-w^2]]], {w, m, {6, 6}}], 30]
        Print[NumberForm[CoefficientList[Numerator[u //. (-m + w) -> y], y], 30]]
        Print[NumberForm[CoefficientList[Denominator[u //. (-m + w) -> y], y],30]]
       */
      w = Math.sqrt(w) - 4;
      n = 3.8397835813561773121259
              + w * (6.40606141817807604873450527672
              + w * (4.91646876250136741085330904788
              + w * (2.10537490874210636928893364272
              + w * (0.508016023883569847058045559907
              + w * (0.0633632760824030951430985478726
              + w * (0.00308703563325252013227516605271))))));
      d = 1.
              + w * (1.40543506657355026732416844459
              + w * (0.910581421468976162170508303895
              + w * (0.308673586635137049791633573337
              + w * (0.0510901507380644397641886001543
              + w * (0.00308561517071538672599065383562
              + w * (3.35840388441975655044564681664e-9))))));
    }
    else
    {
      w = Math.sqrt(w) - 2.5;
      n = 2.33368955690714387635337572 + w * (2.02874539567545870602821931572
              + w * (1.14449090844734728013274085823
              + w * (0.343295973133138669769414530280
              + w * (0.0325130206074192534419948809892
              + w * (-0.00375331798256883643810063847298
              + w * (-0.00281167088594418422036289325941))))));
      d = 1. + w * (0.443990650780212965834329850310
              + w * (0.295609917624835638765143274286
              + w * (0.0213047599525304036009376054831
              + w * (0.00373354680395452695960993420209
              + w * (-0.00293245623736438280475179213684
              + w * (0.0000111924432836777578109331474155))))));
    }
    return n / d;
  }

  /**
   * Error function.
   *
   * @param x
   * @return \( \displaystyle erf(x)= \frac{2}{\sqrt{\pi}}
   * \int_{0}^{x}{e^{-t^2}dt} \)
   */
  public static double erf(double x)
  {
    if (x < 0)
      return -erf(-x);

    // Deal with the region where the value is very small.
    if (x < 0.1)
    {
      /*
        m = SetPrecision[0, 30]
        u = SetPrecision[PadeApproximant[Erf[w], {w, m, {6, 6}}], 30]
        Print[NumberForm[CoefficientList[Numerator[u //. (-m + w) -> y], y], 25]]
        Print[NumberForm[CoefficientList[Denominator[u //. (-m + w) -> y], y],25]]
       */
      double x2 = x * x;
      return x * (1.1283791670955126 + 0.11317568551751440 * x2
              + 0.033311870314118763 * x2 * x2) / (1 + x2 * (0.43363267314552836 + x2 * (0.074066100791405257
              + 0.0051349567587727263 * x2)));
    }
    if (x < 0.4)
    {
      /*
        m = SetPrecision[0.25, 30]
        u = SetPrecision[PadeApproximant[Erf[w], {w, m, {6, 6}}], 30]
        Print[NumberForm[CoefficientList[Numerator[u //. (-m + w) -> y], y], 25]]
        Print[NumberForm[CoefficientList[Denominator[u //. (-m + w) -> y], y],25]]
       */
      x = x - 0.25;
      return (0.27632639016823696 + x * (1.1156145161119599
              + x * (0.071715108805455341 + x * (0.12968704062787029
              + x * (0.039142060403787829 + x * (0.032191915923229890
              - x * 0.00023218718109118894))))))
              / (1 + x * (0.20121272782521496
              + x * (0.44668302064561055 + x * (0.067635953050670578
              + x * (0.076170108574933698 + x * (0.0067757443023838768
              + x * (0.0050179377835603070)))))));
    }
    return 1.0 - erfc(x);
  }

  public static double gammaln(double x)
  {
    return LogGammaFunction.evaluate(x);
  }

  public static double atanh(double x)
  {
    return 0.5 * Math.log((1.0 + x) / (1.0 - x));
  }

  /**
   * This function maps all real numbers to the positive domain. Accurate from
   * -745 to infinity.
   *
   * @param x
   * @return \( \displaystyle U(x)=\left\{\begin{matrix} x-log\left (
   * 1-\frac{1}{1+e^x} \right ) &amp;&amp; x\geqslant 0 \\ log(e^x+1) &amp;&amp;
   * x&lt;0 \end{matrix}\right. \)
   */
  public static double positiveAsymptotic(double x)
  {
    if (x < 0)
      return Math.log1p(Math.exp(x));
    return x - Math.log(1 - 1 / (1 + Math.exp(x)));
    // alternatives were 0.5*(x+sqrt(1+x^2))
  }

  public static double logerfc(double x)
  {
    if (x > 20)
    {
      double u = x * x;
      // Log of continued fraction expansion of erfc.
      // Cuyt, Annie A. M.; Petersen, Vigdis B.; Verdonk, Brigitte; Waadeland, Haakon; Jones, William B. (2008).
      // Handbook of Continued Fractions for Special Functions.
      return -u + Math.log(x / MathConstants.SQRT_PI / (u + 0.5 / (1 + 1 / (u + 1.5 / (1 + 2 / (u + 2.5))))));
    }
    return Math.log(erfc(x));
  }

  public static double erfcinv(double x)
  {
    return erfinv(1.0 - x);
  }

  /**
   * Gamma incomplete function for upper incomplete gamma function.
   *
   * @param a is the gamma factorial.
   * @param x is the upper limit.
   * @return \( \displaystyle P(a,x)=\frac{1}{\Gamma (x)} \int_{-\infty}^{x}
   * t^{a-1}e^{-t} dt \)
   */
  public static double gammaP(double a, double x)
  {
    RegularizedGammaFunction rgf = new RegularizedGammaFunction();
    rgf.evaluate(a, x);
    return rgf.getP();
  }

  /**
   * Gamma incomplete function for lower regularized incomplete gamma function.
   *
   * @param a is the gamma factorial.
   * @param x is the lower limit.
   * @return \( \displaystyle Q(a,x)=\frac{1}{\Gamma (x)} \int_{x}^{\infty}
   * t^{a-1}e^{-t} dt \)
   *
   */
  public static double gammaQ(double a, double x)
  {
    RegularizedGammaFunction rgf = new RegularizedGammaFunction();
    rgf.evaluate(a, x);
    return rgf.getQ();
  }

  /**
   * Logistic function
   *
   * @param x is the parameter.
   * @param x0 is the midpoint.
   * @param k is the the steepness of the curve.
   * @return
   */
  public static double logistic(double x, double x0, double k)
  {
    if (x > x0)
      return 1 / (1 + Math.exp(-k * (x - x0)));
    double t = Math.exp(k * (x - x0));
    return t / (t + 1);
  }

  /**
   * Convenience function for Matlab plotting. }
   *
   * @param methodName is the name of a special function
   * @return the method if it exists or null otherwise.
   */
  @Matlab static public Method getFunction(String methodName)
  {
    try
    {
      Class cls = SpecialFunctions.class;
      @SuppressWarnings("unchecked")
      Method out = cls.getDeclaredMethod(methodName, double.class);
      return out;
    }
    catch (NoSuchMethodException | SecurityException ex)
    {
      throw null;
    }
  }

  /**
   * Complementary error function.
   *
   * @param x is the integration limit.
   * @return \( \displaystyle erfc(x)= \frac{2}{\sqrt{\pi}}
   * \int_{x}^{\infty}{e^{-t^2}dt} = 1-erf(x) \)
   */
  public static double erfc(double x)
  {
    double offset = 0;
    double factor = 1;
    if (x < 0)
    {
      offset = 2;
      factor = -1;
      x = -x;
    }
    if (x == 0)
      return 1.0;

    double r = -x * x;
    factor *= exp(r);
    if (x > 13)
      return factor * erfcx_large(x) + offset;
    int table = erfcx_table(x);
    return factor * erfcx_small(x, table) + offset;
  }

  public static double erfcx(double x)
  {
    if (x == 0)
      return 1.0;
    if (x < 0)
      return exp(x * x) * erfc(x);
    if (Double.isNaN(x))
      return Double.NaN;
    if (x > 13)
      return erfcx_large(x);
    int table = erfcx_table(x);
    return erfcx_small(x, table);
  }

//<editor-fold desc="erfcx" defaultstate="collapse">
  final static double[][] ERFCX_TABLE = new double[][]
  {
    new double[]
    {
      0.15309310892394862, 0.84824259373890293, 0.95538218270973362, 0.54204532423477613,
      0.16998195845471560, 0.029478924678469832, 0.0022144685174198387, 2.1503767573976580,
      1.9979337481773847, 1.0298217014718435, 0.31174612162390739, 0.052790172783559808, 0.0039290389277024201
    },
    new double[]
    {
      0.45927932677184591, 0.63717857532175896, 0.74551858323427354, 0.41240886234221784,
      0.12493846332785108, 0.020581848498298456, 0.0014592741430848356, 2.0223716019714367,
      1.7624544445690275, 0.84950493337065050, 0.23962703470295767, 0.037653443829944664, 0.0025874113224154018
    },
    new double[]
    {
      0.61237243569579447, 0.56231959772614625, 0.66365396361060931, 0.36206324623760550,
      0.10757661727523850, 0.017270210444257539, 0.0011889616935785956, 1.9621138793127659,
      1.6568810480829639, 0.77271410188332228, 0.21054242269530405, 0.031893628688781924, 0.0021078283959956644
    },
    new double[]
    {
      0.91855865354369182, 0.45090274083351707, 0.53354475865228489, 0.28226891599114137,
      0.080448879160295493, 0.012260437594446930, 0.00079559805525156852, 1.8486529826998220,
      1.4672328301194197, 0.64136516121856263, 0.16327814695666900, 0.023024356679387072, 0.0014102737082096019
    },
    new double[]
    {
      1.2247448713915889, 0.37316567427801551, 0.43649597127963513, 0.22311179653943935,
      0.060844251169294402, 0.0087992407300064760, 0.00053832154815185338, 1.7440233433354718,
      1.3028897989697433, 0.53470398793872850, 0.12742818390677706, 0.016764265009035735, 0.00095418020427663402
    },
    new double[]
    {
      1.8371173070873836, 0.27397089384189582, 0.30526304517241766, 0.14448563039965325,
      0.035935822105115605, 0.0046803836013645625, 0.00025507463299112455, 1.5585921002398742,
      1.0363443287366656, 0.37683537945222902, 0.079161586185991827, 0.0091262851167847225, 0.00045211053599802137
    },
    new double[]
    {
      2.4494897427831779, 0.21462633906982060, 0.22371836457254280, 0.097474802058175709,
      0.022078723137675040, 0.0025949770341844433, 0.00012654921850420251, 1.4007949853954829,
      0.83422909386743793, 0.27065091125711184, 0.050512125952420586, 0.0051488978509704661, 0.00022430290705342116
    },
    new double[]
    {
      3.6742346141747673, 0.14840374411978889, 0.13296522855695844, 0.048954655081686047,
      0.0092450261100027102, 0.00089504278589091886, 0.000035537363580801356, 1.1509416398448424,
      0.56016030785224189, 0.14765474435728580, 0.022246765332948918, 0.0018178558261380106, 0.000062988341420173349
    },
    new double[]
    {
      4.8989794855663558, 0.11290171377607302, 0.086911621440737191, 0.027256248175182237,
      0.0043507983611326186, 0.00035344005362122157, 0.000011690233143937818, 0.96618931227273671,
      0.39327830439534181, 0.086351831956951793, 0.010790945606861873, 0.00072796498754051143, 0.000020720398892544796
    },
    new double[]
    {
      7.3484692283495345, 0.076084471936853084, 0.044555522209045273, 0.010541303519129678,
      0.0012593625007429586, 0.000075974165188158532, 1.8516074453868341e-6, 0.71927801225460132,
      0.21697949128957356, 0.035142219062153927, 0.0032233528815571736, 0.00015877756010154887, 3.2818887474160659e-6
    },
    new double[]
    {
      9.7979589711327115, 0.057287018412509724, 0.026676926720672740, 0.0049998792068215791,
      0.00047144353073598196, 0.000022363908417360508, 4.2698548251327740e-7, 0.56669700653345653,
      0.13437323820248832, 0.017065458171933667, 0.0012243715600843539, 0.000047054209135102389, 7.5681206276684693e-7
    },
    new double[]
    {
      14.696938456699069, 0.038299987232156634, 0.012475016676119845, 0.0016302042044093155,
      0.00010683425261726600, 3.5111361386306199e-6, 4.6296283615431242e-8, 0.39344851980128407,
      0.064635401121401674, 0.0056749647544224226, 0.00028086366200737329, 7.4293285305671817e-6, 8.2058026176791599e-8
    }
  };

  private static int erfcx_table(double x)
  {
    long bits = Double.doubleToLongBits(x);
    long exponent = (bits & 0x7ff0000000000000L) >> 52;
    long mantissa = bits & 0x000fffffffffffffL;
    int table = (int) ((exponent - 1021) * 2);
    table++;
    if (mantissa < 2251799813685248L)
      table--;
    return table;
  }

  private static double erfcx_large(double x)
  {
    double u = 1 / x;
    double u2 = u * u;
    double u3 = u2 * u;
    double u4 = u3 * u;
    return ((19.676111726228000507 * u4 + 26.093768239083728271 * u2 + 7.6165593778947098738) * u3 + 0.56418958354775628695 * u) / (1.0 + (6.56250 * u4 + 52.50 * u2 + 52.50) * u4 + 14.0 * u2);
  }

  private static double erfcx_small(double x, int table)
  {
    if (table < 0)
      table = 0;
    if (table >= ERFCX_TABLE.length)
      table = ERFCX_TABLE.length - 1;
    double[] B = ERFCX_TABLE[table];
    double t = x - B[0];
    double t2 = t * t;
    double t3 = t2 * t;
    double t4 = t2 * t2;
    double t5 = t3 * t2;
    double n = B[1] + B[2] * t + B[3] * t2 + B[4] * t3 + B[5] * t4 + B[6] * t5;
    double d = 1 + B[7] * t + B[8] * t2 + B[9] * t3 + B[10] * t4 + B[11] * t5 + B[12] * t5 * t;
    return n / d;
  }

//</editor-fold>
//<editor-fold desc="exp" defaultstate="collapsed">
  final static double EXP_F = 0.693147180559945309417232;
  final static double[] EXP_N = new double[]
  {
    0.50000000000000000000, -0.15995704166767968679, 0.023098702592221222340,
    -0.0019407031001685867116, 0.00010088946616393507513, -3.1080555119879821500e-6,
    4.4882081565797231802e-8
  };
  final static double[] EXP_D = new double[]
  {
    0.37323309722458593584, 0.064676367258219422551, 0.0067924608505900534908,
    0.00047081750876503035059, 0.000021756388583915875050, 6.2834914192116124523e-7,
    8.8885395128553847786e-9
  };

  private static double exp_pade(double m)
  {
    m = -2 * m - 1;
    double n = (((((EXP_N[6] * m + EXP_N[5]) * m + EXP_N[4]) * m + EXP_N[3]) * m + EXP_N[2]) * m + EXP_N[1]) * m + EXP_N[0];
    double d = ((((((EXP_D[6] * m + EXP_D[5]) * m + EXP_D[4]) * m + EXP_D[3]) * m + EXP_D[2]) * m + EXP_D[1]) * m + EXP_D[0]) * m + 1;
    double p = n / d;
    return p;
  }

  public static double exp(double x0)
  {
    double x = x0;
    if (x > 0)
    {
      double u = exp(-x);
      if (u == 0)
        return Double.POSITIVE_INFINITY;
      return 1 / u;
    }

    // Special cases
    if (x == 0)
      return 1.0;
    if (Double.isNaN(x))
      return Double.NaN;
    if (x<-745)
      return 0.0;
    x /= EXP_F;

    // Deal with whole portion
    int whole = (int) x;
    x = x - whole;
    whole = -whole;
    double q = 1;
    double u = 0.5;
    while (whole > 0)
    {
      if ((whole & 1) == 1)
        q *= u;
      u = u * u;
      whole >>= 1;
    }
    if (q == 0)
      return 0.0;

    // Pade Approximate
    double p = exp_pade(x / 2);
    return q * p;
  }

//</editor-fold
}
