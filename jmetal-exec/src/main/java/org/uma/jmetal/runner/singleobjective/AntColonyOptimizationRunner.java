package org.uma.jmetal.runner.singleobjective;

import java.util.List;

import org.uma.jmetal.algorithm.singleobjective.ACO.AntColonyOptimizationAlgorithm;
import org.uma.jmetal.algorithm.singleobjective.ACO.AntColonyOptimizationBuilder;
import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.impl.DefaultIntegerPermutationSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;

/**
 * Class to configure and run a generational genetic algorithm. The target problem is TSP.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class AntColonyOptimizationRunner {
  /**
   * Usage: java org.uma.jmetal.runner.singleobjective.BinaryGenerationalGeneticAlgorithmRunner
   */
  public static void main(String[] args) throws Exception {
	///int temperature = 10000000;
    TSP problem;
    AntColonyOptimizationAlgorithm<DefaultIntegerPermutationSolution> algorithm;
    //PermutationSwapMutation<Integer> mutation;
    //SelectionOperator<List<PermutationSolution<Integer>>, PermutationSolution<Integer>> selection;

    problem = new TSP("/tspInstances/myKro11.tsp");//*new TSP("/tspInstances/kroA100.tsp");*/

//    crossover = new PMXCrossover(0.9) ;

    //double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
    
    System.out.println("Number of Variables: "  + problem.getNumberOfVariables());//Taras
    //mutation = new PermutationSwapMutation<Integer>(1) ;

    algorithm = new AntColonyOptimizationBuilder(problem).build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute() ;

	List<DefaultIntegerPermutationSolution> solution = algorithm.getResult() ;

    System.out.println("Solution:"  + solution.get(0) );
    
    long computingTime = algorithmRunner.getComputingTime() ;

//    new SolutionListOutput(population)
//            .setSeparator("\t")
//            .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
//            .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
//            .print();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

  }
}
