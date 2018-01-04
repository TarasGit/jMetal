package org.uma.jmetal.runner.singleobjective.ACO;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.singleobjective.ACO.NRP.AntColonyOptimizationAlgorithmNRP;
import org.uma.jmetal.algorithm.singleobjective.ACO.NRP.AntColonyOptimizationBuilderNRP;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRPClassic;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a Ant Colony Optimization algorithm. The target
 * problem is NRP.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class AntColonyOptimizationRunnerNRPClassic {

	public static final int 	NUMBER_OF_ANTS = 10000;
	public static final double 	ALPHA = 5;// importance of pheramon trail, x >= 0, 
	public static final double 	BETA = 1;// importance between source and destination, x >= 1

	public static final double 	Q = 0.03;// feramon deposited level, 0<=x<=1
	public static final double 	RHO = 0.02;// feramon avapouration level, 0<=x<=1
	public static final double COST_FACTOR = 0.7;

	public static void main(String[] args) throws Exception {

		Problem<PermutationSolution<Integer>> problem;
		AntColonyOptimizationAlgorithmNRP<PermutationSolution<Integer>> algorithm;
		
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(1);// probability = 1 for 0 -> zero initial solution.

		problem =  new NRPClassic("/nrpClassicInstances/nrp1.txt", COST_FACTOR);//new NRPClassic("/nrpClassicInstances/myNRP10Customers.txt", COST_FACTOR); //500(Min costs)//new

		System.out.println("Number of Variables: " + problem.getNumberOfVariables());// Taras

		algorithm = new AntColonyOptimizationBuilderNRP<PermutationSolution<Integer>>(problem, NUMBER_OF_ANTS, ALPHA, BETA,
				RHO, Q).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		PermutationSolution<Integer> solution = algorithm.getResult();

		List<PermutationSolution<Integer>> population = new ArrayList<>(1);
		population.add(solution);

		System.out.println("End Solution: " + solution);

		long computingTime = algorithmRunner.getComputingTime();

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
