/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.llnl.ensdf.decay;

import gov.llnl.ensdf.EnsdfAuger;
import gov.llnl.ensdf.EnsdfComment;
import gov.llnl.ensdf.EnsdfDataSet;
import gov.llnl.ensdf.EnsdfGamma;
import gov.llnl.ensdf.EnsdfLevel;
import gov.llnl.ensdf.EnsdfQuantity;
import gov.llnl.ensdf.EnsdfXray;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nelson85
 */
public class LNHBDecayLibrary
{
    static void parseEnsdfTComment(EnsdfDataSet dataSet)
  {
    double KCtot = 0;
    double LCtot = 0;
    double MCtot = 0;
    double NCtot = 0;
    double OCtot = 0;
    for (EnsdfLevel level : dataSet.levels) {
      if (!level.gamma.isEmpty()) {
        for (EnsdfGamma gamma : level.gamma) {
          EnsdfQuantity KCpartial = (EnsdfQuantity) (gamma.continuation.get("KC"));
          if (KCpartial != null) {
          KCtot += KCpartial.toDouble();
          }
          EnsdfQuantity LCpartial = (EnsdfQuantity) (gamma.continuation.get("LC"));
          if (LCpartial != null) {
          LCtot += LCpartial.toDouble();
          }
          EnsdfQuantity MCpartial = (EnsdfQuantity) (gamma.continuation.get("MC"));
          if (MCpartial != null) {
          MCtot += MCpartial.toDouble();
          }
          EnsdfQuantity NCpartial = (EnsdfQuantity) (gamma.continuation.get("NC"));
          if (NCpartial != null) {
          NCtot += NCpartial.toDouble();
          }
          EnsdfQuantity OCpartial = (EnsdfQuantity) (gamma.continuation.get("OC"));
          if (OCpartial != null) {
          OCtot += OCpartial.toDouble();
          }
        }
      }
    }
    List<Double> Ctot = new ArrayList<>();
    Ctot.add(KCtot);
    Ctot.add(LCtot);
    Ctot.add(MCtot);
    Ctot.add(NCtot);
    Ctot.add(OCtot);
    
    for (EnsdfComment comment : dataSet.identification.comments) {
      String line = comment.contents;
      if (line.charAt(41) == 'X') {
        parseEnsdfXray(dataSet, line, Ctot);
      } else if (line.substring(45, 50).equals("AUGER") || line.substring(43, 48).equals("AUGER")) {
        parseEnsdfAuger(dataSet, line, Ctot);
      }
    }
  }

  static void parseEnsdfXray(EnsdfDataSet dataSet, String line, List<Double> Ctot)
  {
    for (EnsdfLevel level : dataSet.levels) {
      if (!level.gamma.isEmpty()) {
        for (EnsdfGamma gamma : level.gamma) { 
          EnsdfQuantity E = new EnsdfQuantity(line.substring(7, 20), null);
          EnsdfQuantity RI = new EnsdfQuantity(line.substring(26, 33), line.substring(34, 36));
          String M = line.substring(41, 51).strip() + " / " + gamma.M;
          if(RI.field.isEmpty() != true) {
            switch (M.charAt(1)) {
                case 'K':
                  EnsdfQuantity KC = (EnsdfQuantity) (gamma.continuation.get("KC"));
                  RI.field = String.valueOf( RI.toDouble() * KC.toDouble() / Ctot.get(0) );
                  RI.unc = null;
                  break;
                case 'L':
                  EnsdfQuantity LC = (EnsdfQuantity) (gamma.continuation.get("LC"));
                  RI.field = String.valueOf( RI.toDouble() * LC.toDouble() / Ctot.get(1) );
                  RI.unc = null;
                  break;
                case 'M':
                  EnsdfQuantity MC = (EnsdfQuantity) (gamma.continuation.get("MC"));
                  RI.field = String.valueOf( RI.toDouble() * MC.toDouble() / Ctot.get(2) );
                  RI.unc = null;
                  break;
                case 'N':
                  EnsdfQuantity NC = (EnsdfQuantity) (gamma.continuation.get("NC"));
                  RI.field = String.valueOf( RI.toDouble() * NC.toDouble() / Ctot.get(3) );
                  RI.unc = null;
                  break;
                case 'O':
                  EnsdfQuantity OC = (EnsdfQuantity) (gamma.continuation.get("OC"));
                  RI.field = String.valueOf( RI.toDouble() * OC.toDouble() / Ctot.get(4) );
                  RI.unc = null;
                  break;
          }
          EnsdfXray xray = new EnsdfXray(dataSet, E, RI, M);
          level.xray.add(xray);            
          }
        }
      }
    }
  }

  static void parseEnsdfAuger(EnsdfDataSet dataSet, String line, List<Double> Ctot)
  {
      for (EnsdfLevel level : dataSet.levels) {
      if (!level.gamma.isEmpty()) {
        for (EnsdfGamma gamma : level.gamma) { 
          EnsdfQuantity E = new EnsdfQuantity(line.substring(7, 20), null);
          EnsdfQuantity RI = new EnsdfQuantity(line.substring(26, 33), line.substring(34, 36));
          String M = line.substring(41, 51).strip() + " / " + gamma.M;
          if(RI.field.isEmpty() != true) {
            switch (M.charAt(0)) {
                case 'K':
                  EnsdfQuantity KC = (EnsdfQuantity) (gamma.continuation.get("KC"));
                  RI.field = String.valueOf( RI.toDouble() * KC.toDouble() / Ctot.get(0) );
                  RI.unc = null;
                  break;
                case 'L':
                  EnsdfQuantity LC = (EnsdfQuantity) (gamma.continuation.get("LC"));
                  RI.field = String.valueOf( RI.toDouble() * LC.toDouble() / Ctot.get(1) );
                  RI.unc = null;
                  break;
                case 'M':
                  EnsdfQuantity MC = (EnsdfQuantity) (gamma.continuation.get("MC"));
                  RI.field = String.valueOf( RI.toDouble() * MC.toDouble() / Ctot.get(2) );
                  RI.unc = null;
                  break;
                case 'N':
                  EnsdfQuantity NC = (EnsdfQuantity) (gamma.continuation.get("NC"));
                  RI.field = String.valueOf( RI.toDouble() * NC.toDouble() / Ctot.get(3) );
                  RI.unc = null;
                  break;
                case 'O':
                  EnsdfQuantity OC = (EnsdfQuantity) (gamma.continuation.get("OC"));
                  RI.field = String.valueOf( RI.toDouble() * OC.toDouble() / Ctot.get(4) );
                  RI.unc = null;
                  break;
          }
          EnsdfAuger auger = new EnsdfAuger(dataSet, E, RI, M);
          level.auger.add(auger);            
          }
        }
      }
    }
  }
}
