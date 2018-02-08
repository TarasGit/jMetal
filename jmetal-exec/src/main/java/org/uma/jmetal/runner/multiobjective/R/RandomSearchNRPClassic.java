package org.uma.jmetal.runner.multiobjective.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.WindowConstants;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.randomsearch.RandomSearchBuilder;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRPClassicMultiObjectiveBinarySolution;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.utility.GenerateScatterPlotChart;

/**
 * Class to configure and run a Tabu Search algorithm. The target problem is
 * NRP.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class RandomSearchNRPClassic {

	public static final double COST_FACTOR = 0.5;
	public static final int MAX_EVALUATIONS = 100000;

	public static void main(String[] args) throws Exception {

		Problem<BinarySolution> problem;
		Algorithm<List<BinarySolution>> algorithm;
		double[] data1 = null, data2 = null;

		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(1 - ((1 - COST_FACTOR) / 2));

		problem = new NRPClassicMultiObjectiveBinarySolution("/nrpClassicInstances/nrp1.txt", COST_FACTOR);
		algorithm = new RandomSearchBuilder<BinarySolution>(problem).setMaxEvaluations(MAX_EVALUATIONS).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<BinarySolution> population = algorithm.getResult();
		long computingTime = algorithmRunner.getComputingTime();
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
		List<String> nameList = Arrays.asList("R");
		GenerateScatterPlotChart example = new GenerateScatterPlotChart("Scatter Chart", doubleArrayList, nameList);
		example.setSize(1200, 800);
		example.setLocationRelativeTo(null);
		example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		example.setVisible(true);

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
