package org.uma.jmetal.runner.singleobjective;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.singleobjective.SA.SimulatedAnnealingAlgorithm;
import org.uma.jmetal.algorithm.singleobjective.SA.SimulatedAnnealingBuilder;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a simulated annealing algorithm. The target
 * problem is TSP.
 *
 * @author Taras Iks <ikstaras@gmail>
 */
public class SimulatedAnnealingRunner {
	/**
	 * Usage: java
	 * org.uma.jmetal.runner.singleobjective.BinaryGenerationalGeneticAlgorithmRunne
	 */
	public static void main(String[] args) throws Exception {
		int temperature = 1000;
		double mutationProbability = 1.0;
		TSP problem;
		SimulatedAnnealingAlgorithm<PermutationSolution<Integer>> algorithm;
		PermutationSwapMutation<Integer> mutation;

		problem =  new TSP("/tspInstances/kroA100.tsp"); //new TSP("/tspInstances/myKro11.tsp"); /*  */

		System.out.println("Number of Variables: " + problem.getNumberOfVariables());// Taras
		mutation = new PermutationSwapMutation<Integer>(mutationProbability);

		algorithm = new SimulatedAnnealingBuilder<PermutationSolution<Integer>>(problem, temperature, mutation).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		PermutationSolution<Integer> solution = algorithm.getResult();// List<DefaultIntegerPermutationSolution>
																		// solution = algorithm.getResult() ;

		long computingTime = algorithmRunner.getComputingTime();

		System.out.println("Solution:");
		System.out.println(solution);

		List<PermutationSolution<Integer>> population = new ArrayList<>(1);
		population.add(solution);

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
