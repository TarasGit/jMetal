
//

//

package org.uma.jmetal.experiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.MOACO.MOAntColonyOptimizationBuilderNRP;
import org.uma.jmetal.algorithm.multiobjective.MOSA.MOSimulatedAnnealingBuilder;
import org.uma.jmetal.algorithm.multiobjective.MOTS.MOTabuSearchBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.randomsearch.RandomSearchBuilder;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipOrExchangeMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRPRealisticMultiObjectiveBinarySolution;
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.MONotInTabuListSolutionFinder;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.comparator.SimpleMaxDoubleComparator;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.ComputeQualityIndicators;
import org.uma.jmetal.util.experiment.component.ExecuteAlgorithms;
import org.uma.jmetal.util.experiment.component.GenerateBoxplotsWithR;
import org.uma.jmetal.util.experiment.component.GenerateFriedmanTestTables;
import org.uma.jmetal.util.experiment.component.GenerateLatexTablesWithStatistics;
import org.uma.jmetal.util.experiment.component.GenerateReferenceParetoFront;
import org.uma.jmetal.util.experiment.component.GenerateWilcoxonTestTablesWithR;
import org.uma.jmetal.util.experiment.util.ExperimentAlgorithm;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;

/**
 * Six quality indicators are used for performance assessment.
 *
 * The steps to carry out the experiment are: 1. Configure the experiment 2.
 * Execute the algorithms 3. Generate the reference Pareto fronts 4. Compute que
 * quality indicators 5. Generate Latex tables reporting means and medians 6.
 * Generate Latex tables with the result of applying the Wilcoxon Rank Sum Test
 * 7. Generate Latex tables with the ranking obtained by applying the Friedman
 * test 8. Generate R scripts to obtain boxplots
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es> changed by Taras
 *         Iks<ikstaras@gmail.com>
 */
public class MetaheuristicStudy {
	private static final int INDEPENDENT_RUNS = 2;

	/* GA */
	public static final int POPULATION_SIZE = 500;
	public static final int MAX_EVALUATIONS = 250000;
	public static final double INITIAL_SOLUTION_PROBABILITY_GA = 0.98;

	/* ACO */
	public static final int NUMBER_OF_ANTS = 100;
	public static final double ALPHA = 2;// importance of pheromone trail, x >= 0,
	public static final double BETA = 2;// importance between source and destination, x >= 1
	public static final double Q = 100;// pheromone deposited level;
	public static final double RHO = 0.01;// pheromone evaporation level, 0<=x<=1 -> 0.1 <= x <= 0.01 is ok.

	public static final double INITIAL_SOLUTION_PROBABILITY_ACO = 1;

	/* RANDOM */
	public static final int RANDOM_MAX_EVALUATION = 30000;
	public static final double INITIAL_SOLUTION_PROBABILITY_R = 0.85;

	/* SA */
	public static final double RATE_OF_COOLING = 0.0005;
	public static final int INITIAL_TEMPERATURE = 1000;
	public static final int MINIMAL_TEMPERATURE = 1;
	public static final double INITIAL_SOLUTION_PROBABILITY_SA = 0.95;
	public static final double MUTATION_PROBABILITY_SA = 0.95;

	/* TS */
	public static final int TABU_LIST_SIZE = 1000;
	public static final int NUMBER_OF_ITERATIONS = 1000;
	public static final int NUMBER_OF_NEIGHBORS = 100;
	public static final double INITIAL_SOLUTION_PROBABILITY_TS = 1;

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			throw new JMetalException("Needed arguments: experimentBaseDirectory");
		}
		String experimentBaseDirectory = args[0];

		List<ExperimentProblem<BinarySolution>> problemList = new ArrayList<>();

		problemList.add(new ExperimentProblem<>(
				new NRPRealisticMultiObjectiveBinarySolution("/nrpRealisticInstances/nrp-e1.txt", 0.5)));

		List<ExperimentAlgorithm<BinarySolution, List<BinarySolution>>> algorithmList = configureAlgorithmList(
				problemList);

		Experiment<BinarySolution, List<BinarySolution>> experiment;
		experiment = new ExperimentBuilder<BinarySolution, List<BinarySolution>>("MetaheuristicsStudy")
				.setAlgorithmList(algorithmList).setProblemList(problemList)
				.setExperimentBaseDirectory(experimentBaseDirectory).setOutputParetoFrontFileName("FUN")
				.setOutputParetoSetFileName("VAR")
				.setReferenceFrontDirectory(experimentBaseDirectory + "/referenceFronts")
				.setIndicatorList(Arrays.asList(new Spread<BinarySolution>(), new Epsilon<BinarySolution>(),
						new GenerationalDistance<BinarySolution>(), new PISAHypervolume<BinarySolution>(),
						new InvertedGenerationalDistance<BinarySolution>(),
						new InvertedGenerationalDistancePlus<BinarySolution>()))
				.setIndependentRuns(INDEPENDENT_RUNS).setNumberOfCores(1).build();

		System.out.println("Start computing");

		/*
		 * Changed to sequential execution, because of the initial solution
		 * configuration for each algorithm.
		 */
		new ExecuteAlgorithms<>(experiment).run();
		System.out.println("Execute Algorithms - finished");

		new GenerateReferenceParetoFront(experiment).run();
		System.out.println("Generate Reference Front - finished");

		new ComputeQualityIndicators<>(experiment).run();
		System.out.println("Compute Quality Indicators - finished");

		new GenerateLatexTablesWithStatistics(experiment).run();
		System.out.println("Generate Latex Tables - finished");

		new GenerateWilcoxonTestTablesWithR<>(experiment).run();
		System.out.println("Generate Wilcoxon Tables - finished");

		new GenerateFriedmanTestTables<>(experiment).run();
		System.out.println("Generate Friedman Test - finished");

		new GenerateBoxplotsWithR<>(experiment).setRows(1).setColumns(2).setDisplayNotch().run();
		System.out.println("Generate Boxplot - finished");
	}

	/**
	 * The algorithm list is composed of pairs {@link Algorithm} + {@link Problem}
	 * which form part of a {@link ExperimentAlgorithm}, which is a decorator for
	 * class {@link Algorithm}.
	 */

	static List<ExperimentAlgorithm<BinarySolution, List<BinarySolution>>> configureAlgorithmList(
			List<ExperimentProblem<BinarySolution>> problemList) {
		List<ExperimentAlgorithm<BinarySolution, List<BinarySolution>>> algorithms = new ArrayList<>();

		/* NSGAII Study */
		for (int i = 0; i < problemList.size(); i++) {
			Algorithm<List<BinarySolution>> algorithm = new NSGAIIBuilder<BinarySolution>(
					problemList.get(i).getProblem(), new SinglePointCrossover(0.5), new BitFlipOrExchangeMutation(0.5))
							.setSelectionOperator(new BinaryTournamentSelection<BinarySolution>(
									new RankingAndCrowdingDistanceComparator<BinarySolution>()))
							.setMaxEvaluations(MAX_EVALUATIONS).setPopulationSize(POPULATION_SIZE)
							.setInitialPopulationProbability(INITIAL_SOLUTION_PROBABILITY_GA).build();

			algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));

		}

		/* TS Study */
		for (int i = 0; i < problemList.size(); i++) {
			Algorithm<List<BinarySolution>> algorithm = new MOTabuSearchBuilder<BinarySolution>(
					problemList.get(i).getProblem(), new BitFlipOrExchangeMutation(0.4), TABU_LIST_SIZE,
					NUMBER_OF_ITERATIONS, NUMBER_OF_NEIGHBORS, new MONotInTabuListSolutionFinder<>())
							.setInitialPopulationProbability(INITIAL_SOLUTION_PROBABILITY_TS).build();

			algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
		}

		/* ACO Study */
		for (int i = 0; i < problemList.size(); i++) {

			Algorithm<List<BinarySolution>> algorithm = new MOAntColonyOptimizationBuilderNRP<BinarySolution>(
					problemList.get(i).getProblem(), NUMBER_OF_ANTS, ALPHA, BETA, RHO, Q,
					INITIAL_SOLUTION_PROBABILITY_ACO).build();
			algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
		}

		/* SA Study */
		for (int i = 0; i < problemList.size(); i++) {

			Algorithm<List<BinarySolution>> algorithm = new MOSimulatedAnnealingBuilder<BinarySolution>(
					problemList.get(i).getProblem(), new BitFlipOrExchangeMutation(MUTATION_PROBABILITY_SA),
					new SimpleMaxDoubleComparator()).setMinimalTemperature(MINIMAL_TEMPERATURE)
							.setInitialTemperature(INITIAL_TEMPERATURE).setRateOfCooling(RATE_OF_COOLING)
							.setInitialPopulationProbability(INITIAL_SOLUTION_PROBABILITY_SA).build();
			algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
		}

		/* Random Study */
		for (int i = 0; i < problemList.size(); i++) {
			Algorithm<List<BinarySolution>> algorithm = new RandomSearchBuilder<BinarySolution>(
					problemList.get(i).getProblem(), new BitFlipOrExchangeMutation(0))
							.setMaxEvaluations(RANDOM_MAX_EVALUATION)
							.setInitialPopulationProbability(INITIAL_SOLUTION_PROBABILITY_R).build();
			algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
		}

		return algorithms;
	}
}
