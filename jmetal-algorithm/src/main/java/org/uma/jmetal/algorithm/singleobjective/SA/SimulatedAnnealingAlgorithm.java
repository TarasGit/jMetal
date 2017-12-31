package org.uma.jmetal.algorithm.singleobjective.SA;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * @author Taras Iks <ikstaras@gmail.com>
 */
@SuppressWarnings("serial")
public class SimulatedAnnealingAlgorithm<S extends Solution<?>> implements Algorithm<S> {

	private double rateOfCooling;
	private int initialTemperature;
	private int minimalTemperature;
	private int count = 0;

	protected Problem<S> problem;
	protected MutationOperator<S> mutationOperator;

	private S adjacentSolution;
	private S shortestSolution;

	/**
	 * Constructor
	 */
	public SimulatedAnnealingAlgorithm(final Problem<S> problem,
			final MutationOperator<S> mutationOperator, final double rateOfCooling,
			final int initialTemperature, final int minimalTemperature) {
		this.problem = problem;
		this.mutationOperator = mutationOperator;
		this.rateOfCooling = rateOfCooling;
		this.initialTemperature = initialTemperature;
		this.minimalTemperature = minimalTemperature;
	}

	@Override
	public String getName() {
		return "SA";
	}

	@Override
	public String getDescription() {
		return "Generational Genetic Algorithm";
	}

	public S findRoute(S currentSolution) {
		shortestSolution = (S) currentSolution.copy();
		problem.evaluate(shortestSolution);
		int temperature = this.initialTemperature;

		System.out.println("Anfangsroute: " + shortestSolution.getObjective(0));
		while (temperature > minimalTemperature) {
			adjacentSolution = (S) mutationOperator.execute((S) currentSolution.copy());
			problem.evaluate(adjacentSolution);
			problem.evaluate(currentSolution);
			problem.evaluate(shortestSolution);
			if (currentSolution.getObjective(0) < shortestSolution.getObjective(0))
				shortestSolution = (S) currentSolution.copy();// copy???
			if (acceptRoute(currentSolution.getObjective(0), adjacentSolution.getObjective(0), temperature))
				currentSolution = (S) adjacentSolution.copy();// copy???
			temperature *= 1 - rateOfCooling;

			count++;
		}

		System.out.println("Shortes tSolution Size:" + shortestSolution.getNumberOfVariables());
		problem.evaluate(shortestSolution);

		System.out.print(">Sortest Solution:");
		System.out.println(shortestSolution.getObjective(0));
		System.out.println("Count: " + count);

		return shortestSolution;
	}

	private boolean acceptRoute(double currentDistance, double adjacentDistance, double temperature) {
		boolean acceptRouteFlag = false;
		double acceptanceProbability = 1.0;

		if (adjacentDistance >= currentDistance) {
			acceptanceProbability = Math.exp(-(adjacentDistance - currentDistance) / temperature);
		}

		double randomNumb = JMetalRandom.getInstance().nextDouble();
		if (acceptanceProbability >= randomNumb)
			acceptRouteFlag = true;
		return acceptRouteFlag;
	}

	@Override
	public void run() {
		findRoute(problem.createSolution());
	}

	@Override
	public S getResult() {
		return this.shortestSolution;
	}
}
