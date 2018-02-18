package org.uma.jmetal.algorithm.impl;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;

/**
 * Abstract class representing an evolutionary algorithm
 * 
 * @param <S>
 *            Solution
 * @param <R>
 *            Result
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es> changed by Taras
 *         Iks<ikstaras@gmail.com>
 */
@SuppressWarnings("serial")
public abstract class AbstractEvolutionaryAlgorithm<S, R> implements Algorithm<R> {
	protected List<S> population;
	protected Problem<S> problem;
	protected double initialSolutionProbability;
	
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
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(this.initialSolutionProbability);// probability for 0.		

		int objectives = problem.getNumberOfObjectives();
		List<S> offspringPopulation;
		List<S> matingPopulation;
		population = createInitialPopulation();
		population = evaluatePopulation(population);
		initProgress();

		while (!isStoppingConditionReached()) {

			matingPopulation = selection(population);
			offspringPopulation = reproduction(matingPopulation);
			offspringPopulation = evaluatePopulation(offspringPopulation);

			if (objectives == 2) {
				population = repairPopulation(population);// TODO: added, but it should not apply for all algorithms.
				offspringPopulation = repairPopulation(offspringPopulation);
			}

			population = replacement(population, offspringPopulation);
			updateProgress();
		}
	}

	public List<S> repairPopulation(List<S> population) {// TODO: move to own class
		Object o = population.get(0);

		if (o instanceof BinarySolution) {
			for (int i = 0; i < population.size(); i++) {

				BinarySolution solution = (BinarySolution) population.get(i);
				if (solution.getObjective(0) == -1 || solution.getObjective(1) == -1 || solution.getObjective(0) == 0
						|| solution.getObjective(1) == 0) {
					population.remove(i);
				}
			}
		}

		if (o instanceof PermutationSolution) {
			for (int i = 0; i < population.size(); i++) {
				PermutationSolution<Integer> solution = (PermutationSolution<Integer>) population.get(i);
				if (solution.getObjective(0) == -1 || solution.getObjective(1) == -1 || solution.getObjective(0) == 0
						|| solution.getObjective(1) == 0) {
					population.remove(i);
				}
			}
		}

		return population;
	}
}
