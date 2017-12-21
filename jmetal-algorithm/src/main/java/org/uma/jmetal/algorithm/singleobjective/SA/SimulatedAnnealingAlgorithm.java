package org.uma.jmetal.algorithm.singleobjective.SA;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.DefaultIntegerPermutationSolution;

/**
 * @author Taras Iks <ikstaras@gmail.com>
 */
@SuppressWarnings("serial")
public class SimulatedAnnealingAlgorithm<S extends Solution<?>> implements Algorithm<List<S>> {

	public static final double RATE_OF_COOLING = 0.01;
	public static final double INITIAL_TEMPERATURE = 2;
	public static final double MIN_TEMPERATURE = 0.99;
	private int count = 0;

	protected Problem<S> problem;
	protected PermutationSwapMutation<Integer> mutationOperator;
	private int temperature;

	private S adjacentSolution;
	private S shortestSolution;

	/**
	 * Constructor
	 */
	public SimulatedAnnealingAlgorithm(Problem<S> problem, int temperature, PermutationSwapMutation<Integer> mutationOperator) {
		this.problem = problem;
		this.temperature = temperature;
		this.mutationOperator = mutationOperator;
	}

	@Override
	public String getName() {
		return "SA";
	}

	@Override
	public String getDescription() {
		return "Generational Genetic Algorithm";
	}
	


	@SuppressWarnings("unchecked")
	public S findRoute(double temperature, S currentSolution) {
		shortestSolution = (S)currentSolution.copy();
		problem.evaluate(shortestSolution);

		System.out.println("Anfangsroute: " + shortestSolution.getObjective(0));
		while (temperature > MIN_TEMPERATURE) {
			adjacentSolution =   (S) mutationOperator.execute((PermutationSolution<Integer>)currentSolution.copy());//obtainAdjacentRoute(new Route(currentRoute));// permutationSwapMutation Operator.
			problem.evaluate(adjacentSolution);
			problem.evaluate(currentSolution);
			problem.evaluate(shortestSolution);
			if (currentSolution.getObjective(0) < shortestSolution.getObjective(0))
				shortestSolution = (S)currentSolution.copy();//copy???
			if (acceptRoute(currentSolution.getObjective(0), adjacentSolution.getObjective(0), temperature))
				currentSolution = (S)adjacentSolution.copy();//copy???
			temperature *= 1 - RATE_OF_COOLING;

			count++;
		}
		
		System.out.println("Shortes tSolution Size:" + shortestSolution.getNumberOfVariables());
		problem.evaluate(shortestSolution);
		//Test Start - only unique values in the set.
		Set<Integer> intSet = new TreeSet<>();
	
		IntStream.range(0, shortestSolution.getNumberOfVariables()).forEach(v -> {
			intSet.add(v);
		});
		System.out.println("Set Size: " + intSet.size());
		//Test End
		
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

		double randomNumb = Math.random();
		if (acceptanceProbability >= randomNumb)
			acceptRouteFlag = true;
		return acceptRouteFlag;
	}


	@Override
	public void run() {
		findRoute(this.temperature,  problem.createSolution());
	}

	@Override
	public List<S> getResult() {
		return (List<S>) Arrays.asList(this.shortestSolution);
	}
}
