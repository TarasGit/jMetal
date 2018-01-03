package org.uma.jmetal.runner.singleobjective.ACO;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.singleobjective.ACO.TSP.AntColonyOptimizationAlgorithmTSP;
import org.uma.jmetal.algorithm.singleobjective.ACO.TSP.AntColonyOptimizationBuilderTSP;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a Ant Colony Optimization algorithm. The target
 * problem is TSP.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class AntColonyOptimizationRunnerTSP {

	public static final int 	NUMBER_OF_ANTS = 4;
	public static final double 	ALPHA = 9;// importance of pheramon trail, x >= 0 TODO: what is the value of ALPHA????
	public static final double 	BETA = 9.5;// importance between source and destination, x >= 1

	public static final double 	Q = 0.0005;// feramon deposited level, 0<=x<=1
	public static final double 	RHO = 0.2;// feramon avapouration level, 0<=x<=1

	public static void main(String[] args) throws Exception {

		Problem<PermutationSolution<Integer>> problem;
		AntColonyOptimizationAlgorithmTSP<PermutationSolution<Integer>> algorithm;
		
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(1);//probability for 0.
		
		problem =  new TSP("/tspInstances/myKro11.tsp");//new TSP("/tspInstances/kroA100.tsp");

		System.out.println("Number of Variables: " + problem.getNumberOfVariables());// Taras

		algorithm = new AntColonyOptimizationBuilderTSP<PermutationSolution<Integer>>(problem, NUMBER_OF_ANTS, ALPHA, BETA,
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
