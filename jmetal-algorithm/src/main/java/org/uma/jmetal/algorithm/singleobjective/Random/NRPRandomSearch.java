package org.uma.jmetal.algorithm.singleobjective.Random;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.impl.selection.RankingAndCrowdingSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.SolutionListUtils;

/**
 * This class implements a simple random search algorithm.
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
@SuppressWarnings("serial")
public class NRPRandomSearch<S extends Solution<?>> implements Algorithm<List<S>> {
	private Problem<S> problem;
	private int maxEvaluations;
	private S result;
	static int count = 0;
	private List<S> endResult;
	private List<S> workingList;
	private int listSize = 500;

	/** Constructor */
	public NRPRandomSearch(Problem<S> problem, int maxEvaluations) {
		this.problem = problem;
		this.maxEvaluations = maxEvaluations;
		workingList = new ArrayList<>(listSize);
		endResult = new ArrayList<>(listSize);
	}

	/* Getter */
	public int getMaxEvaluations() {
		return maxEvaluations;
	}

	@Override
	public void run() {
		S newSolution;
		boolean flag = true;
		result = problem.createSolution();
		problem.evaluate(result);
		System.out.println("Max Iterations: " + maxEvaluations);
		for (int i = 0; i < maxEvaluations; i++) {
			newSolution = problem.createSolution();
			problem.evaluate(newSolution);

			if (newSolution.getObjective(0) == -1)
				continue;

			if (count == listSize - 1) {
				count = 0;
				endResult = replacement(endResult, workingList);
			}

			if (workingList.size() != listSize)
				workingList.add(newSolution);
			else
				workingList.set(count++, newSolution);

			if (newSolution.getObjective(0) > result.getObjective(0)) {
				result = (S) newSolution.copy();
				System.out.println(result.getObjective(0) + " / " + result.getObjective(1));
			}
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
	public List<S> getResult() {
		System.out.println(SolutionListUtils.getNondominatedSolutions(endResult).size());
		return SolutionListUtils.getNondominatedSolutions(endResult);
	}

	@Override
	public String getName() {
		return "RS";
	}

	@Override
	public String getDescription() {
		return "Multi-objective random search algorithm";
	}
}
