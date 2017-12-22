package org.uma.jmetal.algorithm.singleobjective.ACO;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.DefaultIntegerPermutationSolution;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class AntColonyOptimizationAlgorithm<S extends Solution<?>> implements Algorithm<S> {

	public static final int NUMBER_OF_ANTS = 20;
	public static final double PROCESSING_CYCLE_PROBABILITY = 0.0;//0.8; works correctly with 0.0, or an null pointer exception!!!

	private int count = 0;
	protected Problem<S> problem;
	private int activeAnts;

	private S currentSolution;
	private S shortestSolution;

	static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	static ExecutorCompletionService<Ant> executorCompletionService = new ExecutorCompletionService<Ant>(
			executorService);

	/**
	 * Constructor
	 */
	public AntColonyOptimizationAlgorithm(Problem<S> problem) {
		this.problem = problem;
	}

	@Override
	public String getName() {
		return "SA";
	}

	@Override
	public String getDescription() {
		return "Ant Colony Optimizatio Algorithm";
	}

	public S findRoute(S currentSolution) {
		this.currentSolution = currentSolution;
		this.shortestSolution = (S) currentSolution.copy();
		AntColonyOptimization<S> aco = new AntColonyOptimization<S>(problem);
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
				currentSolution = (S) ant.getSolution();
				problem.evaluate(currentSolution);
				problem.evaluate(shortestSolution);
				if (shortestSolution == null || currentSolution.getObjective(0) < shortestSolution.getObjective(0)) {
					shortestSolution = (S) currentSolution.copy();// copy?
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			activeAnts--;
		}
	}

	@Override
	public void run() {
		findRoute(problem.createSolution());
	}

	@Override
	public S getResult() {
		return this.shortestSolution;// XXX TODO wie im NSGA-II
	}
}
