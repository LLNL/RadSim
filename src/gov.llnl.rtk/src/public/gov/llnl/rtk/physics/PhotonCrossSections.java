/* 
 * Copyright 2025, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import java.util.function.DoubleUnaryOperator;

/**
 * Interface for photon cross sections in materials.
 *
 * Currently we only support NIST XCOM cross section database.
 * If units are not specified then the defaults will be library specific.
 *
 * @author nelson85
 */
public interface PhotonCrossSections
{

  /**
   * Get the material for this cross section.
   *
   * @return the material
   */
  Material getMaterial();

  /**
   * Get the current input units for the evaluation.
   *
   * @return the inputUnits of energy.
   */
  Units getInputUnits();

  /**
   * Get the current units for the evaluation.
   *
   * @return the outputUnits of cross section.
   */
  Units getOutputUnits();

  /**
   * Select the default input units for evaluators and functions.
   *
   * @param units for input in energy.
   */
  void setInputUnits(Units units);

  /**
   * Select the default output units for evaluators and functions.
   *
   * @param units for output in cross section.
   */
  void setOutputUnits(Units units);

  /**
   * Get an evaluator for cross section by energy.
   *
   * @return a new evaluator.
   */
  PhotonCrossSectionsEvaluator newEvaluator();

  /**
   * Get the incoherent scattering as a function.
   *
   * Use {@link #setInputUnits(Units) setInputUnits} and
   * {@link #setOutputUnits(Units) setOutputUnits} prior to this call to select
   * the units for the function.
   *
   * @return function in units specified by user.
   */
  default DoubleUnaryOperator getIncoherent()
  {
    PhotonCrossSectionsEvaluator eval = newEvaluator();
    return (double p) ->
    {
      eval.seek(p);
      return eval.getIncoherent();
    };
  }

  /**
   * Get the pair cross section.
   *
   * Use {@link #setInputUnits(Units) setInputUnits} and
   * {@link #setOutputUnits(Units) setOutputUnits} prior to this call to select
   * the units for the function.
   *
   * This includes both electron and atomic.
   *
   * @return function in units specified by user.
   */
  default DoubleUnaryOperator getPair()
  {
    PhotonCrossSectionsEvaluator eval = newEvaluator();
    return (double p) ->
    {
      eval.seek(p);
      return eval.getPair();
    };
  }

  /**
   * Get the photo electric section.
   *
   * Use {@link #setInputUnits(Units) setInputUnits} and
   * {@link #setOutputUnits(Units) setOutputUnits} prior to this call to select
   * the units for the function.
   *
   * @return function in units specified by user.
   */
  default DoubleUnaryOperator getPhotoelectric()
  {

    PhotonCrossSectionsEvaluator eval = newEvaluator();
    return (double p) ->
    {
      eval.seek(p);
      return eval.getPhotoelectric();
    };
  }

  /**
   * Get the total cross section excluding coherent.
   *
   * Use {@link #setInputUnits(Units) setInputUnits} and
   * {@link #setOutputUnits(Units) setOutputUnits} prior to this call to select
   * the units for the function.
   *
   * @return function in units specified by user.
   */
  default DoubleUnaryOperator getTotal()
  {
    PhotonCrossSectionsEvaluator eval = newEvaluator();
    return (double p) ->
    {
      eval.seek(p);
      return eval.getTotal();
    };
  }

}
