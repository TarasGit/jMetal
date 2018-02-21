package org.uma.jmetal.runner.multiobjective.AllAlgorithmsRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.WindowConstants;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.MOACO.MOAntColonyOptimizationBuilderNRP;
import org.uma.jmetal.algorithm.multiobjective.MOSA.MOSimulatedAnnealingBuilder;
import org.uma.jmetal.algorithm.multiobjective.MOTS.MOTabuSearchBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.randomsearch.RandomSearchBuilder;
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
import org.uma.jmetal.util.comparator.MONotInTabuListSolutionFinder;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.comparator.SimpleMaxDoubleComparator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.utility.GenerateScatterPlotChart;

/**
 * Class for configuring and running the NSGA-II algorithm to solve the
 * bi-objective NRP.
 *
 * @author Taras Iks
 */

public class PlotAll5RealisticAlgorithms extends AbstractAlgorithmRunner {

	public static void main(String[] args) throws JMetalException, IOException {

		/*---------------------------------------------------
		 * NSGA-II
		 * --------------------------------------------------
		 **/

		// JMetalRandom.getInstance().setSeed(100L);
		Problem<BinarySolution> problem;
		Algorithm<List<BinarySolution>> algorithmNSGA;
		CrossoverOperator<BinarySolution> crossoverNSGA;
		MutationOperator<BinarySolution> mutationNSGA;
		SelectionOperator<List<BinarySolution>, BinarySolution> selectionNSGA;

		double costFactor = 0.5;
		problem = new NRPRealisticMultiObjectiveBinarySolution("/nrpRealisticInstances/nrp-e1.txt", costFactor);
		crossoverNSGA = new SinglePointCrossover(0.5);
		AlgorithmRunner algorithmRunner = null;

		double INITIAL_SOLUTION_PROBABILITY_GA = 0.99;

		selectionNSGA = new BinaryTournamentSelection<BinarySolution>(
				new RankingAndCrowdingDistanceComparator<BinarySolution>());

		double data1NSGA[] = null, data2NSGA[] = null;
		double mutationProbabilityNSGA = 0.5;
		mutationNSGA = new BitFlipOrExchangeMutation(mutationProbabilityNSGA);

		/*
		 * nrp-e1 - 300.000 | nrp-e2 - 200.000 |
		 */
		algorithmNSGA = new NSGAIIBuilder<BinarySolution>(problem, crossoverNSGA, mutationNSGA)
				.setSelectionOperator(selectionNSGA).setMaxEvaluations(250000).setPopulationSize(500)
				.setInitialPopulationProbability(INITIAL_SOLUTION_PROBABILITY_GA).build();

		algorithmRunner = new AlgorithmRunner.Executor(algorithmNSGA).execute();

		List<BinarySolution> populationNSGA = algorithmNSGA.getResult();
		System.out.println("NSGA-II - finished");
		int sizeNSGA = populationNSGA.size();
		data1NSGA = new double[sizeNSGA];
		data2NSGA = new double[sizeNSGA];
		for (int i = 0; i < sizeNSGA; i++) {
			data1NSGA[i] = populationNSGA.get(i).getObjective(0) * -1;
			data2NSGA[i] = populationNSGA.get(i).getObjective(1);
		}

		/*----------------------------------------------------
		 * Ant Colony Optimization
		 *----------------------------------------------------
		 */

		int NUMBER_OF_ANTS = 200;
		double ALPHA = 2;// importance of pheramon trail, x >= 0,
		double BETA = 2;// importance between source and destination, x >= 1

		double Q = 5.0;// feramon deposited level;
		double RHO = 0.01;// feramon avapouration level, 0<=x<=1 -> 0.1 <= x <= 0.01 is ok.

		double INITIAL_SOLUTION_PROBABILITY_ACO = 1;

		Algorithm<List<BinarySolution>> algorithmACO;
		double data1ACO[] = null, data2ACO[] = null;

		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(1);
		algorithmACO = new MOAntColonyOptimizationBuilderNRP<BinarySolution>(problem, NUMBER_OF_ANTS, ALPHA, BETA, RHO,
				Q, INITIAL_SOLUTION_PROBABILITY_ACO).build();

		new AlgorithmRunner.Executor(algorithmACO).execute();

		List<BinarySolution> populationACO = algorithmACO.getResult();
		int sizeACO = populationACO.size();
		data1ACO = new double[sizeACO];
		data2ACO = new double[sizeACO];
		for (int i = 0; i < sizeACO; i++) {
			data1ACO[i] = populationACO.get(i).getObjective(0) * -1;
			data2ACO[i] = populationACO.get(i).getObjective(1);
		}

		/*--------------------------------------
		 * Simulated Annealing
		 * -------------------------------------
		 */
		double RATE_OF_COOLING = 0.0001;// 0.001 - nrp-e1 | 0.01 - rnp-e2 |
		int INITIAL_TEMPERATURE = 100;// 8000 - erp-e1 | 8000 - nrp-e2 |
		int MINIMAL_TEMPERATURE = 1;
		double mutationProbabilitySA = 0.999;

		double INITIAL_SOLUTION_PROBABILITY_SA = 1;

		MutationOperator<BinarySolution> mutationSA;
		SelectionOperator<List<BinarySolution>, BinarySolution> selectionSA;
		Algorithm<List<BinarySolution>> algorithmSA;
		double data1SA[] = null, data2SA[] = null;

		selectionSA = new BinaryTournamentSelection<BinarySolution>(
				new RankingAndCrowdingDistanceComparator<BinarySolution>());
		mutationSA = new BitFlipOrExchangeMutation(mutationProbabilitySA);

		algorithmSA = new MOSimulatedAnnealingBuilder<BinarySolution>(problem, mutationSA,
				new SimpleMaxDoubleComparator()).setMinimalTemperature(MINIMAL_TEMPERATURE)
						.setInitialTemperature(INITIAL_TEMPERATURE).setRateOfCooling(RATE_OF_COOLING)
						.setInitialPopulationProbability(INITIAL_SOLUTION_PROBABILITY_SA).build();

		new AlgorithmRunner.Executor(algorithmSA).execute();

		List<BinarySolution> populationSA = algorithmSA.getResult();
		int sizeSA = populationSA.size();
		data1SA = new double[sizeSA];
		data2SA = new double[sizeSA];
		for (int i = 0; i < sizeSA; i++) {
			data1SA[i] = populationSA.get(i).getObjective(0) * -1;
			data2SA[i] = populationSA.get(i).getObjective(1);
		}

		/*------------------------
		 *  Tabu Search
		 * -----------------------
		 * */
		MutationOperator<BinarySolution> mutationTS;
		Algorithm<List<BinarySolution>> algorithmTS;
		double mutationProbabilityTS = 0.5;
		int tabuListSize = 1000;
		int numberOfNeighbors = 100;
		int numbOfIterations = 1000; // 1000 - nrp-e1 |

		double INITIAL_SOLUTION_PROBABILITY_TS = 1;

		double data1TS[] = null, data2TS[] = null;
		mutationTS = new BitFlipOrExchangeMutation(mutationProbabilityTS);
		algorithmTS = new MOTabuSearchBuilder<BinarySolution>(problem, mutationTS, tabuListSize, numbOfIterations,
				numberOfNeighbors, new MONotInTabuListSolutionFinder<>())
						.setInitialPopulationProbability(INITIAL_SOLUTION_PROBABILITY_TS).build();
		new AlgorithmRunner.Executor(algorithmTS).execute();

		List<BinarySolution> populationTS = algorithmTS.getResult();

		int sizeTS = populationTS.size();
		data1TS = new double[sizeTS];
		data2TS = new double[sizeTS];
		for (int i = 0; i < sizeTS; i++) {
			data1TS[i] = populationTS.get(i).getObjective(0) * -1;
			data2TS[i] = populationTS.get(i).getObjective(1);
		}

		/*-------------------------------------------------
		 * Random
		 * -------------------------------------------------
		 * */
		double INITIAL_SOLUTION_PROBABILITY_R = 1;
		MutationOperator<BinarySolution> mutation;

		mutation = new BitFlipOrExchangeMutation(0.95);//1 for swap mutation.


		Algorithm<List<BinarySolution>> algorithmRandom;
		algorithmRandom = new RandomSearchBuilder<BinarySolution>(problem, mutation).setMaxEvaluations(40000)
				.setInitialPopulationProbability(INITIAL_SOLUTION_PROBABILITY_R).build();

		new AlgorithmRunner.Executor(algorithmRandom).execute();

		List<BinarySolution> populationRandom = algorithmRandom.getResult();
		double[] data1Random = null, data2Random = null;
		int sizeRandom = populationRandom.size();

		data1Random = new double[sizeRandom];
		data2Random = new double[sizeRandom];
		for (int i = 0; i < sizeRandom; i++) {
			data1Random[i] = populationRandom.get(i).getObjective(0) * -1;
			data2Random[i] = populationRandom.get(i).getObjective(1);
		}

		/*----------------------------------------------------
		 * Create Scatter Plot for all Algorithms.
		 * ---------------------------------------------------
		 **/

		/* Create List of Arrays with data */
		List<double[]> doubleArrayList = new ArrayList<>();
		doubleArrayList.add(data1NSGA);// red
		doubleArrayList.add(data2NSGA);

		doubleArrayList.add(data1ACO);// blue
		doubleArrayList.add(data2ACO);

		doubleArrayList.add(data1SA);// orange
		doubleArrayList.add(data2SA);

		doubleArrayList.add(data1TS);// magenta
		doubleArrayList.add(data2TS);

		doubleArrayList.add(data1Random);// black
		doubleArrayList.add(data2Random);

		/* Create Chart */
		List<String> nameList = Arrays.asList("NSGA-II", "ACO", "SA", "TS", "R");
		GenerateScatterPlotChart example = new GenerateScatterPlotChart("Scatter Chart", doubleArrayList, nameList);
		example.setSize(1200, 800);
		example.setLocationRelativeTo(null);
		example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		example.setVisible(true);

		long computingTime = algorithmRunner.getComputingTime();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed());
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
	}
}