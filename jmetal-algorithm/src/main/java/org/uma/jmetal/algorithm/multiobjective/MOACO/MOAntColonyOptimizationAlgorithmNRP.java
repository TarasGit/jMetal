package org.uma.jmetal.algorithm.multiobjective.MOACO;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.archive.impl.MyNonDominatedSolutionListArchive;

/**
 * @author Taras Iks <ikstaras@gmail.com>
 */
@SuppressWarnings("serial")
public class MOAntColonyOptimizationAlgorithmNRP<S extends Solution<?>> implements Algorithm<List<S>> {

	protected Problem<S> problem;
	private int numberOfAnts;
	private S currentSolution;
	private S shortestSolution;
	private double alpha;
	private double beta;
	private double rho;
	private double q;

	MyNonDominatedSolutionListArchive<S> nonDominatedArchive;

	/**
	 * Constructor
	 */
	public MOAntColonyOptimizationAlgorithmNRP(Problem<S> problem, int numberOfAnts, double alpha, double beta,
			double rho, double q) {
		this.problem = problem;
		this.numberOfAnts = numberOfAnts;
		this.shortestSolution = null;
		this.alpha = alpha;
		this.beta = beta;
		this.rho = rho;
		this.q = q;
		nonDominatedArchive = new MyNonDominatedSolutionListArchive<S>();

	}

	@Override
	public String getName() {
		return "ACO";
	}

	@Override
	public String getDescription() {
		return "Ant Colony Optimizatio Algorithm";
	}

	public S findRoute(S currentSolution) {
		this.currentSolution = currentSolution;

		MOAntNRP<S> currentAnt;

		MOAntColonyOptimizationNRP<S> aco = new MOAntColonyOptimizationNRP<S>(problem, currentSolution);

		for (int i = 0; i < numberOfAnts; i++) {
			currentAnt = new MOAntNRP<S>(aco, i, alpha, beta, rho, q).run();
			processAnts(currentAnt);
			System.out.println("------------------------------------------------------------->Ant(" + i + ")");
		}

		// endResult = replacement(endResult, workingList);

		return shortestSolution;
	}

	int count = 0;

	private void processAnts(MOAntNRP ant) {
		try {

			List<S> resultList = ant.getSolution();

			for (int i = 0; i < resultList.size(); i++) {

				currentSolution = resultList.get(i);

				problem.evaluate(currentSolution);
				if (currentSolution.getObjective(0) == -1)
					break;

				shortestSolution = (S) currentSolution.copy();// copy?

				nonDominatedArchive.add(shortestSolution);

				System.out.println("shortest Solution: " + shortestSolution.getObjective(0) + "Costs: "
						+ shortestSolution.getObjective(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		findRoute(problem.createSolution());
	}

	@Override
	public List<S> getResult() {
		return nonDominatedArchive.getSolutionList();
	}
}
