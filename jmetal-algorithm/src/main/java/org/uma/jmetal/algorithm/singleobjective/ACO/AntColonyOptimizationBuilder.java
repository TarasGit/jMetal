package org.uma.jmetal.algorithm.singleobjective.ACO;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 * Created by ajnebro on 10/12/14.
 */
public class AntColonyOptimizationBuilder<S extends Solution<?>> {
  /**
   * Builder class
   */
  private Problem<S> problem;
  private SolutionListEvaluator<Integer> evaluator;
  /**
   * Builder constructor
   */
  public AntColonyOptimizationBuilder(Problem<S> problem) {
    this.problem = problem;
    evaluator = new SequentialSolutionListEvaluator<Integer>();//TODO XXX remove it.

  }



  public AntColonyOptimizationBuilder<S> setSolutionListEvaluator(SolutionListEvaluator<Integer> evaluator) {
    this.evaluator = evaluator;

    return this;
  }

  public AntColonyOptimizationAlgorithm<S> build() {
	  return new AntColonyOptimizationAlgorithm<S>(problem);
  }

  /*
   * Getters
   */
  public Problem<S> getProblem() {
    return problem;
  }
  public SolutionListEvaluator<Integer> getEvaluator() {
    return evaluator;
  }
}
