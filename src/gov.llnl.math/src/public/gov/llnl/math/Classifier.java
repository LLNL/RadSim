/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import java.io.Serializable;
import java.util.Collection;

/**
 * Generic interface for all classification algorithms. The job of a classifier
 * is to assign one or more labels to a sample. Classifiers usually produce a
 * list of classifications such that the probabilities add up to one. It is
 * assumed that each of the labels are mutually exclusive.
 * <p>
 * Alternative behaviors should be documented by the specific implementation.
 *
 * @author nelson85
 * @param <Input>
 */
public interface Classifier<Input> extends Serializable
{
  /**
   * Generic interface for labels created by the classifier. The classifier
   * implementation should extend this interface to produce it specific label
   * with any additional information associated with that label.
   */
  interface Classification extends Serializable
  {
    /**
     * Get the label associated with the classification.
     *
     * @return
     */
    String getLabel();

    /**
     * Get the probability that the label is associated with the input.
     *
     * @return
     */
    double getProbability();

    /**
     * Get the log likelihood for this sample.
     *
     * @return
     */
    default double getLikelihood()
    {
      return Math.log(getProbability());
    }
  }

  interface ClassificationSet<Type extends Classification>
          extends Collection<Type>, Serializable
  {

  }

  /**
   * Each classification should have an associated rule. This class if primarily
   * a place holder to enforce a naming convention.
   */
  interface Rule extends Serializable
  {
  }

  /**
   * Analyze an input and assign it a set of labels.
   *
   * @param input is the sample to classify.
   * @return a list of classifications that apply.
   */
  ClassificationSet classify(Input input);
}
