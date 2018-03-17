package org.uma.jmetal.algorithm.multiobjective.MOSA;

import java.util.Comparator;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchiveForMinMax;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * @author Taras Iks <ikstaras@gmail.com>
 */
@SuppressWarnings("serial")
public class MOSimulatedAnnealingAlgorithm<S extends Solution<?>> implements Algorithm<List<S>> {

	public static final boolean D = true; // Debug.
	private double rateOfCooling;
	private int initialTemperature;
	private int minimalTemperature;
	private int count = 0;
	private double k;

	protected Problem<S> problem;
	protected MutationOperator<S> mutationOperator;
	private Comparator<Double> comparator;

	private S tmpSolution;
	private S shortestSolution;
	private double initialSolutionProbability;

	NonDominatedSolutionListArchiveForMinMax<S> nonDominatedArchive;

	/**
	 * Constructor
	 */
	public MOSimulatedAnnealingAlgorithm(final Problem<S> problem, final MutationOperator<S> mutationOperator,
			final double rateOfCooling, final int initialTemperature, final int minimalTemperature, final double k,
			Comparator<Double> comparator, double initialSolutionProbability) {
		this.problem = problem;
		this.mutationOperator = mutationOperator;
		this.rateOfCooling = rateOfCooling;
		this.initialTemperature = initialTemperature;
		this.minimalTemperature = minimalTemperature;
		this.comparator = comparator;
		this.k = k;
		this.initialSolutionProbability = initialSolutionProbability;

		nonDominatedArchive = new NonDominatedSolutionListArchiveForMinMax<S>();

	}

	public S findSolution(S currentSolution) {
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().setProbability(initialSolutionProbability);// probability for 0.	
		currentSolution = problem.createSolution();
		shortestSolution = (S) currentSolution;
		double lastDistance, lastCosts;

		lastDistance = currentSolution.getObjective(0);
		lastCosts = currentSolution.getObjective(1);

		problem.evaluate(shortestSolution);
		double temperature = this.initialTemperature;

		System.out.println("Anfangsroute: " + shortestSolution.getObjective(0));
		while (temperature > minimalTemperature) {
			tmpSolution = (S) mutationOperator.execute((S) currentSolution.copy());

			problem.evaluate(tmpSolution);

			if (tmpSolution.getObjective(0) == -1) {
				continue;
			}

			if (lastDistance != tmpSolution.getObjective(0) || lastCosts != tmpSolution.getObjective(1)) {
				nonDominatedArchive.add(tmpSolution);
				lastDistance = tmpSolution.getObjective(0);
				lastCosts = tmpSolution.getObjective(1);

			}

			if (acceptSolution(tmpSolution.getObjective(0), shortestSolution.getObjective(0),
					tmpSolution.getObjective(1), shortestSolution.getObjective(1), temperature)) {
				shortestSolution = (S) tmpSolution;
				currentSolution = (S) tmpSolution;

				if (D) {
					System.out.println(">SA: " + shortestSolution.getObjective(0) + "| "
							+ shortestSolution.getObjective(1));
				}

			}
			temperature *= 1 - rateOfCooling;
		}

		problem.evaluate(shortestSolution);

		if (D) {
			System.out.println("Shortes tSolution Size:" + tmpSolution.getNumberOfVariables());
			System.out.print(">Sortest Solution:");
			System.out.println(shortestSolution.getObjective(0));
			System.out.println("Count: " + count);
		}

		return shortestSolution;
	}

	private boolean acceptSolution(double currentDistance, double shortestDistance, double currentCosts,
			double shortestCosts, double temperature) {
		double acceptanceProbability = 0;
		double delta;
		if (comparator.compare(currentDistance, shortestDistance) == 1) {
			// || (comparator.compare(-1 * currentCosts, -1 * shortestCosts) <= 0)) {
			acceptanceProbability = 1.0;
		} else {
			delta = Math.abs(currentDistance - shortestDistance);
			acceptanceProbability = Math.exp(-(delta / (k * temperature)));
		}

		if (acceptanceProbability >= JMetalRandom.getInstance().nextDouble())
			return true;
		else
			return false;
	}

	@Override
	public void run() {
		findSolution(problem.createSolution());//TODO
	}

	@Override
	public List<S> getResult() {
		List<S> solutions = nonDominatedArchive.getSolutionList();
		for (S s : solutions) {
			s.setObjective(0, s.getObjective(0) * -1);
			s.setObjective(1, s.getObjective(1) * -1);
		}
		nonDominatedArchive = new NonDominatedSolutionListArchiveForMinMax<>();

		return solutions;
	}

	@Override
	public String getName() {
		return "MOSA";
	}

	@Override
	public String getDescription() {
		return "Multi-objective Simulated Annealing";
	}
}
