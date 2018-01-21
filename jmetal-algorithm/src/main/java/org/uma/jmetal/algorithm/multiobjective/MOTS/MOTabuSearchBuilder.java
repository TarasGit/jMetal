package org.uma.jmetal.algorithm.multiobjective.MOTS;

import java.util.Comparator;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.comparator.BestNeighborSolutionFinder;
import org.uma.jmetal.util.comparator.MONotInTabuListSolutionFinder;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 * Taras Iks.
 */
public class MOTabuSearchBuilder<S extends Solution<?>> implements AlgorithmBuilder<MOTabuSearchAlgorithm<S>> {//TODO: add Algorithm interface to all classes.

	/**
	 * Builder class
	 */
	private Problem<S> problem;
	private MutationOperator<S> mutationOperator;
	private SolutionListEvaluator<Integer> evaluator;
	private int listSize;
	private int numbOfIterations;
	private Comparator<Double> comparator;
	private MONotInTabuListSolutionFinder<S> locator;

	private S solution;

	/**
	 * Builder constructor
	 */
	public MOTabuSearchBuilder(Problem<S> problem, MutationOperator<S> mutationOperator, int listSize,
			int numbOfIterations, Comparator<Double> comparator, MONotInTabuListSolutionFinder<S> locator) {
		this.problem = problem;
		this.comparator = comparator;
		this.mutationOperator = mutationOperator;
		this.listSize = listSize;
		this.numbOfIterations = numbOfIterations;
		this.locator = locator;

		evaluator = new SequentialSolutionListEvaluator<Integer>();// TODO XXX remove it.

	}

	public MOTabuSearchBuilder<S> setTabuListSize(int tabuListSize) {
		this.listSize = tabuListSize;

		return this;
	}

	public MOTabuSearchBuilder<S> setNumberOfIterations(int numberOfIterations) {
		this.numbOfIterations = numberOfIterations;

		return this;
	}

	public MOTabuSearchBuilder<S> setSolutionListEvaluator(SolutionListEvaluator<Integer> evaluator) {
		this.evaluator = evaluator;

		return this;
	}

	public MOTabuSearchAlgorithm<S> build() {
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
	public MOTabuSearchAlgorithm<S> setupTS(Integer tabuListSize, Integer iterations,
			MutationOperator<S> mutationOperator, Comparator<Double> comparator) {
		return new MOTabuSearchAlgorithm<S>(new MOStaticTabuList<S>(tabuListSize), new MOIterationsStopCondition(iterations),
				locator, mutationOperator, solution, comparator, problem);

	}

	public void buildInstance() {
		this.solution = problem.createSolution();
	}

}
