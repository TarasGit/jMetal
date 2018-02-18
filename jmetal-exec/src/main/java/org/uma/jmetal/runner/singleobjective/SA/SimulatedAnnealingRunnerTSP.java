package org.uma.jmetal.runner.singleobjective.SA;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.SA.SimulatedAnnealingBuilder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.SimpleMinDoubleComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a simulated annealing algorithm. The target
 * problem is TSP.
 *
 * @author Taras Iks <ikstaras@gmail>
 */
public class SimulatedAnnealingRunnerTSP {
	/*
	 * IMPORTANT: don't increase the temperature, because the formulate for SA
	 * depends on it, and for very high temperatures the most of the time you will
	 * get the acceptance probability equal to 1.
	 * 
	 * SOLUTION: change RATE OF COOLING to be smaller to get better solution quality
	 * OR multiply temperature in SA with some factor, to be able to use another
	 * temperatures.
	 */

	public static final double RATE_OF_COOLING = 0.0001;
	public static final int INITIAL_TEMPERATURE = 100;
	public static final int MINIMAL_TEMPERATURE = 1;
	public static final double K = 1;

	public static final double MUTATION_PROBABILITY = 0.5;

	public static void main(String[] args) throws Exception {
		Problem<PermutationSolution<Integer>> problem;
		Algorithm<List<PermutationSolution<Integer>>> algorithm;
		MutationOperator<PermutationSolution<Integer>> mutation;

		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(1);

		problem = new TSP("/tspInstances/kroA100.tsp");

		System.out.println("Number of Variables: " + problem.getNumberOfVariables());

		mutation = new PermutationSwapMutation<Integer>(MUTATION_PROBABILITY);

		algorithm = new SimulatedAnnealingBuilder<PermutationSolution<Integer>>(problem, mutation,
				new SimpleMinDoubleComparator()).setMinimalTemperature(MINIMAL_TEMPERATURE)
						.setInitialTemperature(INITIAL_TEMPERATURE).setRateOfCooling(RATE_OF_COOLING).setKFactor(K)
						.build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<PermutationSolution<Integer>> population = algorithm.getResult();

		long computingTime = algorithmRunner.getComputingTime();

		System.out.println("Solution:");
		System.out.println(population.get(0));

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
