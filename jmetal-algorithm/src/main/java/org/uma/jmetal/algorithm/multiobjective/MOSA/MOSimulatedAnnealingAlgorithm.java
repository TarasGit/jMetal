package org.uma.jmetal.algorithm.multiobjective.MOSA;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.selection.RankingAndCrowdingSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchive;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * @author Taras Iks <ikstaras@gmail.com>
 */
@SuppressWarnings("serial")
public class MOSimulatedAnnealingAlgorithm<S extends Solution<?>> implements Algorithm<List<S>> {

	private double rateOfCooling;
	private int initialTemperature;
	private int minimalTemperature;
	private int count = 0;

	protected Problem<S> problem;
	protected MutationOperator<S> mutationOperator;
	private Comparator<Double> comparator;

	private S adjacentSolution;
	private S shortestSolution;
	private List<S> endResult;
	
	NonDominatedSolutionListArchive<S> nonDominatedArchive ;


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
		this.endResult = new ArrayList<>();
		
	    nonDominatedArchive = new NonDominatedSolutionListArchive<S>();

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
		
		List<S> emptyList = new ArrayList<>();
		
		problem.evaluate(shortestSolution);
		double temperature = this.initialTemperature;

		System.out.println("Anfangsroute: " + shortestSolution.getObjective(0));
		while (temperature > minimalTemperature) {
			adjacentSolution = (S) mutationOperator.execute((S) currentSolution.copy());// currentSolution.copy(); ->
																						// Taras changed.

			problem.evaluate(adjacentSolution);
			problem.evaluate(currentSolution);
			problem.evaluate(shortestSolution);

			if (adjacentSolution.getObjective(0) == -1) {
				continue;
			}
			
		    nonDominatedArchive.add(shortestSolution);//TODO: shouold add shortest?
			
			
			endResult.add(shortestSolution);
		
			if(endResult.size() > 2000)
				endResult = replacement(emptyList, endResult);
			

			

			if (comparator.compare(currentSolution.getObjective(0), shortestSolution.getObjective(0)) == 1)
				shortestSolution = (S) currentSolution.copy();// copy???
			if (acceptRoute(currentSolution.getObjective(0), adjacentSolution.getObjective(0), temperature))
				currentSolution = (S) adjacentSolution.copy();// copy???
			temperature *= 1 - rateOfCooling;
			System.out.println(">" + shortestSolution.getObjective(0) + " + " + shortestSolution.getObjective(1));
			count++;
		}

		System.out.println("Shortes tSolution Size:" + shortestSolution.getNumberOfVariables());
		problem.evaluate(shortestSolution);

		System.out.print(">Sortest Solution:");
		System.out.println(shortestSolution.getObjective(0));
		System.out.println("Count: " + count);

		return shortestSolution;
	}

	protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
		List<S> jointPopulation = new ArrayList<>();
		jointPopulation.addAll(population);
		jointPopulation.addAll(offspringPopulation);

		RankingAndCrowdingSelection<S> rankingAndCrowdingSelection;
		rankingAndCrowdingSelection = new RankingAndCrowdingSelection<S>(2000);//TODO XXX: //getMaxPopulationSize());

		return rankingAndCrowdingSelection.execute(jointPopulation);
	}
	
	private boolean acceptRoute(double currentDistance, double adjacentDistance, double temperature) {
		double acceptanceProbability;
		double delta;
		if (comparator.compare(currentDistance, adjacentDistance) == 1) {
			delta = Math.abs(adjacentDistance - currentDistance);
			acceptanceProbability = Math.exp(-(delta / (temperature)));//TODO: check
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
		findRoute(problem.createSolution());
	}

	@Override
	public List<S> getResult() {
		System.out.println("->" + SolutionListUtils.getNondominatedSolutions(endResult).stream().count());
		return SolutionListUtils.getNondominatedSolutions(endResult);
	}
}
