package org.uma.jmetal.algorithm.multiobjective.randomsearch;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.AlgorithmBuilder;

/**
 * This class implements a simple random search algorithm.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class RandomSearchBuilder<S extends Solution<?>> implements AlgorithmBuilder<RandomSearch<S>> {
	private Problem<S> problem;
	private int maxEvaluations;
	private double initialSolutionProbability;
	private MutationOperator<S> mutationOperator;

	/* Getter */
	public int getMaxEvaluations() {
		return maxEvaluations;
	}

	public RandomSearchBuilder(Problem<S> problem, MutationOperator<S> mutationOperator) {
		this.problem = problem;
		maxEvaluations = 25000;
		this.mutationOperator = mutationOperator; 
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(1);
	}

	public RandomSearchBuilder<S> setMaxEvaluations(int maxEvaluations) {
		this.maxEvaluations = maxEvaluations;

		return this;
	}

	public RandomSearchBuilder<S> setInitialPopulationProbability(double initialPopulationProbability) {
		this.initialSolutionProbability = initialPopulationProbability;
		return this;
	}

	public RandomSearch<S> build() {
		return new RandomSearch<S>(problem, maxEvaluations, initialSolutionProbability, mutationOperator);
	}
}
