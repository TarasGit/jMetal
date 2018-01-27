package org.uma.jmetal.algorithm.multiobjective.MOACO;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.impl.selection.RankingAndCrowdingSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;

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
	private List<S> endResult;
	private List<S> workingList;
	private int listSize = 1000;

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
		this.endResult = new ArrayList<>();
		this.workingList = new ArrayList<>();
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

		//endResult = replacement(endResult, workingList);

		return shortestSolution;
	}

	int count = 0;

	private void processAnts(MOAntNRP ant) {
		try {
			
			List<S> resultList = ant.getSolution();

			for (int i = 0; i < resultList.size(); i++) {


				currentSolution = resultList.get(i);

				problem.evaluate(currentSolution);
				if(currentSolution.getObjective(0) == -1)
					break;

				shortestSolution = (S) currentSolution.copy();// copy?

				if (count == listSize - 1) {
					count = 0;
					endResult = replacement(endResult, workingList);
				}

				if (workingList.size() != listSize)
					workingList.add(shortestSolution);
				else
					workingList.set(count++, shortestSolution);

				System.out.println("shortest Solution: " + shortestSolution.getObjective(0) + "Costs: "
						+ shortestSolution.getObjective(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected List<S> replacement(List<S> population, List<S> offspringPopulation) {// TODO: protected?
		List<S> jointPopulation = new ArrayList<>();
		jointPopulation.addAll(population);
		jointPopulation.addAll(offspringPopulation);

		RankingAndCrowdingSelection<S> rankingAndCrowdingSelection;
		rankingAndCrowdingSelection = new RankingAndCrowdingSelection<S>(listSize);// TODO XXX:
																					// //getMaxPopulationSize());

		return rankingAndCrowdingSelection.execute(jointPopulation);
	}

	@Override
	public void run() {
		findRoute(problem.createSolution());
	}

	@Override
	public List<S> getResult() {
		return SolutionListUtils.getNondominatedSolutions(endResult);
	}
}
