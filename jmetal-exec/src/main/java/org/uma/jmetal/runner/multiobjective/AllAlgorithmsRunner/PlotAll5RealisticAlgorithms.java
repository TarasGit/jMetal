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
	/**
	 * @param args
	 *            Command line arguments.
	 * @throws java.io.IOException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 *             Invoking command: java
	 *             org.uma.jmetal.runner.multiobjective.NSGAIITSPRunner problemName
	 *             [referenceFront]
	 */
	public static void main(String[] args) throws JMetalException, IOException {

		/*---------------------------------------------------
		 * NSGA-II
		 * --------------------------------------------------
		 **/

		JMetalRandom.getInstance().setSeed(100L);

		Problem<BinarySolution> problem;
		Algorithm<List<BinarySolution>> algorithmNSGA;
		CrossoverOperator<BinarySolution> crossoverNSGA;
		MutationOperator<BinarySolution> mutationNSGA;
		SelectionOperator<List<BinarySolution>, BinarySolution> selectionNSGA;

		double costFactor = 0.5;

		problem = new NRPRealisticMultiObjectiveBinarySolution("/nrpRealisticInstances/nrp-e1.txt", costFactor);
		crossoverNSGA = new SinglePointCrossover(0.5);// new PMXCrossover(0.9);

		AlgorithmRunner algorithmRunner = null;

		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(0.99);// 0.9 for Zero.

		// mutation = new PermutationSwapMutation<Integer>(mutationProbability) ;

		selectionNSGA = new BinaryTournamentSelection<BinarySolution>(
				new RankingAndCrowdingDistanceComparator<BinarySolution>());
		/**
		 * List<Double> inters = new ArrayList<>(); inters.add(0.0); inters.add(0.0);
		 * double epsilon =0.0001; algorithm = new RNSGAIIBuilder<>(problem, crossover,
		 * mutation,inters,epsilon)
		 * 
		 */

		double data1NSGA[] = null, data2NSGA[] = null;
		double mutationProbabilityNSGA = 0.5;

		mutationNSGA = new BitFlipOrExchangeMutation(mutationProbabilityNSGA);

		/*
		 * // nrp-e1 -  300.000  |  nrp-e2 - 200.000  | 
		 */
		algorithmNSGA = new NSGAIIBuilder<BinarySolution>(problem, crossoverNSGA, mutationNSGA)
				.setSelectionOperator(selectionNSGA).setMaxEvaluations(300000).setPopulationSize(500).build();

		System.out.println("start");
		algorithmRunner = new AlgorithmRunner.Executor(algorithmNSGA).execute();

		List<BinarySolution> populationNSGA = algorithmNSGA.getResult();

		System.out.println("end: " + populationNSGA);

		System.out.println("NSGA-II - finished");

		int sizeNSGA = populationNSGA.size();
		data1NSGA = new double[sizeNSGA];
		data2NSGA = new double[sizeNSGA];
		for (int i = 0; i < sizeNSGA; i++) {
			data1NSGA[i] = populationNSGA.get(i).getObjective(0);
			data2NSGA[i] = populationNSGA.get(i).getObjective(1) * -1;
		}

		/*----------------------------------------------------
		 * Ant Colony Optimization
		 *----------------------------------------------------
		 */

		int NUMBER_OF_ANTS = 200;
		double ALPHA = 10;// importance of pheramon trail, x >= 0,
		double BETA = 2;// importance between source and destination, x >= 1

		double Q = 0.0;// feramon deposited level;
		double RHO = 0.1;// feramon avapouration level, 0<=x<=1 -> 0.1 <= x <= 0.01 is ok.

		double COST_FACTOR = 0.5;

		Problem<BinarySolution> problemACO;
		Algorithm<List<BinarySolution>> algorithmACO;
		double data1ACO[] = null, data2ACO[] = null;

		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(1);// pro bability = 1 for 0
																								// -> zero initial
																								// solution.
		
		System.out.println("Number of Variables: " + problem.getNumberOfVariables());// Taras

		algorithmACO = new MOAntColonyOptimizationBuilderNRP<BinarySolution>(problem, NUMBER_OF_ANTS, ALPHA, BETA, RHO,
				Q).build();

		AlgorithmRunner algorithmRunnerACO = new AlgorithmRunner.Executor(algorithmACO).execute();

		List<BinarySolution> populationACO = algorithmACO.getResult();

		int sizeACO = populationACO.size();
		data1ACO = new double[sizeACO];
		data2ACO = new double[sizeACO];
		for (int i = 0; i < sizeACO; i++) {
			data1ACO[i] = populationACO.get(i).getObjective(0);
			data2ACO[i] = populationACO.get(i).getObjective(1) * -1;
		}

		/*--------------------------------------
		 * Simulated Annealing
		 * -------------------------------------
		 */

		double RATE_OF_COOLING = 0.01;// 0.001 - nrp-e1 | 0.01 - rnp-e2 |
		int INITIAL_TEMPERATURE = 8000;// 8000 - erp-e1 | 8000 - nrp-e2 |
		int MINIMAL_TEMPERATURE = 1;

		MutationOperator<BinarySolution> mutationSA;
		SelectionOperator<List<BinarySolution>, BinarySolution> selectionSA;

		Algorithm<List<BinarySolution>> algorithmSA;
		double mutationProbabilitySA = 0.6;// IMPORTANT for MOTS is mutation probability < 0.1 the best option.

		double data1SA[] = null, data2SA[] = null;

		// Initial Solution of Tabu Search must be zero.
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(1);// probability for 0.

		System.out.println("Solving NRP");

		selectionSA = new BinaryTournamentSelection<BinarySolution>(
				new RankingAndCrowdingDistanceComparator<BinarySolution>());
		// costs)//new
		// NRPClassic("/nrpClassicInstances/myNRP10Customers.txt");
		mutationSA = new BitFlipOrExchangeMutation(mutationProbabilitySA);

		algorithmSA = new MOSimulatedAnnealingBuilder<BinarySolution>(problem, mutationSA,
				new SimpleMaxDoubleComparator()).setMinimalTemperature(MINIMAL_TEMPERATURE)
						.setInitialTemperature(INITIAL_TEMPERATURE).setRateOfCooling(RATE_OF_COOLING).build();

		AlgorithmRunner algorithmRunnerSA = new AlgorithmRunner.Executor(algorithmSA).execute();

		List<BinarySolution> populationSA = algorithmSA.getResult(); // TODO: set ACO, SA to this single

		int sizeSA = populationSA.size();
		data1SA = new double[sizeSA];
		data2SA = new double[sizeSA];
		for (int i = 0; i < sizeSA; i++) {
			data1SA[i] = populationSA.get(i).getObjective(0);
			data2SA[i] = populationSA.get(i).getObjective(1) * -1;
		}

		/*------------------------
		 *  Tabu Search
		 * -----------------------
		 * */

		MutationOperator<BinarySolution> mutationTS;
		SelectionOperator<List<BinarySolution>, BinarySolution> selectionTS;

		Algorithm<List<BinarySolution>> algorithmTS;
		double mutationProbabilityTS = 0.7;
		int tabuListSize = 1000;
		int numberOfNeighbors = 100;
		int numbOfIterations = 1000; // 1000 - nrp-e1 |

		double data1TS[] = null, data2TS[] = null;

		// Initial Solution of Tabu Search must be zero.
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(0.99);// probability for 0.

		System.out.println("Solving NRP");

		// costs)//new
		// NRPClassic("/nrpClassicInstances/myNRP10Customers.txt");
		mutationTS = new BitFlipOrExchangeMutation(mutationProbabilityTS);

		algorithmTS = new MOTabuSearchBuilder<BinarySolution>(problem, mutationTS, tabuListSize, numbOfIterations,
				numberOfNeighbors, new MONotInTabuListSolutionFinder<>()).build();
		AlgorithmRunner algorithmRunnerTS = new AlgorithmRunner.Executor(algorithmTS).execute();

		List<BinarySolution> populationTS = algorithmTS.getResult(); // TODO: set ACO, SA to this single

		int sizeTS = populationTS.size();
		data1TS = new double[sizeTS];
		data2TS = new double[sizeTS];
		for (int i = 0; i < sizeTS; i++) {
			data1TS[i] = populationTS.get(i).getObjective(0);
			data2TS[i] = populationTS.get(i).getObjective(1) * -1;
		}

		/*-------------------------------------------------
		 * Random
		 * -------------------------------------------------
		 * */

		Algorithm<List<BinarySolution>> algorithmRandom;

		// Initial Solution of Tabu Search must be zero.
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(0.9);// probability for 0.

		algorithmRandom = new RandomSearchBuilder<BinarySolution>(problem).setMaxEvaluations(50000).build();

		AlgorithmRunner algorithmRunnerRandom = new AlgorithmRunner.Executor(algorithmRandom).execute();

		List<BinarySolution> populationRandom = algorithmRandom.getResult(); // TODO: set ACO, SA to this single result
		// instead of list.

		long computingTimeRandom = algorithmRunnerRandom.getComputingTime();

		double[] data1Random = null, data2Random = null;

		int sizeRandom = populationRandom.size();
		System.err.println("last Size" + sizeRandom);

		data1Random = new double[sizeRandom];
		data2Random = new double[sizeRandom];
		for (int i = 0; i < sizeRandom; i++) {
			data1Random[i] = populationRandom.get(i).getObjective(0);
			data2Random[i] = populationRandom.get(i).getObjective(1) * -1;
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

		doubleArrayList.add(data1SA);// black
		doubleArrayList.add(data2SA);

		doubleArrayList.add(data1TS);// magenta
		doubleArrayList.add(data2TS);

		doubleArrayList.add(data1Random);// yellow
		doubleArrayList.add(data2Random);

		/* Create Chart */
		List<String> nameList = Arrays.asList("NSGA-II", "ACO", "SA", "TS", "R");
		GenerateScatterPlotChart example = new GenerateScatterPlotChart("Scatter Chart", doubleArrayList, nameList);
		example.setSize(1200, 800);
		example.setLocationRelativeTo(null);
		example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		example.setVisible(true);

		long computingTime = algorithmRunner.getComputingTime();

		// new SolutionListOutput(population)
		// .setSeparator("\t")
		// .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
		// .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
		// .print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed());
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
	}
}