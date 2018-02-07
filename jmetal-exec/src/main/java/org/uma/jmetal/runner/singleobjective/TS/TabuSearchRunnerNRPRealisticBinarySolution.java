package org.uma.jmetal.runner.singleobjective.TS;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.TS.TabuSearchBuilder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.BitFlipOrExchangeMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRPRealisticBinarySolution;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.MaxNeighborSolutionFinder;
import org.uma.jmetal.util.comparator.SimpleMaxDoubleComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a Tabu Search algorithm. The target problem is
 * NRP.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class TabuSearchRunnerNRPRealisticBinarySolution {

	private static final double MUTATION_PROBABILITY = 0.3;
	private static final int TABU_LIST_SIZE = 50;
	private static final int NUMBER_OF_ITERATIONS = 300;
	private static final int NUMBER_OF_NEIGHBORS = 100;
	private static final double COST_FACTOR = 0.5;
	
	public static void main(String[] args) throws Exception {

		Problem<BinarySolution> problem;
		MutationOperator<BinarySolution> mutation;

		Algorithm<List<BinarySolution>> algorithm;

		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(0.9);//probability for 0.

		System.out.println("Solving NRP");
		problem = new NRPRealisticBinarySolution("/nrpRealisticInstances/nrp-e1.txt", COST_FACTOR);		
		mutation = new BitFlipOrExchangeMutation(MUTATION_PROBABILITY);

		algorithm = new TabuSearchBuilder<BinarySolution>(problem, mutation, TABU_LIST_SIZE, 
				NUMBER_OF_ITERATIONS, new SimpleMaxDoubleComparator(), new MaxNeighborSolutionFinder<>(), NUMBER_OF_NEIGHBORS).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<BinarySolution> population = algorithm.getResult(); 

		System.out.println("Solution:" + population.get(0).getVariableValueString(0));
		System.out.println("Optimal Solution: " + population.get(0).getObjective(0));

		long computingTime = algorithmRunner.getComputingTime();

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
