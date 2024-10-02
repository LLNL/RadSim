/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.llnl.ensdf.decay;

import gov.llnl.rtk.physics.Alpha;
import gov.llnl.rtk.physics.Beta;
import gov.llnl.rtk.physics.DecayTransition;
import gov.llnl.rtk.physics.ElectronCapture;
import gov.llnl.rtk.physics.Emission;
import gov.llnl.rtk.physics.EmissionCorrelation;
import gov.llnl.rtk.physics.Gamma;
import gov.llnl.rtk.physics.Nuclide;
import gov.llnl.rtk.physics.Positron;
import gov.llnl.rtk.physics.Xray;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author nelson85
 */
public class SandiaFormatter
{

  private StringBuilder sb;
  private String prefix;
  private HashMap<Emission, String> labels = new HashMap<>();
  int pid, aid, bid, eid, gid;
  private Map<Emission, List<EmissionCorrelation>> correlations;

  public String format(DecayTransition transition)
  {
    Nuclide parent = transition.getParent();
    Nuclide child = transition.getChild();
    if (parent == null)
      throw new NullPointerException("Null parent");
    if (child == null)
      throw new NullPointerException("Null child");
    labels.clear();
    prefix = String.format("N%s-N%s",
            parent.getZaid(),
            child.getZaid());
    pid = 0;
    gid = 0;
    aid = 0;
    bid = 0;
    gid = 0;
    this.correlations = transition.getCorrelations().stream()
            .collect(Collectors.groupingBy(EmissionCorrelation::getPrimary));

    boolean alpha = transition.getEmissions().stream().anyMatch(p -> p instanceof Alpha);
    boolean bm = transition.getEmissions().stream().anyMatch(p -> p instanceof Beta);
    boolean ec = transition.getEmissions().stream().anyMatch(p -> p instanceof ElectronCapture);
    boolean bp = transition.getEmissions().stream().anyMatch(p -> p instanceof Positron);
    boolean it = false;
    if (transition.getChild() != null)
      it = transition.getParent().getAtomicNumber() == transition.getChild().getAtomicNumber();

    String mode = "";
    if (it)
      mode = "IT";
    else if (ec)
      mode = "ec";
    else if (bp)
      mode = "b+";
    else if (bm)
      mode = "b-";
    else if (alpha)
      mode = "a";
    // FIXME we do not deal with b-n, b+n etc currently.

    this.sb = new StringBuilder();
    sb.append("<transition branchingRatio=\"").append(transition.getBranchingRatio()).append("\"");
    if (transition.getChild() != null)
      sb.append(" child=\"").append(transition.getChild().toString()).append("\"");
    sb.append(" mode=\"").append(mode).append("\"");
    sb.append(" parent=\"").append(transition.getParent().toString()).append("\">\n");

    List<Emission> emissions = transition.getEmissions();
    emissions.stream().filter(p -> p instanceof Gamma)
            .map(p -> (Gamma) p)
            .sorted((p1, p2) -> Double.compare(p1.getEnergy().getValue(), p2.getEnergy().getValue()))
            .forEach(this::labelGamma);

    emissions.stream().filter(p -> p instanceof Gamma)
            .map(p -> (Gamma) p)
            .sorted((p1, p2) -> Double.compare(p1.getEnergy().getValue(), p2.getEnergy().getValue()))
            .forEach(this::formatGamma);
    emissions.stream().filter(p -> p instanceof Alpha)
            .map(p -> (Alpha) p)
            .sorted((p1, p2) -> Double.compare(p1.getEnergy().getValue(), p2.getEnergy().getValue()))
            .forEach(this::formatAlpha);
    emissions.stream().filter(p -> p instanceof Beta)
            .map(p -> (Beta) p)
            .sorted((p1, p2) -> Double.compare(p1.getEnergy().getValue(), p2.getEnergy().getValue()))
            .forEach(this::formatBeta);
    emissions.stream().filter(p -> p instanceof Positron)
            .map(p -> (Positron) p)
            .sorted((p1, p2) -> Double.compare(p1.getEnergy().getValue(), p2.getEnergy().getValue()))
            .forEach(this::formatPositron);
    emissions.stream().filter(p -> p instanceof ElectronCapture)
            .map(p -> (ElectronCapture) p)
            .sorted((p1, p2) -> Double.compare(p1.getIntensity().getValue(), p2.getIntensity().getValue()))
            .forEach(this::formatElectronCapture);
    emissions.stream().filter(p -> p instanceof Xray)
            .map(p -> (Xray) p)
            .sorted((p1, p2) -> Double.compare(p1.getEnergy().getValue(), p2.getEnergy().getValue()))
            .forEach(this::formatXray);

    sb.append("</transition>\n");
    return sb.toString();
  }

  private void labelGamma(Gamma t)
  {
    this.gid++;
    String label = String.format("%s-G%04d", prefix, this.gid);
    labels.put(t, label);
  }

  private void formatGamma(Gamma t)
  {
    sb.append("  <gamma energy=\"").append(t.getEnergy().getValue()).append("\"");
    sb.append(" id=\"").append(labels.get(t)).append("\"");
    sb.append(" intensity=\"").append(t.getIntensity().getValue()).append("\"");
    if (correlations.containsKey(t))
    {
      sb.append(">\n");
      for (EmissionCorrelation cor : correlations.get(t))
      {
        if (!(cor.getSecondary() instanceof Gamma))
          continue;
        sb.append("    <coincidentGamma");
        sb.append(" energy=\"").append(((Gamma) (cor.getSecondary())).getEnergy().getValue()).append("\"");
        sb.append(" id=\"").append(labels.get(cor.getSecondary())).append("\"");
        sb.append(" intensity=\"").append(cor.getProbability()).append("\"");
        sb.append(">\n");
      }
      sb.append("  </gamma>\n");
    } else
      sb.append("/>\n");
  }

  private void formatAlpha(Alpha t)
  {
    sb.append("  <alpha energy=\"").append(t.getEnergy().getValue()).append("\"");
    sb.append(" hinderance=\"").append(t.getHindrance().getValue()).append("\"");
    sb.append(" intensity=\"").append(t.getIntensity().getValue()).append("\"");
    sb.append("/>\n");
  }

  private void formatBeta(Beta t)
  {
    sb.append("  <beta energy=\"").append(t.getEnergy().getValue()).append("\"");
    if (!t.getForbiddenness().isBlank())
      sb.append(" forbiddenness=\"").append(t.getForbiddenness().toLowerCase()).append("\"");
    sb.append(" intensity=\"").append(t.getIntensity().getValue()).append("\"");
    if (t.getLogFT().isSpecified())
      sb.append(" logFT=\"").append(t.getLogFT().getValue()).append("\"");
    sb.append(">\n");
  }

  private void formatElectronCapture(ElectronCapture t)
  {
    sb.append("  <electronCapture intensity=\"").append(t.getIntensity().getValue()).append("\"");
    if (!t.getForbiddenness().isBlank())
      sb.append(" forbiddenness=\"").append(t.getForbiddenness().toLowerCase()).append("\"");
    if (t.getLogFT().isSpecified())
      sb.append(" logFT=\"").append(t.getLogFT().getValue()).append("\"");
    sb.append(">\n");
  }

  private void formatPositron(Positron t)
  {
    sb.append("  <positron energy=\"").append(t.getEnergy().getValue()).append("\"");
    if (!t.getForbiddenness().isBlank())
      sb.append(" forbiddenness=\"").append(t.getForbiddenness().toLowerCase()).append("\"");
    sb.append(" intensity=\"").append(t.getIntensity().getValue()).append("\"");
    if (t.getLogFT().isSpecified())
      sb.append(" logFT=\"").append(t.getLogFT().getValue()).append("\"");
    sb.append("/>\n");
  }

  private void formatXray(Xray t)
  {
    sb.append("  <xray energy=\"").append(t.getEnergy().getValue()).append("\"");
    sb.append(" intensity=\"").append(t.getIntensity().getValue()).append("\"");
    sb.append(" assignment=\"").append(t.getName()).append("\"");
    sb.append("/>\n");
  }

}
