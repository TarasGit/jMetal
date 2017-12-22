package org.uma.jmetal.runner.singleobjective;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.TS.TabuSearchBuilder;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a generational genetic algorithm. The target
 * problem is TSP.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class TabuSearchRunner {
	/**
	 * Usage: java
	 * org.uma.jmetal.runner.singleobjective.BinaryGenerationalGeneticAlgorithmRunner
	 */
	public static void main(String[] args) throws Exception {
		TSP problem;
		PermutationSwapMutation<Integer> mutation;
		Algorithm<PermutationSolution<Integer>> algorithm;
		double mutationProbability = 1.0;
		int tabuListSize = 20;
		int numbOfIterations = 200;

		problem = new TSP("/tspInstances/myKro11.tsp");// new TSP("/tspInstances/kroA100.tsp");//**/

		System.out.println("Number of Variables: " + problem.getNumberOfVariables());// Taras

		mutation = new PermutationSwapMutation<Integer>(mutationProbability);

		algorithm = new TabuSearchBuilder<PermutationSolution<Integer>>(problem, mutation, tabuListSize,
				numbOfIterations).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		PermutationSolution<Integer> solution = algorithm.getResult(); // TODO: set ACO, SA to this single result
																		// instead of list.

		List<PermutationSolution<Integer>> population = new ArrayList<>(1);
		population.add(solution);

		System.out.println("Solution:" + solution);

		long computingTime = algorithmRunner.getComputingTime();

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
