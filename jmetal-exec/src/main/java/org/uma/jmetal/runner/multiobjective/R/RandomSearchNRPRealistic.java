package org.uma.jmetal.runner.multiobjective.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.WindowConstants;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.randomsearch.RandomSearchBuilder;
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
 * Class to configure and run a Tabu Search algorithm. The target problem is
 * NRP/TSP.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class RandomSearchNRPRealistic {

	public static void main(String[] args) throws Exception {

		Problem<BinarySolution> problem;
		//MutationOperator<PermutationSolution<Integer>> mutation;

		Algorithm<List<BinarySolution>> algorithm;
		//double mutationProbability = 0.3;
		double costFactor = 0.5;
		
		//Initial Solution  of Tabu Search must be zero.
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(1 - ((1-costFactor)/2));//probability for 0.

		System.out.println("Solving NRP");
		problem = new NRPRealisticMultiObjectiveBinarySolution("/nrpRealisticInstances/nrp-e1.txt", costFactor);// 500(Min costs)//new
																	// NRPClassic("/nrpClassicInstances/myNRP10Customers.txt");
		//mutation = new BinaryFlipMutation<PermutationSolution<Integer>>(mutationProbability);

		algorithm = new RandomSearchBuilder<BinarySolution>(problem)//uses original random builder!!!
        .setMaxEvaluations(100000)
        .build() ;
		
		/*
		algorithm = new NRPRandomSearchBuilder<BinarySolution>(problem)
	            .setMaxEvaluations(40000)
	            .build();
	            
	    */
		//algorithm = new TabuSearchBuilder<PermutationSolution<Integer>>(problem, mutation, tabuListSize,
		//numbOfIterations, new SimpleMaxDoubleComparator(), new MaxNeighborSolutionFinder<>()).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<BinarySolution> population = algorithm.getResult(); // TODO: set ACO, SA to this single result
																		// instead of list.

		long computingTime = algorithmRunner.getComputingTime();
		
		double[] data1 = null, data2 = null, data3 = null, data4 = null;
		
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

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
