package org.uma.jmetal.runner.singleobjective.SA;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.SA.SimulatedAnnealingBuilder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.problem.Problem;
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
public class SimulatedAnnealingRunnerTSP {
	/**
	 * Usage: java
	 * org.uma.jmetal.runner.singleobjective.BinaryGenerationalGeneticAlgorithmRunne
	 */

	public static final double RATE_OF_COOLING = 0.01;
	public static final int INITIAL_TEMPERATURE = 1000;
	public static final int MINIMAL_TEMPERATURE = 0;

	public static void main(String[] args) throws Exception {
		double mutationProbability = 1.0;
		Problem<PermutationSolution<Integer>> problem;
		Algorithm<PermutationSolution<Integer>> algorithm;
		MutationOperator<PermutationSolution<Integer>> mutation;

		problem = new TSP("/tspInstances/kroA100.tsp"); // new TSP("/tspInstances/myKro11.tsp"); /* */

		System.out.println("Number of Variables: " + problem.getNumberOfVariables());// Taras
		
		mutation = new PermutationSwapMutation<Integer>(mutationProbability);

		algorithm = new SimulatedAnnealingBuilder<PermutationSolution<Integer>>(problem, mutation)
				.setMinimalTemperature(MINIMAL_TEMPERATURE).setInitialTemperature(INITIAL_TEMPERATURE)
				.setRateOfCooling(RATE_OF_COOLING).build();

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
