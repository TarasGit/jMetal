package org.uma.jmetal.algorithm.singleobjective.ACO;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.DefaultIntegerPermutationSolution;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class AntColonyOptimizationAlgorithm<S extends Solution<?>> implements Algorithm<List<S>> {

	public static final int NUMBER_OF_ANTS = 20;
	public static final double PROCESSING_CYCLE_PROBABILITY = 0.0;//0.8; works correctly with 0.0, or an null pointer exception!!!

	private int count = 0;

	protected TSP problem;
	// protected PermutationSwapMutation<Integer> mutationOperator;
	private int activeAnts;

	// private SolutionListEvaluator<S> evaluator;
	private DefaultIntegerPermutationSolution currentSolution;
	private DefaultIntegerPermutationSolution adjacentSolution;
	private DefaultIntegerPermutationSolution shortestSolution;

	static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	static ExecutorCompletionService<Ant> executorCompletionService = new ExecutorCompletionService<Ant>(
			executorService);

	/**
	 * Constructor
	 */
	public AntColonyOptimizationAlgorithm(TSP problem) {
		this.problem = problem;
		// this.crossoverOperator = crossoverOperator;
		// this.mutationOperator = mutationOperator;
		// this.selectionOperator = selectionOperator;

		// this.evaluator = evaluator;

		// comparator = new ObjectiveComparator<S>(0);
	}

	@Override
	public String getName() {
		return "SA";
	}

	@Override
	public String getDescription() {
		return "Ant Colony Optimizatio Algorithm";
	}

	// Route shortestRoute = new Route(currentRoute);
	// Route adjacentRoute;
	// while(temperature > MIN_TEMPERATURE) {
	// System.out.print(currentRoute + " | " + currentRoute.getTotalDistance() + " |
	// " + String.format("%.2f", temperature));
	// adjacentRoute = obtainAdjacentRoute(new Route(currentRoute));
	// if(currentRoute.getTotalDistance() < shortestRoute.getTotalDistance())
	// shortestRoute = new Route(currentRoute);
	// if(acceptRoute(currentRoute.getTotalDistance(),
	// adjacentRoute.getTotalDistance(), temperature))
	// currentRoute = new Route(adjacentRoute);
	// temperature *= 1 - RATE_OF_COOLING;
	// }
	// return shortestRoute;

	public DefaultIntegerPermutationSolution findRoute(DefaultIntegerPermutationSolution currentSolution) {
		this.currentSolution = currentSolution;
		this.shortestSolution = currentSolution.copy();
		AntColonyOptimization aco = new AntColonyOptimization(problem);
		problem.evaluate(currentSolution);
		System.out.println("Current Solution: " + currentSolution);
		IntStream.range(1, NUMBER_OF_ANTS).forEach( x -> {

			executorCompletionService.submit(new Ant(aco, x));
			activeAnts++;
			if(Math.random() > PROCESSING_CYCLE_PROBABILITY) {
				processAnts();
			}
			count++;
		});
		problem.evaluate(shortestSolution);

		executorService.shutdownNow();
		System.out.println(shortestSolution);
		System.out.println("Count: " + count);
		return shortestSolution;
	}

	private void processAnts() {
		while (activeAnts > 0) {
			try {
				Ant ant = executorCompletionService.take().get();
				currentSolution = ant.getSolution();
				problem.evaluate(currentSolution);
				problem.evaluate(shortestSolution);
				if (shortestSolution == null || currentSolution.getObjective(0) < shortestSolution.getObjective(0)) {
					shortestSolution = currentSolution.copy();// copy?
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			activeAnts--;
		}
	}

	// private S obtainAdjacentRoute(S route) {
	// int x1 = 0, x2 = 0;
	//
	// while (x1 == x2) {
	// x1 = (int) (route.getCities().size() * Math.random());
	// x2 = (int) (route.getCities().size() * Math.random());
	// }
	//
	// City city1 = route.getCities().get(x1);
	// City city2 = route.getCities().get(x2);
	//
	// route.getCities().set(x2, city1);
	// route.getCities().set(x1, city2);
	//
	// return route;
	// }

	@Override
	public void run() {
		findRoute((DefaultIntegerPermutationSolution) problem.createSolution());
	}

	@Override
	public List<S> getResult() {
		return (List<S>) Arrays.asList(this.shortestSolution);// XXX TODO wie im NSGA-II
	}
}
