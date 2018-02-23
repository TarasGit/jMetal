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
import org.uma.jmetal.problem.singleobjective.NRPClassicMultiObjectiveBinarySolution;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.BinarySolution;
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

public class PlotAll5ClassicAlgorithms extends AbstractAlgorithmRunner {

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

		double INITIAL_SOLUTION_PROBABILITY_GA = 0.99;

		problem = new NRPClassicMultiObjectiveBinarySolution("/nrpClassicInstances/nrp1.txt", costFactor);

		crossoverNSGA = new SinglePointCrossover(0.5);
		AlgorithmRunner algorithmRunner = null;

		selectionNSGA = new BinaryTournamentSelection<BinarySolution>(
				new RankingAndCrowdingDistanceComparator<BinarySolution>());
		double data1NSGA[] = null, data2NSGA[] = null;
		double mutationProbabilityNSGA = 0.1;

		mutationNSGA = new BitFlipOrExchangeMutation(mutationProbabilityNSGA);
		algorithmNSGA = new NSGAIIBuilder<BinarySolution>(problem, crossoverNSGA, mutationNSGA)
				.setSelectionOperator(selectionNSGA).setMaxEvaluations(250000).setPopulationSize(300)
				.setInitialPopulationProbability(INITIAL_SOLUTION_PROBABILITY_GA).build();// nrp1 - 300.000 | nrp2
																							// -200.000 | nrp4 - 300.000
																							// | nrp5 - 400.000 |
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
		int NUMBER_OF_ANTS = 10000;// nrp1 - 4000 | nrp2 - 2000 | nrp4 - 250 | nrp5 - 20.
		double ALPHA = 2;// importance of pheramon trail, x >= 0,
		double BETA = 2;// importance between source and destination, x >= 1

		double Q = 7600; //nrp1: 1800; // feramon deposited level;
		double RHO = 0.1;// feramon avapouration level, 0<=x<=1 -> 0.1 <= x <= 0.01 is ok.

		double INITIAL_SOLUTION_PROBABILITY_ACO = 1;

		Algorithm<List<BinarySolution>> algorithmACO;
		double data1ACO[] = null, data2ACO[] = null;

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

		/*----------------------------------
		 * Simulated Annealing
		 * ----------------------------------
		 */
		double RATE_OF_COOLING = 0.00005;// nrp1 - 0.0001, 1.000 | nrp2 - 0.001, 10.000 | nrp4 - 0.01 |
		int INITIAL_TEMPERATURE = 1000;
		int MINIMAL_TEMPERATURE = 5;

		MutationOperator<BinarySolution> mutationSA;
		Algorithm<List<BinarySolution>> algorithmSA;
		double mutationProbabilitySA = 0.99;
		double data1SA[] = null, data2SA[] = null;

		double INITIAL_SOLUTION_PROBABILITY_SA = 1;

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
		double mutationProbabilityTS = 0.9;
		int tabuListSize = 200;
		int numberOfNeighbors = 200;
		int numbOfIterations = 1000; // nrp1 - 2500 | nrp2 - 2000 | nrp4 - 1000 |
		double data1TS[] = null, data2TS[] = null;

		double INITIAL_SOLUTION_PROBABILITY_TS = 1;

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
			data1TS[i] = populationTS.get(i).getObjective(0) * -1; // TODO: write converter from Max/Max to Min/Min
			data2TS[i] = populationTS.get(i).getObjective(1);
		}

		/*-------------------------------------------------
		 * Random
		 * -------------------------------------------------
		 * */
		Algorithm<List<BinarySolution>> algorithmRandom;

		double INITIAL_SOLUTION_PROBABILITY_R = 1; //for 0
		MutationOperator<BinarySolution> mutation;

		mutation = new BitFlipOrExchangeMutation(0.99);//0 for flip mutation


		algorithmRandom = new RandomSearchBuilder<BinarySolution>(problem, mutation).setMaxEvaluations(100000)
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

		doubleArrayList.add(data1SA);// white
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