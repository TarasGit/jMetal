package org.uma.jmetal.runner.singleobjective;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.singleobjective.ACO.AntColonyOptimizationAlgorithm;
import org.uma.jmetal.algorithm.singleobjective.ACO.AntColonyOptimizationBuilder;
import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a Ant Colony Optimization algorithm. The target
 * problem is TSP.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class AntColonyOptimizationRunner {
	/**
	 * Usage: java
	 * org.uma.jmetal.runner.singleobjective.BinaryGenerationalGeneticAlgorithmRunner
	 */
	public static void main(String[] args) throws Exception {
		TSP problem;
		AntColonyOptimizationAlgorithm<PermutationSolution<Integer>> algorithm;

		problem = new TSP("/tspInstances/kroA100.tsp");//  new TSP("/tspInstances/myKro11.tsp");*/

		System.out.println("Number of Variables: " + problem.getNumberOfVariables());// Taras

		algorithm = new AntColonyOptimizationBuilder<PermutationSolution<Integer>>(problem).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		PermutationSolution<Integer> solution = algorithm.getResult();

		List<PermutationSolution<Integer>> population = new ArrayList<>(1);
		population.add(solution);

		System.out.println("End Solution: " + solution);

		long computingTime = algorithmRunner.getComputingTime();
		
		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
