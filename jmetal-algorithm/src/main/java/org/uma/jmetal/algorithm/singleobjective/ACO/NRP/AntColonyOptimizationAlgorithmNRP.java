package org.uma.jmetal.algorithm.singleobjective.ACO.NRP;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

/**
 * @author Taras Iks <ikstaras@gmail.com>
 */
@SuppressWarnings("serial")
public class AntColonyOptimizationAlgorithmNRP<S extends Solution<?>> implements Algorithm<S> {

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
	public AntColonyOptimizationAlgorithmNRP(Problem<S> problem, int numberOfAnts, double alpha, double beta,
			double rho, double q) {
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

	public S findRoute(S currentSolution) {
		this.currentSolution = currentSolution;

		AntNRP<S> currentAnt;

		AntColonyOptimizationNRP<S> aco = new AntColonyOptimizationNRP<S>(problem, currentSolution);

		for (int i = 0; i < numberOfAnts; i++) {
			currentAnt = new AntNRP<S>(aco, i, alpha, beta, rho, q).run();
			processAnts(currentAnt);
		}

		return shortestSolution;
	}

	private void processAnts(AntNRP ant) {
		try {
			currentSolution = (S) ant.getSolution();
			problem.evaluate(currentSolution);
			if (shortestSolution == null || currentSolution.getObjective(0) > shortestSolution.getObjective(0)) {
				shortestSolution = (S) currentSolution.copy();// copy?
				System.out.println("shortest Solution: " + shortestSolution.getObjective(0) + "Costs: " +  shortestSolution.getAttribute(0));
			}
			// System.out.println("CS: " + currentSolution.getObjective(0));
			//System.out.println("> " + shortestSolution.getObjective(0) + "Costs: " +  shortestSolution.getAttribute(0));

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