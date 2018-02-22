package org.uma.jmetal.runner.multiobjective.ACO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.WindowConstants;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.MOACO.MOAntColonyOptimizationBuilderNRP;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRPRealisticMultiObjectiveBinarySolution;
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.SetCoverage;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.utility.GenerateScatterPlotChart;

/**
 * Class to configure and run a Ant Colony Optimization algorithm. The target
 * problem is NRP.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class AntColonyOptimizationRunnerNRPRealistic {

	/*
	 * TODO: BUG: BETA > ALPHA & #Ants = 10-> Rank = -1 Exception.
	 */
	public static final boolean METRICS = true;

	public static final int NUMBER_OF_ANTS = 2000;
	public static final double ALPHA = 10;// importance of pheromone trail, x >= 0,
	public static final double BETA = 10;// importance between source and destination, x >= 1
	public static final double Q = 9000;// pheromone deposited level;
	public static final double RHO = 0.1;// pheromone evaporation level, 0<=x<=1 -> 0.1 <= x <= 0.01 is ok.

	public static final double INITIAL_SOLUTION_PROBABILITY = 1;

	public static final double COST_FACTOR = 0.5;

	public static void main(String[] args) throws Exception {

		Problem<BinarySolution> problem;
		Algorithm<List<BinarySolution>> algorithm;
		double data2[] = null, data1[] = null;

		problem = new NRPRealisticMultiObjectiveBinarySolution("/nrpRealisticInstances/nrp-e1.txt", COST_FACTOR);
		System.out.println("Number of Variables: " + problem.getNumberOfVariables());

		algorithm = new MOAntColonyOptimizationBuilderNRP<BinarySolution>(problem, NUMBER_OF_ANTS, ALPHA, BETA, RHO, Q,
				INITIAL_SOLUTION_PROBABILITY).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
		List<BinarySolution> population = algorithm.getResult();

		long computingTime = algorithmRunner.getComputingTime();

		int size = population.size();
		System.out.println("End Solution: " + population.size());

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
		List<String> nameList = Arrays.asList("ACO");
		GenerateScatterPlotChart example = new GenerateScatterPlotChart("Scatter Chart", doubleArrayList, nameList);
		example.setSize(1200, 800);
		example.setLocationRelativeTo(null);
		example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		example.setVisible(true);

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
		}

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
	}
}
