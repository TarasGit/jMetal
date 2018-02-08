
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
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
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
 * Example of experimental study based on solving two binary problems with four
 * algorithms: NSGAII, SPEA2, MOCell, and MOCHC
 *
 * This experiment assumes that the reference Pareto front are not known, so the
 * names of files containing them and the directory where they are located must
 * be specified.
 *
 * Six quality indicators are used for performance assessment.
 *
 * The steps to carry out the experiment are: 1. Configure the experiment 2.
 * Execute the algorithms 3. Generate the reference Pareto fronts 4. Compute que
 * quality indicators 5. Generate Latex tables reporting means and medians 6.
 * Generate Latex tables with the result of applying the Wilcoxon Rank Sum Test
 * 7. Generate Latex tables with the ranking obtained by applying the Friedman
 * test 8. Generate R scripts to obtain boxplots
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * changed by Taras Iks<ikstaras@gmail.com>
 */
public class MetaheuristicStudy {
	private static final int INDEPENDENT_RUNS = 2;

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			throw new JMetalException("Needed arguments: experimentBaseDirectory");
		}
		String experimentBaseDirectory = args[0];

		List<ExperimentProblem<BinarySolution>> problemList = new ArrayList<>();
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(0.9);// probability for 0.

		problemList.add(new ExperimentProblem<>(
				new NRPRealisticMultiObjectiveBinarySolution("/nrpRealisticInstances/nrp-e1.txt", 0.5)));

		List<ExperimentAlgorithm<BinarySolution, List<BinarySolution>>> algorithmList = configureAlgorithmList(
				problemList);

		Experiment<BinarySolution, List<BinarySolution>> experiment;
		experiment = new ExperimentBuilder<BinarySolution, List<BinarySolution>>("BinaryProblemsStudy")
				.setAlgorithmList(algorithmList).setProblemList(problemList)
				.setExperimentBaseDirectory(experimentBaseDirectory).setOutputParetoFrontFileName("FUN")
				.setOutputParetoSetFileName("VAR")
				.setReferenceFrontDirectory(experimentBaseDirectory + "/referenceFronts")
				.setIndicatorList(Arrays.asList(new Epsilon<BinarySolution>(), new Spread<BinarySolution>(),
						new GenerationalDistance<BinarySolution>(), new PISAHypervolume<BinarySolution>(),
						new InvertedGenerationalDistance<BinarySolution>(),
						new InvertedGenerationalDistancePlus<BinarySolution>()))
				.setIndependentRuns(INDEPENDENT_RUNS).setNumberOfCores(8).build();

		new ExecuteAlgorithms<>(experiment).run();
		new GenerateReferenceParetoFront(experiment).run();
		new ComputeQualityIndicators<>(experiment).run();
		new GenerateLatexTablesWithStatistics(experiment).run();
		new GenerateWilcoxonTestTablesWithR<>(experiment).run();
		new GenerateFriedmanTestTables<>(experiment).run();
		new GenerateBoxplotsWithR<>(experiment).setRows(1).setColumns(2).setDisplayNotch().run();
	}

	/**
	 * The algorithm list is composed of pairs {@link Algorithm} + {@link Problem}
	 * which form part of a {@link ExperimentAlgorithm}, which is a decorator for
	 * class {@link Algorithm}.
	 */

	static List<ExperimentAlgorithm<BinarySolution, List<BinarySolution>>> configureAlgorithmList(
			List<ExperimentProblem<BinarySolution>> problemList) {
		List<ExperimentAlgorithm<BinarySolution, List<BinarySolution>>> algorithms = new ArrayList<>();
		
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(0.99);// 0.9 for Zero.

		/* NSGAII Study*/
		for (int i = 0; i < problemList.size(); i++) {
			Algorithm<List<BinarySolution>> algorithm = new NSGAIIBuilder<BinarySolution>(
					problemList.get(i).getProblem(), new SinglePointCrossover(0.5), new BitFlipOrExchangeMutation(0.5))
							.setSelectionOperator(new BinaryTournamentSelection<BinarySolution>(
									new RankingAndCrowdingDistanceComparator<BinarySolution>()))
							.setMaxEvaluations(100000).setPopulationSize(1000).build();
			
			algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));

		}

		/* TS Study */
		for (int i = 0; i < problemList.size(); i++) {
			Algorithm<List<BinarySolution>> algorithm = new MOTabuSearchBuilder<BinarySolution>(
					problemList.get(i).getProblem(), new BitFlipOrExchangeMutation(0.9), 1000, 100,
					100, new MONotInTabuListSolutionFinder<>()).build();

			algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
		}

		/* ACO Study */
		for (int i = 0; i < problemList.size(); i++) {

			Algorithm<List<BinarySolution>> algorithm = new MOAntColonyOptimizationBuilderNRP<BinarySolution>(
					problemList.get(i).getProblem(), 100, 10, 1, 0.1, 1).build();
			algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
		}

		/* SA Study */
		for (int i = 0; i < problemList.size(); i++) {

			Algorithm<List<BinarySolution>> algorithm = new MOSimulatedAnnealingBuilder<BinarySolution>(
					problemList.get(i).getProblem(), new BitFlipOrExchangeMutation(0.5), new SimpleMaxDoubleComparator())
							.setMinimalTemperature(1).setInitialTemperature(1000).setRateOfCooling(0.1).build();
			algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
		}

		/* Random Study */
		for (int i = 0; i < problemList.size(); i++) {
			Algorithm<List<BinarySolution>> algorithm = new RandomSearchBuilder<BinarySolution>(
					problemList.get(i).getProblem()).setMaxEvaluations(40000).build();
			algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
		}

		return algorithms;
	}
}
