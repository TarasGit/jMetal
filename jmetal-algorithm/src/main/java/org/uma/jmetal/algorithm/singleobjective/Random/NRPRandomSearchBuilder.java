package org.uma.jmetal.algorithm.singleobjective.Random;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;

/**
 * This class implements a simple random search algorithm.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class NRPRandomSearchBuilder<S extends Solution<?>> implements AlgorithmBuilder<NRPRandomSearch<S>> {
  private Problem<S> problem ;
  private int maxEvaluations ;

  /* Getter */
  public int getMaxEvaluations() {
    return maxEvaluations;
  }


  public NRPRandomSearchBuilder(Problem<S> problem) {
    this.problem = problem ;
    maxEvaluations = 25000 ;
  }

  public NRPRandomSearchBuilder<S> setMaxEvaluations(int maxEvaluations) {
    this.maxEvaluations = maxEvaluations ;

    return this ;
  }

  public NRPRandomSearch<S> build() {
    return new NRPRandomSearch<S>(problem, maxEvaluations) ;
  }
} 
