package org.uma.jmetal.algorithm.multiobjective.MOSA;

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
public class MOSimulatedAnnealingBuilder<S extends Solution<?>> implements AlgorithmBuilder<MOSimulatedAnnealingAlgorithm<S>> {
	
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
	private Comparator<Double> comparator;
	private double k;
	private double initialSolutionProbability;
	
	/**
	 * Builder constructor
	 */
	public MOSimulatedAnnealingBuilder(Problem<S> problem, MutationOperator<S> mutationOperator, Comparator<Double> comparator) {
		this.problem = problem;
		this.mutationOperator = mutationOperator;
		this.evaluator = new SequentialSolutionListEvaluator<Integer>();
		this.rateOfCooling = RATE_OF_COOLING;
		this.initialTemperature = INITIAL_TEMPERATURE;
		this.minimalTemperature = MINIMAL_TEMPERATURE;
		this.k = K;
		this.comparator = comparator;
		this.initialSolutionProbability = 1;
		
	}

	public MOSimulatedAnnealingBuilder<S> setInitialTemperature(int initialTemperature) {
		this.initialTemperature = initialTemperature;
		return this;
	}
	
	public MOSimulatedAnnealingBuilder<S> setMinimalTemperature(int minimalTemperature) {
		this.minimalTemperature = minimalTemperature;;
		return this;
	}
	
	public MOSimulatedAnnealingBuilder<S> setInitialPopulationProbability(double initialPopulationProbability) {
		this.initialSolutionProbability = initialPopulationProbability;
		return this;
	}
	
	public MOSimulatedAnnealingBuilder<S> setRateOfCooling(double rateOfCooling) {
		this.rateOfCooling = rateOfCooling;
		return this;
	}
	
	public MOSimulatedAnnealingBuilder<S> setKFactor(double k) {
		this.k = k;
		return this;
	}

	public MOSimulatedAnnealingBuilder<S> setSolutionListEvaluator(SolutionListEvaluator<Integer> evaluator) {
		this.evaluator = evaluator;
		return this;
	}

	public MOSimulatedAnnealingAlgorithm<S> build() {
		return new MOSimulatedAnnealingAlgorithm<S>(problem, mutationOperator, rateOfCooling, initialTemperature,
				minimalTemperature, k, comparator, initialSolutionProbability);
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
