package org.uma.jmetal.algorithm.singleobjective.SA;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * @author Taras Iks <ikstaras@gmail.com>
 */
@SuppressWarnings("serial")
public class SimulatedAnnealingAlgorithm<S extends Solution<?>> implements Algorithm<List<S>> {

	public static final boolean D = true; //Debug.
	private double rateOfCooling;
	private int initialTemperature;
	private int minimalTemperature;
	private int count = 0;
	private double k;

	private Problem<S> problem;
	private MutationOperator<S> mutationOperator;
	private Comparator<Double> comparator;

	private S adjacentSolution;
	private S shortestSolution;

	/**
	 * Constructor
	 */
	public SimulatedAnnealingAlgorithm(final Problem<S> problem, final MutationOperator<S> mutationOperator,
			final double rateOfCooling, final int initialTemperature, final int minimalTemperature, final double k,
			Comparator<Double> comparator) {
		this.problem = problem;
		this.mutationOperator = mutationOperator;
		this.rateOfCooling = rateOfCooling;
		this.initialTemperature = initialTemperature;
		this.minimalTemperature = minimalTemperature;
		this.k = k;
		this.comparator = comparator;
	}

	public S findSolution(S currentSolution) {
		shortestSolution = (S) currentSolution.copy();
		problem.evaluate(shortestSolution);
		double temperature = this.initialTemperature;

		System.out.println("Anfangsroute: " + shortestSolution.getObjective(0));
		while (temperature > minimalTemperature) {
			adjacentSolution = (S) mutationOperator.execute((S) currentSolution.copy());

			problem.evaluate(adjacentSolution);
			problem.evaluate(currentSolution);
			problem.evaluate(shortestSolution);

			if (adjacentSolution.getObjective(0) == -1) {
				continue;
			}

			if (comparator.compare(currentSolution.getObjective(0), shortestSolution.getObjective(0)) == 1)
				shortestSolution = (S) currentSolution.copy();
			if (acceptSolution(currentSolution.getObjective(0), adjacentSolution.getObjective(0), temperature))
				currentSolution = (S) adjacentSolution.copy();
			temperature *= 1 - rateOfCooling;
			if (D) {
				System.out.println(">" + shortestSolution.getObjective(0) + " + " + shortestSolution.getAttribute(0));
			}
			count++;
		}

		if (D) {
			System.out.println("Shortes tSolution Size:" + shortestSolution.getNumberOfVariables());
			problem.evaluate(shortestSolution);

			System.out.print(">Sortest Solution:");
			System.out.println(shortestSolution.getObjective(0));
			System.out.println("Count: " + count);
		}
		
		return shortestSolution;
	}

	private boolean acceptSolution(double currentDistance, double adjacentDistance, double temperature) {
		double acceptanceProbability;
		double delta;
		if (comparator.compare(currentDistance, adjacentDistance) == 1) {
			delta = Math.abs(adjacentDistance - currentDistance);
			acceptanceProbability = Math.exp(-(delta / (k * temperature)));
		} else {
			acceptanceProbability = 1.0;
		}

		if (acceptanceProbability >= JMetalRandom.getInstance().nextDouble())
			return true;
		else
			return false;
	}

	@Override
	public void run() {
		findSolution(problem.createSolution());
	}

	@Override
	public List<S> getResult() {
		return Arrays.asList(this.shortestSolution);
	}

	@Override
	public String getName() {
		return "SA";
	}

	@Override
	public String getDescription() {
		return "Generational Genetic Algorithm";
	}

}
