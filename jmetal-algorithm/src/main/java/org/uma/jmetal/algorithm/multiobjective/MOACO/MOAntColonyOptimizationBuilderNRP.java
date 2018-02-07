package org.uma.jmetal.algorithm.multiobjective.MOACO;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 * Created by Taras Iks.
 */
public class MOAntColonyOptimizationBuilderNRP<S extends Solution<?>> implements AlgorithmBuilder<MOAntColonyOptimizationAlgorithmNRP<S>> {
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
  public MOAntColonyOptimizationBuilderNRP(Problem<S> problem, int numberOfAnts, double alpha, double beta, double rho, double q) {
    this.problem = problem;
    this.numberOfAnts = numberOfAnts;
    evaluator = new SequentialSolutionListEvaluator<Integer>();
    this.alpha = alpha;
    this.beta = beta;
    this.rho = rho;
    this.q = q;
  }

  public MOAntColonyOptimizationBuilderNRP<S> setSolutionListEvaluator(SolutionListEvaluator<Integer> evaluator) {
    this.evaluator = evaluator;

    return this;			
  }

  public MOAntColonyOptimizationAlgorithmNRP<S> build() {
	  return new MOAntColonyOptimizationAlgorithmNRP<S>(problem, numberOfAnts, alpha, beta, rho, q);
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
