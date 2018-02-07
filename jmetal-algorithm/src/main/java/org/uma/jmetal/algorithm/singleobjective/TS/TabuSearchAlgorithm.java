package org.uma.jmetal.algorithm.singleobjective.TS;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.IteratorUtils;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.BestNeighborSolutionFinder;

public class TabuSearchAlgorithm<S extends Solution<?>> implements Algorithm<List<S>> {

	private static final boolean D = true; // DEBUG	
	private static final long serialVersionUID = 1L;
	private TabuList<S> tabuList;
	private StopCondition stopCondition;
	private BestNeighborSolutionFinder<S> solutionLocator;
	private int numberOfNeighbors;
	private MutationOperator<S> mutationOperator;
	private List<S> endResult;
	private S initialSolution;
	private Comparator<Double> comparator;
	Problem<S> problem;

	public TabuSearchAlgorithm(TabuList<S> tabuList, StopCondition stopCondition,
			BestNeighborSolutionFinder<S> solutionLocator, MutationOperator<S> mutationOperator, S initialSolution,
			Comparator<Double> comparator, int numberOfNeighbors, Problem<S> problem) {
		this.tabuList = tabuList;
		this.stopCondition = stopCondition;
		this.solutionLocator = solutionLocator;
		this.mutationOperator = mutationOperator;
		this.initialSolution = initialSolution;
		this.comparator = comparator;
		this.problem = problem;
		this.numberOfNeighbors = numberOfNeighbors;
		this.endResult = new ArrayList<>();
	}

	public S run(S initialSolution) {
		S bestSolution = initialSolution;
		problem.evaluate(bestSolution);
		S currentSolution = initialSolution;
		S bestNeighborFound = null;

		Integer currentIteration = 0;
		while (!stopCondition.mustStop(++currentIteration)) {

			List<S> candidateNeighbors = getNeighbors(currentSolution);
			List<S> solutionsInTabu = IteratorUtils.toList(tabuList.iterator());
			List<S> listWithoutViolations = new ArrayList<>();

			for (S solution : candidateNeighbors) {
				Double tmpAttr = (Double) solution.getObjective(0);
				if (tmpAttr != -1.0)
					listWithoutViolations.add(solution);
			}

			if (listWithoutViolations.isEmpty()) {
				continue;
			}

			Optional<S> optionalBestNeighborFound = solutionLocator.findBestNeighbor(candidateNeighbors,
					solutionsInTabu);

			if (!optionalBestNeighborFound.isPresent())
				break;
			else
				bestNeighborFound = optionalBestNeighborFound.get();

			/* minComparator == 1 if a < b, maxComparator == 1, if a > b */
			if (comparator.compare(bestNeighborFound.getObjective(0), bestSolution.getObjective(0)) == 1) {
				bestSolution = bestNeighborFound;
				if (D)
					System.out.println("fitness: " + bestSolution.getObjective(0) + " / " + "costs: "
							+ bestSolution.getAttribute(0));
			}

			tabuList.add(currentSolution);
			currentSolution = bestNeighborFound;
		}

		return bestSolution;
	}

	public List<S> getNeighbors(S solution) {
		ArrayList<S> newList = new ArrayList<>();
		for (int i = 0; i < numberOfNeighbors; i++) {
			S tmpSolution = mutationOperator.execute((S) solution.copy());
			tmpSolution.setObjective(0, 0);
			tmpSolution.setAttribute(0, 0);
			problem.evaluate(tmpSolution);
			newList.add(tmpSolution);
		}
		return newList;
	}

	@Override
	public String getName() {
		return "TabuSearch";
	}

	@Override
	public String getDescription() {
		return "Tabu Search Algorithm";
	}

	@Override
	public void run() {
		endResult.add(run(initialSolution));
	}

	@Override
	public List<S> getResult() {
		return endResult;
	}

}
