package gov.llnl.rtk.physics;

import java.util.HashMap;
import java.util.List;
import gov.llnl.rtk.physics.SourceImpl;
/**
 *
 * @author nelson85
 */
public class EmissionCalculator
{
  DecayLibrary decayLibrary;

  /**
   * Set the physics library to use for this process.
   *
   * @param library
   */
  public void setDecayLibrary(DecayLibrary library)
  {
    this.decayLibrary = library;
  }
  
  public DecayLibrary getDecayLibrary() {
    return this.decayLibrary;
  }
  /**
   *
   * @param sl
   * @return
   */
  public Emissions calculate(List<Source> sl)
  {
    EmissionsImpl out = new EmissionsImpl();
    for (Source entry : sl)
    {
      List<DecayTransition> transitions = decayLibrary.getTransitionsFrom(entry.getNuclide());
      for (DecayTransition t : transitions)
      {
        add(out, t, t.getBranchingRatio() * entry.getActivity()); //Bq -> particles
      }
    }
    
    // FIXME do we need to convert electron capture to xrays?
    
    return out;

  }
  
  static void add(EmissionsImpl out, Emissions el, double f)
  {
    // FIXME as the coincidence is tied to the original we will need to clone 
    // all of the items with the scaling factor, then connect new coincidence records
    HashMap<Emission, Emission> map = new HashMap<>();
    for (Emission e : el.getEmissions())
    {
      Emission e2 = EmissionScaled.scaled(e, f);
      out.emissions.add(e2);
      map.put(e, e2);
    }

    for (EmissionCorrelation c : el.getCorrelations())
    {
      out.coincidence.add(new EmissionCorrelationImpl(
              map.get(c.getPrimary()),
              map.get(c.getSecondary()),
              c.getProbability()));
    }

    // now link coincident data
  }

}
