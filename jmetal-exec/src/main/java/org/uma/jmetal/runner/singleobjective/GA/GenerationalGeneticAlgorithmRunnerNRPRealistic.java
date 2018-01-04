
package org.uma.jmetal.runner.singleobjective.GA;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.BinarySinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BinaryFlipMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.PermutationProblem;
import org.uma.jmetal.problem.singleobjective.NRPClassic;
import org.uma.jmetal.problem.singleobjective.NRPRealistic;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.ObjectiveComparator.Ordering;
import org.uma.jmetal.util.comparator.SimpleMaxSolutionComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/*
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class GenerationalGeneticAlgorithmRunnerNRPRealistic {
	/**
	 * Usage: java
	 * org.uma.jmetal.runner.singleobjective.BinaryGenerationalGeneticAlgorithmRunner
	 */

	/*
	 * TODO: - generation of initial Solution changed! - comparator changed -> add
	 * an configuration for NRP/TSP.
	 * 
	 */
	public static void main(String[] args) throws Exception {
		PermutationProblem<PermutationSolution<Integer>> problem;
		Algorithm<PermutationSolution<Integer>> algorithm;
		CrossoverOperator<PermutationSolution<Integer>> crossover;
		MutationOperator<PermutationSolution<Integer>> mutation;
		SelectionOperator<List<PermutationSolution<Integer>>, PermutationSolution<Integer>> selection;
		Ordering ordering = Ordering.DESCENDING;

		double costFactor = 0.5;

		/*
		 * The initial solution for GA should not be 0 but also smaller than factor *
		* costs, otherwise unvalid solutions -> 0.9 for zero ist ok.
		*/
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(0.9);// 0.9 for Zero.

		problem = new NRPRealistic("/nrpRealisticInstances/nrp-e1.txt", costFactor);// 500(Min costs)//new

		crossover = new BinarySinglePointCrossover(0.9);// new PMXCrossover(0.9);

		double mutationProbability = 0.3;
		// double mutationProbability = 1.0 / problem.getNumberOfVariables();
		mutation = new BinaryFlipMutation<PermutationSolution<Integer>>(mutationProbability);
		// mutation = new PermutationSwapMutation<Integer>(mutationProbability);

		selection = new BinaryTournamentSelection<PermutationSolution<Integer>>(
				new SimpleMaxSolutionComparator<PermutationSolution<Integer>>());
		// new RankingAndCrowdingDistanceComparator<PermutationSolution<Integer>>());

		algorithm = new GeneticAlgorithmBuilder<>(problem, crossover, mutation, ordering).setPopulationSize(100)
				.setMaxEvaluations(100000).setSelectionOperator(selection).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		PermutationSolution<Integer> solution = algorithm.getResult();
		List<PermutationSolution<Integer>> population = new ArrayList<>(1);
		population.add(solution);

		System.out.println("End Result: " + solution);
		long computingTime = algorithmRunner.getComputingTime();

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
