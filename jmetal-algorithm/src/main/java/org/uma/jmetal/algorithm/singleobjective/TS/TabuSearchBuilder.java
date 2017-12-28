package org.uma.jmetal.algorithm.singleobjective.TS;

import java.util.Comparator;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.BestNeighborSolutionFinder;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 * Taras Iks.
 */
public class TabuSearchBuilder<S extends Solution<?>> {

	/**
	 * Builder class
	 */
	private Problem<S> problem;
	private MutationOperator<S> mutationOperator;
	private SolutionListEvaluator<Integer> evaluator;
	private int listSize;
	private int numbOfIterations;
	private Comparator<Double> comparator;
	private BestNeighborSolutionFinder<S> locator;

	private S solution;

	/**
	 * Builder constructor
	 */
	public TabuSearchBuilder(Problem<S> problem, MutationOperator<S> mutationOperator, int listSize,
			int numbOfIterations, Comparator<Double> comparator, BestNeighborSolutionFinder<S> locator) {
		this.problem = problem;
		this.comparator = comparator;
		this.mutationOperator = mutationOperator;
		this.listSize = listSize;
		this.numbOfIterations = numbOfIterations;
		this.locator = locator;

		evaluator = new SequentialSolutionListEvaluator<Integer>();// TODO XXX remove it.

	}

	public TabuSearchBuilder<S> setTabuListSize(int tabuListSize) {
		this.listSize = tabuListSize;

		return this;
	}

	public TabuSearchBuilder<S> setNumberOfIterations(int numberOfIterations) {
		this.numbOfIterations = numberOfIterations;

		return this;
	}

	public TabuSearchBuilder<S> setSolutionListEvaluator(SolutionListEvaluator<Integer> evaluator) {
		this.evaluator = evaluator;

		return this;
	}

	public TabuSearchAlgorithm<S> build() {
		buildInstance();
		return setupTS(listSize, numbOfIterations, mutationOperator, comparator);
	}

	/*
	 * Getters
	 */
	public Problem<S> getProblem() {
		return problem;
	}

	public MutationOperator<S> getMutationOperator() {
		return mutationOperator;
	}

	public SolutionListEvaluator<Integer> getEvaluator() {
		return evaluator;
	}

	// Min for TSP, Max for NRP.
	public TabuSearchAlgorithm<S> setupTS(Integer tabuListSize, Integer iterations,
			MutationOperator<S> mutationOperator, Comparator<Double> comparator) {
		return new TabuSearchAlgorithm<S>(new StaticTabuList<S>(tabuListSize), new IterationsStopCondition(iterations),
				locator, mutationOperator, solution, comparator, problem);

	}

	public void buildInstance() {
		this.solution = problem.createSolution();
	}

}
