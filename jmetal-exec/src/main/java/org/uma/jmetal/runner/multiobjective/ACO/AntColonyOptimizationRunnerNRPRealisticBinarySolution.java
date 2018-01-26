package org.uma.jmetal.runner.multiobjective.ACO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.WindowConstants;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.MOACO.MOAntColonyOptimizationBuilderNRP;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRPRealisticMultiObjectiveBinarySolution;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.utility.GenerateScatterPlotChart;

/**
 * Class to configure and run a Ant Colony Optimization algorithm. The target
 * problem is NRP.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class AntColonyOptimizationRunnerNRPRealisticBinarySolution {

	/*
	 * BUG: BETA > ALPHA & #Ants = 10-> Rank = -1 Exception.
	 * */
	public static final int 	NUMBER_OF_ANTS = 100;
	public static final double 	ALPHA = 20;// importance of pheramon trail, x >= 0, 
	public static final double 	BETA = 10;// importance between source and destination, x >= 1

	

	/* IMPORTANT
	 * Die Ameise versucht gleich den längst möglichen Pfad zu nehmen, deswegen fallen Lösugen im Unteren Bereich aus.
	 * 
	 * Q is not used, because if Q is constant, so Q/distance -> limit(0)
	 * instead a counter is used, which increases in each iteration -> couter/distance ~ [0..1]
	 * */
	public static final double 	Q = 0.0;// feramon deposited level; 
	public static final double 	RHO = 0.1;// feramon avapouration level, 0<=x<=1 -> 0.1 <= x <= 0.01 is ok.

	public static final double COST_FACTOR = 0.5;

	public static void main(String[] args) throws Exception {

		Problem<BinarySolution> problem;
		Algorithm<List<BinarySolution>> algorithm;
		double data2[] = null, data1[] = null,data3[] = null, data4[] = null;

	    data3 = new double[0];
	    data4 = new double[0];
		
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(1);// pro	bability = 1 for 0 -> zero initial solution.

		
		problem = new NRPRealisticMultiObjectiveBinarySolution("/nrpRealisticInstances/nrp-e1.txt", COST_FACTOR);// 500(Min costs)//new
		System.out.println("Number of Variables: " + problem.getNumberOfVariables());// Taras

		algorithm = new MOAntColonyOptimizationBuilderNRP<BinarySolution>(problem, NUMBER_OF_ANTS, ALPHA, BETA,
				RHO, Q).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<BinarySolution> population = algorithm.getResult();

		int size = population.size();
		System.err.println("last Size" + size);

	    int ii = 0;
		   if(ii==0) {
		    	data1 = new double[size];
		    	data2= new double[size];
		    	for(int i=0; i<size;i++) {
		        	data1[i] = population.get(i).getObjective(0);
		        	data2[i] = population.get(i).getObjective(1) * -1;
		        }
		    }else {
		    	data3 = new double[size];
		    	data4= new double[size];
		    	for(int i=0; i<size;i++) {
		        	data3[i] = population.get(i).getObjective(0);
		        	data4[i] = population.get(i).getObjective(1) * -1;
		        }
		    }
	
		   /* Create List of Arrays with data*/
		    List<double[]> doubleArrayList = new ArrayList<>();
		    doubleArrayList.add(data1);
		    doubleArrayList.add(data2);
//		    doubleArrayList.add(data3);
//		    doubleArrayList.add(data4);
 
		   
		    /*Create Chart*/
		    List<String> nameList = Arrays.asList("NSGA-II", "ACO", "SA", "TS", "R");
		    GenerateScatterPlotChart example = new GenerateScatterPlotChart("Scatter Chart", doubleArrayList, nameList);
		    example.setSize(1200, 800);
		    example.setLocationRelativeTo(null);
		    example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		    example.setVisible(true);
		
		System.out.println("End Solution: " + population.size());
		
		
		

		long computingTime = algorithmRunner.getComputingTime();

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
