package org.uma.jmetal.runner.multiobjective.GA;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.WindowConstants;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipOrExchangeMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRPRealisticMultiObjectiveBinarySolution;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.utility.GenerateScatterPlotChart;

/**
 * Class for configuring and running the NSGA-II algorithm to solve the
 * bi-objective NRP.
 *
 * @author Taras Iks
 */

public class NSGAIIRunnerNRPRealisticScatterPlotBinarySolution extends AbstractAlgorithmRunner {

	private static final double COST_FACTOR = 0.5;
	private static final double CROSSOVER_PROBABILITY = 0.5;
	private static final double MUTATION_PROBABILITY = 0.5;
	private static final int POPULATION_SIZE = 500;
	private static final int MAX_EVALUATIONS = 250000;

	public static void main(String[] args) throws JMetalException, IOException {
		// JMetalRandom.getInstance().setSeed(100L);

		Problem<BinarySolution> problem;
		Algorithm<List<BinarySolution>> algorithm;
		CrossoverOperator<BinarySolution> crossover;
		MutationOperator<BinarySolution> mutation;
		SelectionOperator<List<BinarySolution>, BinarySolution> selection;
		AlgorithmRunner algorithmRunner;
		double data2[] = null, data1[] = null;

		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(0.99);// probability for Zero.

		problem = new NRPRealisticMultiObjectiveBinarySolution("/nrpRealisticInstances/nrp-e1.txt", COST_FACTOR);

		selection = new BinaryTournamentSelection<BinarySolution>(
				new RankingAndCrowdingDistanceComparator<BinarySolution>());

		crossover = new SinglePointCrossover(CROSSOVER_PROBABILITY);
		mutation = new BitFlipOrExchangeMutation(MUTATION_PROBABILITY);

		algorithm = new NSGAIIBuilder<BinarySolution>(problem, crossover, mutation).setSelectionOperator(selection)
				.setMaxEvaluations(MAX_EVALUATIONS).setPopulationSize(POPULATION_SIZE).build();

		algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<BinarySolution> population = algorithm.getResult();

		int size = population.size();

		data1 = new double[size];
		data2 = new double[size];
		for (int i = 0; i < size; i++) {
			data1[i] = population.get(i).getObjective(0);
			data2[i] = population.get(i).getObjective(1) * -1;
		}

		/* Create List of Arrays with data */
		List<double[]> doubleArrayList = new ArrayList<>();
		doubleArrayList.add(data1);
		doubleArrayList.add(data2);

		/* Create Chart */
		List<String> nameList = Arrays.asList("NSGA-II");
		GenerateScatterPlotChart example = new GenerateScatterPlotChart("Scatter Chart", doubleArrayList, nameList);
		example.setSize(1200, 800);
		example.setLocationRelativeTo(null);
		example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		example.setVisible(true);

		long computingTime = algorithmRunner.getComputingTime();

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed());
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
	}
}