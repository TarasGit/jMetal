package org.uma.jmetal.algorithm.singleobjective.Random;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

/**
 * This class implements a simple random search algorithm.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
@SuppressWarnings("serial")
public class NRPRandomSearch<S extends Solution<?>> implements Algorithm<S> {
	private Problem<S> problem;
	private int maxEvaluations;
	private S result;

	/** Constructor */
	public NRPRandomSearch(Problem<S> problem, int maxEvaluations) {
		this.problem = problem;
		this.maxEvaluations = maxEvaluations;
	}

	/* Getter */
	public int getMaxEvaluations() {
		return maxEvaluations;
	}

	@Override
	public void run() {
		S newSolution;
		result = problem.createSolution();
		problem.evaluate(result);
		System.out.println("Max Iterations: " + maxEvaluations);
		for (int i = 0; i < maxEvaluations; i++) {
			newSolution = problem.createSolution();
			problem.evaluate(newSolution);
			if(newSolution.getObjective(0) > result.getObjective(0)) {
				result = (S) newSolution.copy();
				System.out.println(result.getObjective(0) + " / " + result.getAttribute(0));
			}
		}
	}

	@Override
	public S getResult() {
		return result;
	}

	@Override
	public String getName() {
		return "RS";
	}

	@Override
	public String getDescription() {
		return "Multi-objective random search algorithm";
	}
}
