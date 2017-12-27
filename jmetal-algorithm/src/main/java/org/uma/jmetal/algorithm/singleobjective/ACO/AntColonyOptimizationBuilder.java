package org.uma.jmetal.algorithm.singleobjective.ACO;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 * Created by Taras Iks.
 */
public class AntColonyOptimizationBuilder<S extends Solution<?>> {
  /**
   * Builder class
   */
  private Problem<S> problem;
  private SolutionListEvaluator<Integer> evaluator;
  private int numberOfAnts;
  private double alpha;
  private double beta;
  private double rho;
  private double q;
  /**
   * Builder constructor
   */
  public AntColonyOptimizationBuilder(Problem<S> problem, int numberOfAnts, double alpha, double beta, double rho, double q) {
    this.problem = problem;
    this.numberOfAnts = numberOfAnts;
    evaluator = new SequentialSolutionListEvaluator<Integer>();//TODO XXX remove it.
    this.alpha = alpha;
    this.beta = beta;
    this.rho = rho;
    this.q = q;
  }

  public AntColonyOptimizationBuilder<S> setSolutionListEvaluator(SolutionListEvaluator<Integer> evaluator) {
    this.evaluator = evaluator;

    return this;
  }

  public AntColonyOptimizationAlgorithm<S> build() {
	  return new AntColonyOptimizationAlgorithm<S>(problem, numberOfAnts, alpha, beta, rho, q);
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
