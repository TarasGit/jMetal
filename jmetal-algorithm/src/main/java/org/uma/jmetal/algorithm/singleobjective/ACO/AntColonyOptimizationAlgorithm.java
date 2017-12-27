package org.uma.jmetal.algorithm.singleobjective.ACO;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

/**
 * @author Taras Iks <ikstaras@gmail.com>
 */
@SuppressWarnings("serial")
public class AntColonyOptimizationAlgorithm<S extends Solution<?>> implements Algorithm<S> {

	protected Problem<S> problem;
	private int numberOfAnts;
	private S currentSolution;
	private S shortestSolution;
	private double alpha;
	private double beta;
	private double rho;
	private double q;
	

	/**
	 * Constructor
	 */
	public AntColonyOptimizationAlgorithm(Problem<S> problem, int numberOfAnts, double alpha, double beta, double rho, double q) {
		this.problem = problem;
		this.numberOfAnts = numberOfAnts;
		this.shortestSolution = null;
		this.alpha = alpha;
		this.beta = beta;
		this.rho = rho;
		this.q = q;
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
		
		Ant<S> currentAnt;
		
		AntColonyOptimization<S> aco = new AntColonyOptimization<S>(problem,
				currentSolution);

		// IntStream.range(0, NUMBER_OF_ANTS).forEach(x -> {

		for (int i = 0; i < numberOfAnts; i++) {
			currentAnt = new Ant<S>(aco, i, alpha, beta, rho, q).run();
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
