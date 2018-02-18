package org.uma.jmetal.runner.singleobjective.ACO;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.ACO.NRP.AntColonyOptimizationBuilderNRP;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRPRealisticBinarySolution;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a Ant Colony Optimization algorithm. The target
 * problem is NRP.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class AntColonyOptimizationRunnerNRPRealistic {

	public static final int NUMBER_OF_ANTS = 100;
	public static final double ALPHA = 2;// importance of pheromone trail, x >= 0,
	public static final double BETA = 1;// importance between source and destination, x >= 1
	public static final double Q = 0.0;// pheromone deposited level;
	public static final double RHO = 0.3;// pheromone evaporation level, 0<=x<=1 -> 0.1 <= x <= 0.01 is ok.

	public static final double COST_FACTOR = 0.5;

	public static void main(String[] args) throws Exception {

		Problem<BinarySolution> problem;
		Algorithm<List<BinarySolution>> algorithm;

		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(1);// probability = 1 for 0

		problem = new NRPRealisticBinarySolution("/nrpRealisticInstances/nrp-e1.txt", COST_FACTOR);

		System.out.println("Number of Variables: " + problem.getNumberOfVariables());

		algorithm = new AntColonyOptimizationBuilderNRP<BinarySolution>(problem, NUMBER_OF_ANTS, ALPHA, BETA, RHO, Q)
				.build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
		List<BinarySolution> population = algorithm.getResult();

		long computingTime = algorithmRunner.getComputingTime();
		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
