
package org.uma.jmetal.runner.singleobjective.GA;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.PMXCrossover;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.PermutationProblem;
import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.ObjectiveComparator.Ordering;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a generational genetic algorithm. The target
 * problem is OneMax.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class GenerationalGeneticAlgorithmRunnerTSP {

	public static final double CROSSOVER_PROBABILITY = 0.9;
	public static final int POPULATION_SIZE = 100;
	public static final int MAX_EVALUATIONS = 2500000;

	public static void main(String[] args) throws Exception {
		PermutationProblem<PermutationSolution<Integer>> problem;
		Algorithm<PermutationSolution<Integer>> algorithm;
		CrossoverOperator<PermutationSolution<Integer>> crossover;
		MutationOperator<PermutationSolution<Integer>> mutation;
		SelectionOperator<List<PermutationSolution<Integer>>, PermutationSolution<Integer>> selection;
		Ordering ordering = Ordering.ASCENDING;

		problem = new TSP("/tspInstances/kroA100.tsp");
		crossover = new PMXCrossover(CROSSOVER_PROBABILITY);

		double mutationProbability = 1.0 / problem.getNumberOfVariables();
		mutation = new PermutationSwapMutation<Integer>(mutationProbability);

		selection = new BinaryTournamentSelection<PermutationSolution<Integer>>(
				new RankingAndCrowdingDistanceComparator<PermutationSolution<Integer>>());

		algorithm = new GeneticAlgorithmBuilder<>(problem, crossover, mutation, ordering)
				.setPopulationSize(POPULATION_SIZE).setMaxEvaluations(MAX_EVALUATIONS).setSelectionOperator(selection)
				.build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		PermutationSolution<Integer> solution = algorithm.getResult();
		List<PermutationSolution<Integer>> population = new ArrayList<>(1);
		population.add(solution);

		System.out.println(solution.getObjective(0));
		long computingTime = algorithmRunner.getComputingTime();

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
