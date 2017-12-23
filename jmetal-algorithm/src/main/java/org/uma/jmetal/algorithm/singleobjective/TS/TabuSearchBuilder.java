package org.uma.jmetal.algorithm.singleobjective.TS;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
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
	private boolean MinOrMax;

	private S solution;

	/**
	 * Builder constructor
	 */
	public TabuSearchBuilder(Problem<S> problem, MutationOperator<S> mutationOperator,
			int listSize, int numbOfIterations, boolean MinOrMax) {
		this.problem = problem;
		this.MinOrMax = MinOrMax;
		this.mutationOperator = mutationOperator;
		this.listSize = listSize;
		this.numbOfIterations = numbOfIterations;

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
		return setupTS(listSize, numbOfIterations, mutationOperator, MinOrMax);
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

	public TabuSearchAlgorithm<S> setupTS(Integer tabuListSize, Integer iterations,
			MutationOperator<S> mutationOperator, boolean MinOrMax) {
		if (MinOrMax) {
			return new TabuSearchAlgorithm<S>(new StaticTabuList<S>(tabuListSize), new IterationsStopCondition(iterations),
					new MinNeighborSolutionLocator<S>(), mutationOperator, solution, problem);
		} else {

			return new TabuSearchAlgorithm<S>(new StaticTabuList<S>(tabuListSize), new IterationsStopCondition(iterations),
					new MaxNeighborSolutionLocator<S>(), mutationOperator, solution, problem);
		}
	}

	public void buildInstance() {
		this.solution =  problem.createSolution();
	}

}
