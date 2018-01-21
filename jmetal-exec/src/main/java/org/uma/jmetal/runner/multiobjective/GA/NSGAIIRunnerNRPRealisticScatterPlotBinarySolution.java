package org.uma.jmetal.runner.multiobjective.GA;

import java.io.IOException;
import java.util.List;

import javax.swing.WindowConstants;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.MyBitFlipMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRPRealisticMultiObjectiveBinarySolution;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.utility.GenerateScatterPlotChart;

/**
 * Class for configuring and running the NSGA-II algorithm to solve the bi-objective TSP
 *
 * @author Taras Iks
 */

public class NSGAIIRunnerNRPRealisticScatterPlotBinarySolution extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws java.io.IOException
   * @throws SecurityException
   * @throws ClassNotFoundException
   * Invoking command:
  java org.uma.jmetal.runner.multiobjective.NSGAIITSPRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws JMetalException, IOException {
    JMetalRandom.getInstance().setSeed(100L);

    Problem<BinarySolution> problem;
    Algorithm<List<BinarySolution>> algorithm;
    CrossoverOperator<BinarySolution> crossover;
    MutationOperator<BinarySolution> mutation;
    SelectionOperator<List<BinarySolution>, BinarySolution> selection;
    
    double costFactor = 0.5;
    
	problem = new NRPRealisticMultiObjectiveBinarySolution("/nrpRealisticInstances/nrp-e1.txt", costFactor);// 500(Min costs)//new
	//problem = new MultiobjectiveNRPClassic("/nrpClassicInstances/myNRP10Customers.txt", costFactor);// 500(Min costs)//new
	
	
    //crossover = new PMXCrossover(0.9) ;
	crossover = new SinglePointCrossover(0.5);// new PMXCrossover(0.9);

	AlgorithmRunner algorithmRunner = null;
    
    
	DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(0.9);// 0.9 for Zero.

    //mutation = new PermutationSwapMutation<Integer>(mutationProbability) ;

    
    selection = new BinaryTournamentSelection<BinarySolution>(new RankingAndCrowdingDistanceComparator<BinarySolution>());
/**
 * List<Double> inters = new ArrayList<>();
 inters.add(0.0);
 inters.add(0.0);
 double epsilon =0.0001;
 algorithm = new RNSGAIIBuilder<>(problem, crossover, mutation,inters,epsilon)

 */
    
    double data2[] = null, data1[] = null,data3[] = null, data4[] = null;
    double mutationProbability = 0.1;
    
    for(int ii=0; ii<2;ii++) {
    if(ii == 0) {
    	mutationProbability = 0.1;
		crossover = new SinglePointCrossover(0.1);// new PMXCrossover(0.9);
	
    }else { 
    	mutationProbability = 0.9;
    	crossover = new SinglePointCrossover(0.9);// new PMXCrossover(0.9);
    }

	mutation = new MyBitFlipMutation(mutationProbability);
    	
    algorithm = new NSGAIIBuilder<BinarySolution>(problem, crossover, mutation)
            .setSelectionOperator(selection)
            .setMaxEvaluations(2000)
            .setPopulationSize(10000)
            .build() ;

    System.out.println("start");
    algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute() ;
    
    List<BinarySolution> population = algorithm.getResult() ;


    System.out.println("Count: " + population.stream().count());
    
    int size = population.size();
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
    
    
    
    }
    /*Create Chart*/
    GenerateScatterPlotChart example = new GenerateScatterPlotChart("Scatter Chart",data1, data2, data3, data4);
    example.setSize(1200, 800);
    example.setLocationRelativeTo(null);
    example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    example.setVisible(true);
    
    
    
    long computingTime = algorithmRunner.getComputingTime() ;

//    new SolutionListOutput(population)
//            .setSeparator("\t")
//            .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
//            .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
//            .print();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed());
    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
  }
}