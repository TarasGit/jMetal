package org.uma.jmetal.algorithm.multiobjective.MOSA;

import java.util.Comparator;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchiveForMinMax;

/**
 * @author Taras Iks <ikstaras@gmail.com>
 */
@SuppressWarnings("serial")
public class MOSimulatedAnnealingAlgorithm<S extends Solution<?>> implements Algorithm<List<S>> {

	private double rateOfCooling;
	private int initialTemperature;
	private int minimalTemperature;
	private int count = 0;

	private static final double K = 1;// TODO: move to the Builder.

	protected Problem<S> problem;
	protected MutationOperator<S> mutationOperator;
	private Comparator<Double> comparator;

	private S tmpSolution;
	private S shortestSolution;

	NonDominatedSolutionListArchiveForMinMax<S> nonDominatedArchive;

	/**
	 * Constructor
	 */
	public MOSimulatedAnnealingAlgorithm(final Problem<S> problem, final MutationOperator<S> mutationOperator,
			final double rateOfCooling, final int initialTemperature, final int minimalTemperature,
			Comparator<Double> comparator) {
		this.problem = problem;
		this.mutationOperator = mutationOperator;
		this.rateOfCooling = rateOfCooling;
		this.initialTemperature = initialTemperature;
		this.minimalTemperature = minimalTemperature;
		this.comparator = comparator;

		nonDominatedArchive = new NonDominatedSolutionListArchiveForMinMax<S>();

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
		shortestSolution = (S) currentSolution;
		double lastDistance, lastCosts;

		lastDistance = currentSolution.getObjective(0);
		lastCosts = currentSolution.getObjective(1);

		problem.evaluate(shortestSolution);
		double temperature = this.initialTemperature;

		System.out.println("Anfangsroute: " + shortestSolution.getObjective(0));
		while (temperature > minimalTemperature) {
			tmpSolution = (S) mutationOperator.execute((S) currentSolution.copy());// current

			problem.evaluate(tmpSolution);

			if (tmpSolution.getObjective(0) == -1) {
				continue;
			}

			if (lastDistance != tmpSolution.getObjective(0) || lastCosts != tmpSolution.getObjective(1)) {
				nonDominatedArchive.add(tmpSolution);
				lastDistance = tmpSolution.getObjective(0);
				lastCosts = tmpSolution.getObjective(1);

			}

			if (acceptRoute(tmpSolution.getObjective(0), shortestSolution.getObjective(0), tmpSolution.getObjective(1),
					shortestSolution.getObjective(1), temperature)) {
				shortestSolution = (S) tmpSolution;// copy???
				currentSolution = (S) tmpSolution;

				System.out.println("---------------------------------# " + shortestSolution.getObjective(0) + "| "
						+ shortestSolution.getObjective(1));

			}

			temperature *= 1 - rateOfCooling;
			System.out.println(">" + tmpSolution.getObjective(0) + " + " + currentSolution.getObjective(1));

		}

		System.out.println("Shortes tSolution Size:" + tmpSolution.getNumberOfVariables());
		problem.evaluate(shortestSolution);

		System.out.print(">Sortest Solution:");
		System.out.println(shortestSolution.getObjective(0));
		System.out.println("Count: " + count);

		return shortestSolution;
	}

	private boolean acceptRoute(double currentDistance, double shortestDistance, double currentCosts,
			double shortestCosts, double temperature) {
		double acceptanceProbability = 0;
		double delta;
		if (comparator.compare(currentDistance, shortestDistance) == 1) {
				//|| (comparator.compare(-1 * currentCosts, -1 * shortestCosts) <= 0)) {// Costs *= -1
			acceptanceProbability = 1.0;
		} else {
			delta = Math.abs(currentDistance - shortestDistance);
			acceptanceProbability = Math.exp(-(delta / (K * temperature)));

		}

		if (acceptanceProbability >= 0.8)// JMetalRandom.getInstance().nextDouble())
			return true;
		else
			return false;
	}

	@Override
	public void run() {
		findRoute(problem.createSolution());
	}

	@Override
	public List<S> getResult() {
		return SolutionListUtils.getNondominatedSolutions(nonDominatedArchive.getSolutionList());
	}
}
