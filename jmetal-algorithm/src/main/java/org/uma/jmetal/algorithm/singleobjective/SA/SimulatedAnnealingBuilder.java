package org.uma.jmetal.algorithm.singleobjective.SA;

import java.util.Comparator;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 * Created by Taras Iks.
 */
public class SimulatedAnnealingBuilder<S extends Solution<?>>
		implements AlgorithmBuilder<SimulatedAnnealingAlgorithm<S>> {

	public static final double RATE_OF_COOLING = 0.01;
	public static final int INITIAL_TEMPERATURE = 1000;
	public static final int MINIMAL_TEMPERATURE = 0;
	public static final double K = 1;
	
	private Problem<S> problem;
	private MutationOperator<S> mutationOperator;
	private SolutionListEvaluator<Integer> evaluator;
	private double rateOfCooling;
	private int initialTemperature;
	private int minimalTemperature;
	private double k;
	private Comparator<Double> comparator;

	/**
	 * Builder constructor
	 */
	public SimulatedAnnealingBuilder(Problem<S> problem, MutationOperator<S> mutationOperator,
			Comparator<Double> comparator) {
		this.problem = problem;
		this.mutationOperator = mutationOperator;
		this.evaluator = new SequentialSolutionListEvaluator<Integer>();
		this.rateOfCooling = RATE_OF_COOLING;
		this.initialTemperature = INITIAL_TEMPERATURE;
		this.minimalTemperature = MINIMAL_TEMPERATURE;
		this.k = K;
		this.comparator = comparator;

	}

	public SimulatedAnnealingBuilder<S> setInitialTemperature(int initialTemperature) {
		this.initialTemperature = initialTemperature;
		return this;
	}
	
	public SimulatedAnnealingBuilder<S> setKFactor(double k) {
		this.k = k;
		return this;
	}

	public SimulatedAnnealingBuilder<S> setMinimalTemperature(int minimalTemperature) {
		this.minimalTemperature = minimalTemperature;
		;
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
				minimalTemperature, k, comparator);
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
