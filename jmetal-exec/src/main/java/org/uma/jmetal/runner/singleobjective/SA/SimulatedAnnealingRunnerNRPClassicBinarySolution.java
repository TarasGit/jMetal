package org.uma.jmetal.runner.singleobjective.SA;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.SA.SimulatedAnnealingBuilder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.MyBitFlipMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRPClassicBinarySolution;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.SimpleMaxDoubleComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a simulated annealing algorithm. The target
 * problem is TSP.
 *
 * @author Taras Iks <ikstaras@gmail>
 */
public class SimulatedAnnealingRunnerNRPClassicBinarySolution {
	/**
	 * Usage: java
	 * org.uma.jmetal.runner.singleobjective.BinaryGenerationalGeneticAlgorithmRunne
	 */

	public static final double RATE_OF_COOLING = 0.0001;
	/*
	 * IMPORTANT: don't increase the temperature, because the formulate for SA depends on it,
	 * and for very high temperatures the most of the time you will get the acceptance probability equal to 1.
	 * 
	 * SOLUTION: change  RATE OF COOLING to be smaller to get better solution quality OR multiply temperature
	 * in SA with some factor, to be able to use another temperatures.
	 * */
	public static final int INITIAL_TEMPERATURE = 100;
	public static final int MINIMAL_TEMPERATURE = 1;
	public static final double MUTATION_PROBABILITY = 0.5;//50% bit mutation and 50% swap mutation.
	public static final double COST_FACTOR = 0.5;

	public static void main(String[] args) throws Exception {
		Problem<BinarySolution> problem;
		Algorithm<List<BinarySolution>> algorithm;
		MutationOperator<BinarySolution> mutation;
		
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(1);//1 = probability for 0.

		problem = new NRPClassicBinarySolution("/nrpClassicInstances/nrp1.txt", COST_FACTOR);// 500(Min costs)//new

		System.out.println("Number of Variables: " + problem.getNumberOfVariables());// Taras
		
		mutation = new MyBitFlipMutation(MUTATION_PROBABILITY);
		
		algorithm = new SimulatedAnnealingBuilder<BinarySolution>(problem, mutation, new SimpleMaxDoubleComparator())
				.setMinimalTemperature(MINIMAL_TEMPERATURE).setInitialTemperature(INITIAL_TEMPERATURE)
				.setRateOfCooling(RATE_OF_COOLING).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<BinarySolution> population = algorithm.getResult();// List<DefaultIntegerPermutationSolution>
																		// solution = algorithm.getResult() ;

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
