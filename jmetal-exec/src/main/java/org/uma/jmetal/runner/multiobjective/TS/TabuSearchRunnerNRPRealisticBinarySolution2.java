package org.uma.jmetal.runner.multiobjective.TS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.WindowConstants;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.MOTS.MOTabuSearchBuilder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.mutation.BitFlipOrExchangeMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRPRealisticMultiObjectiveBinarySolution;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.MONotInTabuListSolutionFinder;
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
public class TabuSearchRunnerNRPRealisticBinarySolution2 {

	public static void main(String[] args) throws Exception {

		Problem<BinarySolution> problem;
		MutationOperator<BinarySolution> mutation;
	    SelectionOperator<List<BinarySolution>, BinarySolution> selection;


		Algorithm<List<BinarySolution>> algorithm;
		double mutationProbability = 0.7;
		int tabuListSize = 1000;
		int numbOfIterations = 400;
	    
		double data2[] = null, data1[] = null,data3[] = null, data4[] = null;

	    data3 = new double[0];
	    data4 = new double[0];
		
		double costFactor = 0.5;
		

		// Initial Solution of Tabu Search must be zero.
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(0.99);// probability for 0.

		System.out.println("Solving NRP");
		
	    //selection = new BinaryTournamentSelection<BinarySolution>(new RankingAndCrowdingDistanceComparator<BinarySolution>());
		problem = new NRPRealisticMultiObjectiveBinarySolution("/nrpRealisticInstances/nrp-e1.txt", costFactor);// 500(Min costs)//new
		// NRPClassic("/nrpClassicInstances/myNRP10Customers.txt");
		mutation = new BitFlipOrExchangeMutation(mutationProbability);

		algorithm = new MOTabuSearchBuilder<BinarySolution>(problem, mutation, tabuListSize,
				numbOfIterations, new SimpleMaxDoubleComparator(), new MONotInTabuListSolutionFinder<>()).build();
		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<BinarySolution> population = algorithm.getResult(); // TODO: set ACO, SA to this single
																				// result
		// instead of list.

		System.out.println("Solution:" + population.get(0));
		System.out.println("Optimal Solution: " + population.get(0).getObjective(0));

		long computingTime = algorithmRunner.getComputingTime();
		
	    int size = population.size();
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
		    //doubleArrayList.add(data3);
		    //doubleArrayList.add(data4);
		    
	
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
