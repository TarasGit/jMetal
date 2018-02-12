package org.uma.jmetal.runner.multiobjective.SA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.WindowConstants;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.MOSA.MOSimulatedAnnealingBuilder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.BitFlipOrExchangeMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRPClassicMultiObjectiveBinarySolution;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.SimpleMaxDoubleComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.utility.GenerateScatterPlotChart;

/**
 * Class to configure and run a Tabu Search algorithm. The target problem is
 * NRP/TSP.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class SimulatedAnnealingRunnerNRPClassic {

	public static final double RATE_OF_COOLING = 0.00001;
	/*
	 * IMPORTANT: don't increase the temperature, because the formulate for SA
	 * depends on it, and for very high temperatures the most of the time you will
	 * get the acceptance probability equal to 1.
	 * 
	 * SOLUTION: change RATE OF COOLING to be smaller to get better solution quality
	 * OR multiply temperature in SA with some factor, to be able to use another
	 * temperatures.
	 */
	public static final int INITIAL_TEMPERATURE = 10000;
	public static final int MINIMAL_TEMPERATURE = 1;
	public static final double K = 1;
	public static final double MUTATION_PROBABILITY = 0.7;
	
	public static final double COST_FACTOR = 0.5;

	public static void main(String[] args) throws Exception {

		Problem<BinarySolution> problem;
		MutationOperator<BinarySolution> mutation;
		Algorithm<List<BinarySolution>> algorithm;
		double data2[] = null, data1[] = null;

		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(1);// probability for 0.

		problem = new NRPClassicMultiObjectiveBinarySolution("/nrpClassicInstances/nrp1.txt", COST_FACTOR);
		mutation = new BitFlipOrExchangeMutation(MUTATION_PROBABILITY);

		algorithm = new MOSimulatedAnnealingBuilder<BinarySolution>(problem, mutation, new SimpleMaxDoubleComparator())
				.setMinimalTemperature(MINIMAL_TEMPERATURE).setInitialTemperature(INITIAL_TEMPERATURE)
				.setRateOfCooling(RATE_OF_COOLING).setKFactor(K).build();

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
		List<String> nameList = Arrays.asList("SA");
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
