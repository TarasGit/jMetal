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
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.SetCoverage;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.utility.GenerateScatterPlotChart;

/**
 * Class for configuring and running the NSGA-II algorithm to solve the
 * bi-objective NRP.
 *
 * @author Taras Iks
 */

public class NSGAIIRunnerNRPRealistic extends AbstractAlgorithmRunner {

	public static final boolean METRICS = true;
	public static final double COST_FACTOR = 0.5;
	public static final double CROSSOVER_PROBABILITY = 0.5;
	public static final double MUTATION_PROBABILITY = 0.5;
	public static final int POPULATION_SIZE = 500;
	public static final int MAX_EVALUATIONS = 250000;

	public static final double INITIAL_POPOULATION_PROBABILITY = 0.99; // for 0

	public static void main(String[] args) throws JMetalException, IOException {
		// JMetalRandom.getInstance().setSeed(100L);

		Problem<BinarySolution> problem;
		Algorithm<List<BinarySolution>> algorithm;
		CrossoverOperator<BinarySolution> crossover;
		MutationOperator<BinarySolution> mutation;
		SelectionOperator<List<BinarySolution>, BinarySolution> selection;
		AlgorithmRunner algorithmRunner;
		double data2[] = null, data1[] = null;

		problem = new NRPRealisticMultiObjectiveBinarySolution("/nrpRealisticInstances/nrp-e1.txt", COST_FACTOR);

		selection = new BinaryTournamentSelection<BinarySolution>(
				new RankingAndCrowdingDistanceComparator<BinarySolution>());

		crossover = new SinglePointCrossover(CROSSOVER_PROBABILITY);
		mutation = new BitFlipOrExchangeMutation(MUTATION_PROBABILITY);

		algorithm = new NSGAIIBuilder<BinarySolution>(problem, crossover, mutation).setSelectionOperator(selection)
				.setMaxEvaluations(MAX_EVALUATIONS).setPopulationSize(POPULATION_SIZE)
				.setInitialPopulationProbability(INITIAL_POPOULATION_PROBABILITY).build();

		algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<BinarySolution> population = algorithm.getResult();

		int size = population.size();

		data1 = new double[size];
		data2 = new double[size];
		for (int i = 0; i < size; i++) {
			data1[i] = population.get(i).getObjective(0) * -1;
			data2[i] = population.get(i).getObjective(1);
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

		if (METRICS) {
			System.out.println("");
			System.out.println("Metrics...");
			/* Compute SPREAD Metric */
			Spread<BinarySolution> spread = new Spread<>();
			spread.setReferenceParetoFront("./referenceFronts/NRPRealistic.rf");
			double spreadMetric = spread.evaluate(population);
			System.out.println("SPREAD METRIC: " + spreadMetric);

			SetCoverage sc = new SetCoverage();
			double setCoverageMetric = sc.evaluate(
					FrontUtils.convertFrontToSolutionList(new ArrayFront("./referenceFronts/NRPRealistic.rf")),
					population);
			System.out.println("Set Coverage Metric: " + setCoverageMetric);

			Epsilon<BinarySolution> epsilon = new Epsilon<>();
			epsilon.setReferenceParetoFront(new ArrayFront("./referenceFronts/NRPRealistic.rf"));
			double epsilonMetric = epsilon.evaluate(population);
			System.out.println("Epsilon Metric: " + epsilonMetric);

			GenerationalDistance<BinarySolution> generationalDistance = new GenerationalDistance<>();
			generationalDistance.setReferenceParetoFront("./referenceFronts/NRPRealistic.rf");
			double generationalDistanceMetrik = generationalDistance.evaluate(population);
			System.out.println("Generational Distance: " + generationalDistanceMetrik);

			InvertedGenerationalDistance<BinarySolution> invertedGenerationalDistance = new InvertedGenerationalDistance<BinarySolution>();
			invertedGenerationalDistance.setReferenceParetoFront("./referenceFronts/NRPRealistic.rf");
			double invertedGeneratlDistanceMetrik = invertedGenerationalDistance.evaluate(population);
			System.out.println("Inverted General Distance: " + invertedGeneratlDistanceMetrik);

			InvertedGenerationalDistancePlus<BinarySolution> invertedGenerationalDistancePlus = new InvertedGenerationalDistancePlus<BinarySolution>();
			invertedGenerationalDistancePlus.setReferenceParetoFront("./referenceFronts/NRPRealistic.rf");
			double invertedGenerationalDistancePlusMetrik = invertedGenerationalDistancePlus.evaluate(population);
			System.out.println("Inverted Generational Distance Plus: " + invertedGenerationalDistancePlusMetrik);

			PISAHypervolume<BinarySolution> hypervolume = new PISAHypervolume<BinarySolution>();
			hypervolume.setReferenceParetoFront("./referenceFronts/NRPRealistic.rf");
			double hypervolumeMetrik = hypervolume.evaluate(population);
			System.out.println("Hypervolume: " + hypervolumeMetrik);
		}

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed());
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
	}
}