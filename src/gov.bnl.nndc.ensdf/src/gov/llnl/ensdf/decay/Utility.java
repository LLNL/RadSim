/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.llnl.ensdf.decay;

import gov.llnl.ensdf.EnsdfAlpha;
import gov.llnl.ensdf.EnsdfBeta;
import gov.llnl.ensdf.EnsdfDataSet;
import gov.llnl.ensdf.EnsdfElectronCapture;
import gov.llnl.ensdf.EnsdfGamma;
import gov.llnl.ensdf.EnsdfLevel;
import gov.llnl.ensdf.EnsdfNormalization;
import gov.llnl.ensdf.EnsdfParticle;
import gov.llnl.rtk.physics.DecayTransition;
import gov.llnl.rtk.physics.Nuclide;
import java.util.List;

/**
 *
 * @author nelson85
 */
public class Utility
{

// !!!This class may no longer be needed as it is replaced by the SplitIsomers class!!!
//  public static EnsdfDecayTransition convert(EnsdfDataSet ds)
//  {
//    // Skip records with unknown products
//    if (ds.identification.product == null)
//      return null;
//
//    if (ds.parents.isEmpty())
//      return null;
//
//    if (ds.parents.size() > 1)
//      return null;
//
//    if (ds.parents.get(0).nuclide == null)
//      return null;
//
//    if (ds.normalizations.isEmpty())
//      return null;
//
//    Nuclide parent = ds.parents.get(0).nuclide;
//    Nuclide child = ds.identification.product;
//
//    System.out.println("Convert " + ds.identification.DSID + " " + parent + "->" + child);
//
//    // Note we need to split the record if there are metastable daughters.   THis means
//    // Tracing out each path that leads to the metastable.   For now will will stupidly
//    // assume we can do one to one record conversion.  This will work on many nuclides.
//    for (EnsdfLevel level : ds.levels)
//    {
//      if (!level.MS.isBlank())
//      {
//        System.out.println("Need to split transition.");
//      }
//    }
//
//    // Verify that the levels are defined for all betas.
//    for (EnsdfBeta beta : ds.collectBetas())
//    {
//      if (beta.E.field.isBlank() && beta.level.E.field.contains("+"))
//        return null;
//    }
//
//    EnsdfNormalization normalization = ds.normalizations.get(0);
//    if (normalization.NR.field.isBlank())
//      return null;
//    if (normalization.NB.field.isBlank())
//      return null;
//
//    EnsdfDecayTransition out = new EnsdfDecayTransition(ds);
//    double nr = normalization.NR.toDouble() / 100; // used to scale RI in Gamma
//    double nb = normalization.NB.toDouble() / 100; // used to scale IB, IE, and TI
//
//    // Compute xray vacancies
//    double vacanciesK = 0;
//    double vacanciesL = 0;
//
//    // Convert the gammas
//    for (EnsdfGamma gamma : ds.collectGammas())
//    {
//      // Skip lines with no relative intensity
//      if (gamma.RI.field.isBlank())
//        continue;
//      if (gamma.E.field.isBlank())
//        continue;
//      out.emissions.add(new GammaImpl(out, gamma, nr));
//
//      // Internal conversion produces vacancies
//      if (gamma.continuation.containsKey("KC"))
//        vacanciesK += gamma.RI.toDouble() * nr * gamma.continuation.get("KC").toDouble();
//      if (gamma.continuation.containsKey("LC"))
//        vacanciesL += gamma.RI.toDouble() * nr * gamma.continuation.get("LC").toDouble();
//    }
//
//    // Convert the betas
//    for (EnsdfAlpha alpha : ds.collectAlphas())
//    {
//      // Skip lines with no relative intensity
//      if (alpha.IA.field.isBlank())
//        continue;
//      out.emissions.add(new AlphaImpl(out, alpha, 1.0));
//    }
//
//    // Convert the betas
//    for (EnsdfBeta beta : ds.collectBetas())
//    {
//      // Skip lines with no relative intensity
//      if (beta.IB.field.isBlank())
//        continue;
//      out.emissions.add(new EnsdfDecayTransition.BetaImpl(out, beta, nb));
//    }
//
//    // Convert the electron captures
//    for (EnsdfElectronCapture ec : ds.collectCaptures())
//    {
//      // Skip lines with no relative intensity
//      if (!ec.IB.field.isBlank())
//        out.emissions.add(new EnsdfDecayTransition.PositronImpl(out, ec, nb));
//
//      if (ec.IE.field.isBlank())
//        continue;
//
//      double ti = 0;
//
//      if (ec.TI.isSpecified())
//      {
//        ti = ec.TI.toDouble();
//      } else if (ec.IB.isSpecified() && ec.IE.isSpecified())
//      {
//        ti = ec.IB.toDouble() + ec.IE.toDouble();
//      } else
//      {
//      }
//      // Trace the number of vacancies
//      if (ec.continuation.containsKey("CK"))
//        vacanciesK += ti * nr * ec.continuation.get("CK").toDouble();
//      if (ec.continuation.containsKey("CL"))
//        vacanciesL += ti * nr * ec.continuation.get("CL").toDouble();
//    }
//
//    for (EnsdfParticle ec : ds.collectParticles())
//    {
//      if (ec.IP.field.isBlank())
//        continue;
//
//      // FIXME deal with 
//    }
//
//    out.vacanciesK = vacanciesK;
//    out.vacanciesL = vacanciesL;
//
//    return out;
//
//  }
//
//  public static List<DecayTransition> splitMeta(EnsdfDataSet ds)
//  {
//    return null;
//  }
}
