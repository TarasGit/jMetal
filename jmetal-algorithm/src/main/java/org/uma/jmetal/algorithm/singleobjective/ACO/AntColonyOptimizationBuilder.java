package org.uma.jmetal.algorithm.singleobjective.ACO;

import org.uma.jmetal.algorithm.singleobjective.SA.SimulatedAnnealingAlgorithm;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.impl.DefaultIntegerPermutationSolution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 * Created by ajnebro on 10/12/14.
 */
public class AntColonyOptimizationBuilder{//<S extends Solution<?>> {
  //public enum GeneticAlgorithmVariant {GENERATIONAL, STEADY_STATE}
  /**
   * Builder class
   */
  private TSP problem;
  //private int populationSize;
  //private CrossoverOperator<S> crossoverOperator;
  //private PermutationSwapMutation<Integer> mutationOperator;
  //private SelectionOperator<List<S>, S> selectionOperator;
  private SolutionListEvaluator<Integer> evaluator;
  //private int temperature;

  //private GeneticAlgorithmVariant variant ;
  //private SelectionOperator<List<S>, S> defaultSelectionOperator = new BinaryTournamentSelection<S>() ;

  /**
   * Builder constructor
   */
  public AntColonyOptimizationBuilder(TSP problem) {
    this.problem = problem;
    evaluator = new SequentialSolutionListEvaluator<Integer>();//TODO XXX remove it.

   // this.variant = GeneticAlgorithmVariant.GENERATIONAL ;
  }




//  public SimulatedAnnealingBuilder<S> setSelectionOperator(SelectionOperator<List<S>, S> selectionOperator) {
//    this.selectionOperator = selectionOperator;
//
//    return this;
//  }

  public AntColonyOptimizationBuilder setSolutionListEvaluator(SolutionListEvaluator<Integer> evaluator) {
    this.evaluator = evaluator;

    return this;
  }

//  public SimulatedAnnealingBuilder<S> setVariant(GeneticAlgorithmVariant variant) {
//    this.variant = variant;
//
//    return this;
//  }

  public AntColonyOptimizationAlgorithm<DefaultIntegerPermutationSolution> build() {
//    if (variant == GeneticAlgorithmVariant.GENERATIONAL) {
//      return new GenerationalGeneticAlgorithm<S>(problem, maxEvaluations, populationSize,
//          crossoverOperator, mutationOperator, selectionOperator, evaluator);
//    } else if (variant == GeneticAlgorithmVariant.STEADY_STATE) {
//      return new SteadyStateGeneticAlgorithm<S>(problem, maxEvaluations, populationSize,
//          crossoverOperator, mutationOperator, selectionOperator);
//    } else {
//      throw new JMetalException("Unknown variant: " + variant) ;
//    }
	  return new AntColonyOptimizationAlgorithm<DefaultIntegerPermutationSolution>(problem);
  }

  /*
   * Getters
   */
  public TSP getProblem() {
    return problem;
  }


//
//  public CrossoverOperator<S> getCrossoverOperator() {
//    return crossoverOperator;
//  }
////
//  public PermutationSwapMutation<Integer> getMutationOperator() {
//    return mutationOperator;
//  }
//
//  public SelectionOperator<List<S>, S> getSelectionOperator() {
//    return selectionOperator;
//  }

  public SolutionListEvaluator<Integer> getEvaluator() {
    return evaluator;
  }

//  public GeneticAlgorithmVariant getVariant() {
//    return variant ;
//  }
}
