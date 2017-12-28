package org.uma.jmetal.runner.singleobjective.TS;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.TS.TabuSearchBuilder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.BinaryFlipMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRPClassic;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.MaxNeighborSolutionLocator;
import org.uma.jmetal.util.comparator.SimpleMaxDoubleComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a Tabu Search algorithm. The target problem is
 * NRP/TSP.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class TabuSearchRunnerNRP {

	public static void main(String[] args) throws Exception {

		Problem<PermutationSolution<Integer>> problem;
		MutationOperator<PermutationSolution<Integer>> mutation;

		Algorithm<PermutationSolution<Integer>> algorithm;
		double mutationProbability = 0.3;
		int tabuListSize = 100;
		int numbOfIterations = 2000;

		System.out.println("Solving NRP");
		problem = new NRPClassic("/nrpClassicInstances/nrp3.txt");// 500(Min costs)//new
																	// NRPClassic("/nrpClassicInstances/myNRP10Customers.txt");
		mutation = new BinaryFlipMutation<PermutationSolution<Integer>>(mutationProbability);

		algorithm = new TabuSearchBuilder<PermutationSolution<Integer>>(problem, mutation, tabuListSize,
				numbOfIterations, new SimpleMaxDoubleComparator(), new MaxNeighborSolutionLocator<>()).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		PermutationSolution<Integer> solution = algorithm.getResult(); // TODO: set ACO, SA to this single result
																		// instead of list.

		if (solution == null) {// TODO: check whether the result if no solution found equals null.
			System.out.println("No Result found");
			System.exit(0);
		}

		List<PermutationSolution<Integer>> population = new ArrayList<>(1);
		population.add(solution);

		System.out.println("Solution:" + solution);
		System.out.println("Optimal Solution: " + solution.getObjective(0));

		long computingTime = algorithmRunner.getComputingTime();

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
