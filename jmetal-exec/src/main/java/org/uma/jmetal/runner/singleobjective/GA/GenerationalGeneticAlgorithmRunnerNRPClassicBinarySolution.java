
package org.uma.jmetal.runner.singleobjective.GA;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipOrExchangeMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRPClassicBinarySolution;
import org.uma.jmetal.solution.BinarySolution;
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
public class GenerationalGeneticAlgorithmRunnerNRPClassicBinarySolution {
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
		Problem<BinarySolution> problem;
		Algorithm<BinarySolution> algorithm;
		CrossoverOperator<BinarySolution> crossover;
		MutationOperator<BinarySolution> mutation;
		SelectionOperator<List<BinarySolution>, BinarySolution> selection;
		Ordering ordering = Ordering.DESCENDING;// TODO: add description.

		double costFactor = 0.5;

		/*
		 * The initial solution for GA should not be 0 but also smaller than factor *
		 * costs, otherwise unvalid solutions -> 0.9 for zero ist ok.
		 */
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(0.9);// 0.9 for Zero.

		problem = new NRPClassicBinarySolution("/nrpClassicInstances/nrp1.txt", costFactor);// 500(Min costs)//new

		crossover = new SinglePointCrossover(0.5);//new BinarySinglePointCrossover(0.9);// new PMXCrossover(0.9);

		double mutationProbability = 0.3;
		// double mutationProbability = 1.0 / problem.getNumberOfVariables();
		// mutation = new
		// BinaryFlipMutation<PermutationSolution<Integer>>(mutationProbability);
		mutation = new BitFlipOrExchangeMutation(mutationProbability);
		// mutation = new PermutationSwapMutation<Integer>(mutationProbability);

		selection = new BinaryTournamentSelection<BinarySolution>(new SimpleMaxSolutionComparator<BinarySolution>());
		// new RankingAndCrowdingDistanceComparator<PermutationSolution<Integer>>());

		algorithm = new GeneticAlgorithmBuilder<BinarySolution>(problem, crossover, mutation, ordering).setPopulationSize(100)
				.setMaxEvaluations(40000).setSelectionOperator(selection).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		BinarySolution solution = algorithm.getResult();
		List<BinarySolution> population = new ArrayList<>(1);
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
