package org.uma.jmetal.runner.singleobjective.TS;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.TS.TabuSearchBuilder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.MinNeighborSolutionFinder;
import org.uma.jmetal.util.comparator.SimpleMinDoubleComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a Tabu Search algorithm. The target problem is
 * NRP.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class TabuSearchRunnerTSP {

	public static void main(String[] args) throws Exception {

		Problem<PermutationSolution<Integer>> problem;
		MutationOperator<PermutationSolution<Integer>> mutation;

		Algorithm<List<PermutationSolution<Integer>>> algorithm;
		double mutationProbability = 0.3;
		int tabuListSize = 100;
		int numbOfIterations = 2000;

		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(1);//probability for 0.
		
		System.out.println("Solving TSP");
		problem = new TSP("/tspInstances/kroA100.tsp");
		mutation = new PermutationSwapMutation<Integer>(mutationProbability);

		algorithm = new TabuSearchBuilder<PermutationSolution<Integer>>(problem, mutation, tabuListSize,
				numbOfIterations, new SimpleMinDoubleComparator(), new MinNeighborSolutionFinder<>()).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<PermutationSolution<Integer>> population = algorithm.getResult(); // TODO: set ACO, SA to this single result
																		// instead of list.

		System.out.println("Solution:" + population.get(0));
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
