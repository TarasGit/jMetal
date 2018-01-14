package org.uma.jmetal.runner.multiobjective.GA;

import java.io.IOException;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.BinarySinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BinaryFlipMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.PermutationProblem;
import org.uma.jmetal.problem.singleobjective.MultiobjectiveNRPClassic;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * Class for configuring and running the NSGA-II algorithm to solve the bi-objective TSP
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */

public class NSGAIIRunnerNRPClassic extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws java.io.IOException
   * @throws SecurityException
   * @throws ClassNotFoundException
   * Invoking command:
  java org.uma.jmetal.runner.multiobjective.NSGAIITSPRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws JMetalException, IOException {
    JMetalRandom.getInstance().setSeed(100L);

    PermutationProblem<PermutationSolution<Integer>> problem;
    Algorithm<List<PermutationSolution<Integer>>> algorithm;
    CrossoverOperator<PermutationSolution<Integer>> crossover;
    MutationOperator<PermutationSolution<Integer>> mutation;
    SelectionOperator<List<PermutationSolution<Integer>>, PermutationSolution<Integer>> selection;
    
    double costFactor = 0.7;

	problem = new MultiobjectiveNRPClassic("/nrpClassicInstances/nrp1.txt", costFactor);// 500(Min costs)//new
	//problem = new MultiobjectiveNRPClassic("/nrpClassicInstances/myNRP10Customers.txt", costFactor);// 500(Min costs)//new
	
	
    //crossover = new PMXCrossover(0.9) ;
	crossover = new BinarySinglePointCrossover(0.9);// new PMXCrossover(0.9);


    double mutationProbability = 0.3;
    
    
	DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(0.9);// 0.9 for Zero.

    //mutation = new PermutationSwapMutation<Integer>(mutationProbability) ;
	mutation = new BinaryFlipMutation<PermutationSolution<Integer>>(mutationProbability);

    
    selection = new BinaryTournamentSelection<PermutationSolution<Integer>>(new RankingAndCrowdingDistanceComparator<PermutationSolution<Integer>>());
/**
 * List<Double> inters = new ArrayList<>();
 inters.add(0.0);
 inters.add(0.0);
 double epsilon =0.0001;
 algorithm = new RNSGAIIBuilder<>(problem, crossover, mutation,inters,epsilon)

 */
    algorithm = new NSGAIIBuilder<PermutationSolution<Integer>>(problem, crossover, mutation)
            .setSelectionOperator(selection)
            .setMaxEvaluations(250000)
            .setPopulationSize(1000)
            .build() ;

    System.out.println("start");
    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute() ;
    
    List<PermutationSolution<Integer>> population = algorithm.getResult() ;
    long computingTime = algorithmRunner.getComputingTime() ;


    System.out.println("end: " + population);
    
    System.out.println(population);
    
    new SolutionListOutput(population)
            .setSeparator("\t")
            .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
            .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
            .print();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed());
    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
  }
}