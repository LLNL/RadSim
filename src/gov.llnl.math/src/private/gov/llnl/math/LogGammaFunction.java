/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

/**
 *
 * @author nelson85
 */
class LogGammaFunction
{
  public static double evaluate(double x)
  {
    if ((x < 0) || (x == 1))
      return 0;
    if (x >= 25)
      return evaluateStieltjes(x, 5);
    if (x >= 8)
      return evaluateStieltjes(x, 9);
    if (x >= 5)
      return evaluateStieltjes(x, 13);

    // Extend to values greater than 5 to improve accuracy for small numbers
    double u = 1;
    int k = 5;
    for (int i = 0; i < k + 1; ++i)
    {
      u = u * (x + i);
    }

    return evaluateStieltjes(x + k + 1, 10) - Math.log(u);
  }

  // Different implementations
  static double evaluateWindschitl(double x)
  {
    // Windschitl approximation, Good for x>30
    double logx = Math.log(x);
    return 0.91893853320467267 - 0.5 * logx + x * (logx - 1 + 0.5
            * Math.log(x * Math.sinh(1 / x) + 1 / 810 / (x * x * x * x * x * x)));
  }

  static double evaluateLanczos(double x)
  {
    // Method used in GNU Scientific Library
    // About the same as Stieltjes overall but somewhat worse in 5 to 10 range
    int g = 7;
    double p[] =
    {
      0.99999999999980993, 676.5203681218851, -1259.1392167224028,
      771.32342877765313, -176.61502916214059, 12.507343278686905,
      -0.13857109526572012, 9.9843695780195716e-6, 1.5056327351493116e-7
    };

    x = x - 1;
    double u = p[0];
    for (int i = 1; i < g + 2; ++i)
    {
      u = u + p[i] / (x + i);
    }
    double t = x + g + 0.5;
    return 0.91893853320467267 + (x + 0.5) * Math.log(t) - t + Math.log(u);
  }

  static double evaluateSterlings(double x)
  {
    // Good for x>18
    x = x - 1;

    double x3 = x * x * x;
    double x5 = x3 * x * x;
    double x7 = x3 * x * x;
    return (x + 0.5) * Math.log(x) - x + 0.91893853320467267
            + (1 / 12 / x - 1 / 360 / x3 + 1 / 1260 / x5 - 1 / 1680 / x7);
  }

  static double evaluateStieltjes(double x)
  {
    return evaluateStieltjes(x, 15);
  }

  static double evaluateStieltjes(double x, int n)
  {
    // Accuracy varies with iterations
    //  x>50  n=4 
    //  x>25  n=5 
    //  x>8   n=8 
    //  x>5   n=13 
    //  x>3.5 n=40 
    // We can extend the range using the recursion relationship
    //  \Gamma(x)=\Gamma(x+1)/x
    x = x - 1;
    double P = 0.91893853320467267 - x + (x + 0.5) * Math.log(x);

    if (n > 40)
      n = 40;

    // http://www.ams.org/journals/mcom/1980-34-150/S0025-5718-1980-0559203-3/S0025-5718-1980-0559203-3.pdf
    // Char B.W., "On Stieltjes' Continued Fraction for the Gamma Function,"
    // MATHEMATICS OF COMPUTATION, VOLUME 34, NUMBER 150, APRIL 1980, 
    // PAGES 547-551
    final double A[] =
    {
      8.333333333333333333333333333333333333333E-2,
      3.333333333333333333333333333333333333333E-2,
      2.523809523809523809523809523809523809524E-1,
      5.256064690026954177897574123989218328841E-1,
      1.011523068126841711747372124730615296653E0,
      1.517473649153287398428491519495476189246E0,
      2.269488974204959960909150672209877584177E0,
      3.009917383259398170073140734207715727374E0,
      4.026887192343901226168875953181442836327E0,
      5.002768080754030051688502412276188574812E0,
      6.283911370815782180072663154951647264278E0,
      7.495919122384033929752354708267505465746E0,
      9.040660234367726699531139360260481749336E0,
      1.048930365450948227718837130459262952210E1,
      1.229719361038620586398943714009191765974E1,
      1.398287695399243018825976065127873008591E1,
      1.605355141670493546971561636500626017835E1,
      1.797660739987027759256947230767155439932E1,
      2.030976202744165374380541472049489689370E1,
      2.247047163993313249551794157150792210900E1,
      2.506584654894597202916340032250630536824E1,
      2.746445182502913360917555898264622267323E1,
      3.032182123167304712688259930640578699449E1,
      3.295853392997298721999406645141208820696E1,
      3.607769893129924264515332090085545233678E1,
      3.895270668231155573454439041048104629916E1,
      4.233349004357695721138185394885609733991E1,
      4.544696085006162101442417573754145108285E1,
      4.908920312901259770816488335027508729245E1,
      5.244128875141533731256985604699610842715E1,
      5.634484534534184353842036594747611354213E1,
      5.993568390716585820785258349275211211013E1,
      6.410042275592035452790661189223791775291E1,
      6.793014078801822118636770274519853581652E1,
      7.235594055521170196968005296323621791075E1,
      7.642465462682968975258509042228752640357E1,
      8.111140323724796548481423098568346097450E1,
      8.541922127641097261458563871734868272699E1,
      9.036681472386410859551357458168337778079E1,
      9.491383710000988795307623129198692745877E1,
      1.001221784639291974889907468344668349799E2
    };

    double d = 1;
    for (int i = n; i > 0; --i)
    {
      d = x + A[i] / d;
    }
    d = A[0] / d;
    return d + P;
  }
};
