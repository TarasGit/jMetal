package org.uma.jmetal.algorithm.singleobjective.ACO;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.DefaultIntegerPermutationSolution;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class AntColonyOptimizationAlgorithm<S extends Solution<?>> implements Algorithm<S> {

	public static final int NUMBER_OF_ANTS = 100000;

	protected Problem<S> problem;

	private S currentSolution;
	private S shortestSolution;

	/**
	 * Constructor
	 */
	public AntColonyOptimizationAlgorithm(Problem<S> problem) {
		this.problem = problem;
		this.shortestSolution = null;
	}

	@Override
	public String getName() {
		return "SA";
	}

	@Override
	public String getDescription() {
		return "Ant Colony Optimizatio Algorithm";
	}

	public S findRoute(S currentSolution) {// TODO: no initial route needed, because the ant starts with zero-route.
		this.currentSolution = currentSolution;
		
		Ant currentAnt;
		
		AntColonyOptimization<S> aco = new AntColonyOptimization<S>(problem,
				(DefaultIntegerPermutationSolution) currentSolution);

		// IntStream.range(0, NUMBER_OF_ANTS).forEach(x -> {

		for (int i = 0; i < NUMBER_OF_ANTS; i++) {
			currentAnt = new Ant(aco, i).run();
			processAnts(currentAnt);
		}


		return shortestSolution;
	}

	private void processAnts(Ant ant) {
		try {
			currentSolution = (S) ant.getSolution();
			problem.evaluate(currentSolution);
			if (shortestSolution == null || currentSolution.getObjective(0) < shortestSolution.getObjective(0)) {
				shortestSolution = (S) currentSolution.copy();// copy?
				System.out.println( shortestSolution.getObjective(0));
			}
			//System.out.println("CS: " + currentSolution.getObjective(0));

		} catch (Exception e) {
			e.printStackTrace();
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
