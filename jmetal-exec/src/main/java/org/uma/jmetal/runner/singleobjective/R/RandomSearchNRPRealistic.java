package org.uma.jmetal.runner.singleobjective.R;

import java.util.Comparator;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.randomsearch.RandomSearchBuilder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.BitFlipOrExchangeMutation;
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
		
		MutationOperator<BinarySolution> mutation;

		mutation = new BitFlipOrExchangeMutation(0);


		problem = new NRPRealisticBinarySolution("/nrpRealisticInstances/nrp-e1.txt", COST_FACTOR);
		algorithm = new RandomSearchBuilder<BinarySolution>(problem, mutation).setMaxEvaluations(MAX_EVALUATION)
				.setInitialPopulationProbability(0.85).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<BinarySolution> population = algorithm.getResult();
		long computingTime = algorithmRunner.getComputingTime();

		population.sort(new Comparator<BinarySolution>() {
			public int compare(BinarySolution o1, BinarySolution o2) {
				if(o1.getObjective(0) > o2.getObjective(0))
					return 1;
				else if(o1.getObjective(0) < o2.getObjective(0))
					return -1;
				else
					return 0;
			};
		});
		System.out.println("Best Solution: " + population.get(0).getObjective(0));

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
