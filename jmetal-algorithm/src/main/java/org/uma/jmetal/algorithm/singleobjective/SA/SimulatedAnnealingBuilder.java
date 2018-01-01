package org.uma.jmetal.algorithm.singleobjective.SA;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 * Created by Taras Iks.
 */
public class SimulatedAnnealingBuilder<S extends Solution<?>> {
	
	public static final double RATE_OF_COOLING = 0.01;
	public static final int INITIAL_TEMPERATURE = 1000;
	public static final int MINIMAL_TEMPERATURE = 0;
	
	private Problem<S> problem;
	private MutationOperator<S> mutationOperator;
	private SolutionListEvaluator<Integer> evaluator;
	private double rateOfCooling;
	private int initialTemperature;
	private int minimalTemperature;

	/**
	 * Builder constructor
	 */
	public SimulatedAnnealingBuilder(Problem<S> problem, MutationOperator<S> mutationOperator) {
		this.problem = problem;
		this.mutationOperator = mutationOperator;
		this.evaluator = new SequentialSolutionListEvaluator<Integer>();// TODO XXX remove it.
		this.rateOfCooling = RATE_OF_COOLING;
		this.initialTemperature = INITIAL_TEMPERATURE;
		this.minimalTemperature = MINIMAL_TEMPERATURE;
	}

	public SimulatedAnnealingBuilder<S> setInitialTemperature(int initialTemperature) {
		this.initialTemperature = initialTemperature;
		return this;
	}
	
	public SimulatedAnnealingBuilder<S> setMinimalTemperature(int minimalTemperature) {
		this.minimalTemperature = minimalTemperature;;
		return this;
	}
	
	public SimulatedAnnealingBuilder<S> setRateOfCooling(double rateOfCooling) {
		this.rateOfCooling = rateOfCooling;
		return this;
	}

	public SimulatedAnnealingBuilder<S> setSolutionListEvaluator(SolutionListEvaluator<Integer> evaluator) {
		this.evaluator = evaluator;
		return this;
	}

	public SimulatedAnnealingAlgorithm<S> build() {
		return new SimulatedAnnealingAlgorithm<S>(problem, mutationOperator, rateOfCooling, initialTemperature,
				minimalTemperature);
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

}
