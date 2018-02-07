
package org.uma.jmetal.runner.singleobjective.GA;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipOrExchangeMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRPClassicBinarySolution;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.ObjectiveComparator.Ordering;
import org.uma.jmetal.util.comparator.SimpleMaxSolutionComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/*
 * @author Taras Iks <ikstaras@gmail.com>
 */
public class GenerationalGeneticAlgorithmRunnerNRPClassicBinarySolution {

	public static final double COST_FACTOR = 0.5;
	public static final double CROSSOVER_PROBABILITY = 0.5;
	public static final double MUTATION_PROBABILITY = 0.3;
	public static final int POPULATION_SIZE = 100;
	public static final int MAX_EVALUATIONS = 40000;

	public static void main(String[] args) throws Exception {
		Problem<BinarySolution> problem;
		Algorithm<BinarySolution> algorithm;
		CrossoverOperator<BinarySolution> crossover;
		MutationOperator<BinarySolution> mutation;
		SelectionOperator<List<BinarySolution>, BinarySolution> selection;
		Ordering ordering = Ordering.DESCENDING;

		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(0.99);// 1 for zero solution

		problem = new NRPClassicBinarySolution("/nrpClassicInstances/nrp1.txt", COST_FACTOR);

		crossover = new SinglePointCrossover(CROSSOVER_PROBABILITY);

		mutation = new BitFlipOrExchangeMutation(MUTATION_PROBABILITY);
		selection = new BinaryTournamentSelection<BinarySolution>(new SimpleMaxSolutionComparator<BinarySolution>());
		algorithm = new GeneticAlgorithmBuilder<BinarySolution>(problem, crossover, mutation, ordering)
				.setPopulationSize(POPULATION_SIZE).setMaxEvaluations(MAX_EVALUATIONS).setSelectionOperator(selection)
				.build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		BinarySolution solution = algorithm.getResult();
		long computingTime = algorithmRunner.getComputingTime();

		List<BinarySolution> population = new ArrayList<>(1);
		population.add(solution);

		System.out.println("End Result: " + solution.getObjective(0));

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
