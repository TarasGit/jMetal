package org.uma.jmetal.algorithm.singleobjective.TS;

import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.PermutationSolution;
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
	private PermutationSwapMutation<Integer> mutationOperator;
	private SolutionListEvaluator<Integer> evaluator;
	private int listSize; 
	private int numbOfIterations;
	
	private PermutationSolution<Integer> solution;


	/**
	 * Builder constructor
	 */
	public TabuSearchBuilder(Problem<S> problem, PermutationSwapMutation<Integer> mutationOperator, int listSize, int numbOfIterations) {
		this.problem = problem;

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
		return setupTS(listSize, numbOfIterations, mutationOperator);
	}

	/*
	 * Getters
	 */
	public Problem<S> getProblem() {
		return problem;
	}


	public PermutationSwapMutation<Integer> getMutationOperator() {
		return mutationOperator;
	}

	public SolutionListEvaluator<Integer> getEvaluator() {
		return evaluator;
	}

	public TabuSearchAlgorithm<S> setupTS(Integer tabuListSize, Integer iterations, PermutationSwapMutation<Integer> mutationOperator) {
		return new TabuSearchAlgorithm<S>(new StaticTabuList(tabuListSize), new IterationsStopCondition(iterations),
				new BasicNeighborSolutionLocator(),  mutationOperator, solution, problem);
	}

	public void buildInstance() {
		this.solution = (PermutationSolution<Integer>)problem.createSolution();
	}

}
