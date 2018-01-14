package org.uma.jmetal.algorithm.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.PermutationSolution;

/**
 * Abstract class representing an evolutionary algorithm
 * 
 * @param <S>
 *            Solution
 * @param <R>
 *            Result
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public abstract class AbstractEvolutionaryAlgorithm<S, R> implements Algorithm<R> {
	protected List<S> population;
	protected Problem<S> problem;

	public List<S> getPopulation() {
		return population;
	}

	public void setPopulation(List<S> population) {
		this.population = population;
	}

	public void setProblem(Problem<S> problem) {
		this.problem = problem;
	}

	public Problem<S> getProblem() {
		return problem;
	}

	protected abstract void initProgress();

	protected abstract void updateProgress();

	protected abstract boolean isStoppingConditionReached();

	protected abstract List<S> createInitialPopulation();

	protected abstract List<S> evaluatePopulation(List<S> population);

	protected abstract List<S> selection(List<S> population);

	protected abstract List<S> reproduction(List<S> population);

	protected abstract List<S> replacement(List<S> population, List<S> offspringPopulation);

	@Override
	public abstract R getResult();

	@Override
	public void run() {
		List<S> offspringPopulation;
		List<S> matingPopulation;
		population = createInitialPopulation();
		population = evaluatePopulation(population);
		initProgress();
		while (!isStoppingConditionReached()) {

			System.out.println(population);
			matingPopulation = selection(population);
			offspringPopulation = reproduction(matingPopulation);
			offspringPopulation = evaluatePopulation(offspringPopulation);
			population = repairPopulation(population);
			offspringPopulation = repairPopulation(offspringPopulation);
			
			population = replacement(population, offspringPopulation);
			updateProgress();
		}
		System.out.println("End");
	}

	public List<S> repairPopulation(List<S> population) {//TODO: move to own class!
		for (int i = 0; i < population.size(); i++) {
			PermutationSolution<Integer> solution = (PermutationSolution<Integer>) population.get(i);

			if (solution.getObjective(0) == -1 || solution.getObjective(1) == -1 || solution.getObjective(0) == 0
					|| solution.getObjective(1) == 0) {
				population.remove(i);
			}
		}
		Set<S> set = new HashSet<>(population);
		List<S> list =  new ArrayList<S>(set);
		
		return list;
	}
}
