package org.uma.jmetal.runner.singleobjective.R;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.randomsearch.RandomSearchBuilder;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRPRealisticBinarySolution;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a Tabu Search algorithm. The target problem is
 * NRP.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class RandomSearchNRPRealistic {

	public static final double COST_FACTOR = 0.5;
	public static final int MAX_EVALUATION = 50000;

	public static void main(String[] args) throws Exception {

		Problem<BinarySolution> problem;
		Algorithm<List<BinarySolution>> algorithm;

		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(1 - ((1 - COST_FACTOR) / 2));

		problem = new NRPRealisticBinarySolution("/nrpRealisticInstances/nrp-e1.txt", COST_FACTOR);
		algorithm = new RandomSearchBuilder<BinarySolution>(problem).setMaxEvaluations(MAX_EVALUATION).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<BinarySolution> population = algorithm.getResult();
		long computingTime = algorithmRunner.getComputingTime();

		System.out.println(population.get(0).getObjective(0));

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
