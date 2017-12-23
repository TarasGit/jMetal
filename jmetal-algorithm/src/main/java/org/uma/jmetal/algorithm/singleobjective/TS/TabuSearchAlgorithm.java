package org.uma.jmetal.algorithm.singleobjective.TS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.IteratorUtils;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;


public class TabuSearchAlgorithm<S extends Solution<?>> implements Algorithm<S> {

	private static final long serialVersionUID = 1L;
	private TabuList<S> tabuList;
	private StopCondition stopCondition;
	private BestNeighborSolutionLocator<S> solutionLocator;
	private int numberOfNeighbors = 30;
	MutationOperator<S> mutationOperator;
	S endResult;
	S initialSolution;
	Problem<S> problem;


	public TabuSearchAlgorithm(TabuList<S> tabuList, StopCondition stopCondition, BestNeighborSolutionLocator<S> solutionLocator,
			MutationOperator<S> mutationOperator, S initialSolution, Problem<S> problem) {
		this.tabuList = tabuList;
		this.stopCondition = stopCondition;
		this.solutionLocator = solutionLocator;
		this.mutationOperator = mutationOperator;
		this.initialSolution = initialSolution;
		this.problem = problem;
	}

	public S run(S initialSolution) {
		S bestSolution = initialSolution;
		problem.evaluate(bestSolution);
		S currentSolution = initialSolution;

		Integer currentIteration = 0;
		while (!stopCondition.mustStop(++currentIteration)) {

			S bestNeighborFound = null;
			List<S> candidateNeighbors = getNeighbors(currentSolution);
			List<S> solutionsInTabu = IteratorUtils.toList(tabuList.iterator());

			Optional<S> optionalBestNeighborFound = solutionLocator
					.findBestNeighbor(candidateNeighbors, solutionsInTabu);

			if (!optionalBestNeighborFound.isPresent())// Taras added.
				break;
			else
				bestNeighborFound = optionalBestNeighborFound.get();


			if (bestNeighborFound.getObjective(0) < bestSolution.getObjective(0)) {
				bestSolution = bestNeighborFound;
			}

			tabuList.add(currentSolution);
			currentSolution = bestNeighborFound;

		}

		return bestSolution;
	}

	public List<S> getNeighbors(S solution) {
		ArrayList<S> newList = new ArrayList<>();
		for (int i = 0; i < numberOfNeighbors; i++) {
				S tmpSolution = mutationOperator.execute((S) solution.copy());//TODO: how to cast properly?
				problem.evaluate(tmpSolution);
				newList.add(tmpSolution);
				solution = tmpSolution;
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
		endResult = run(initialSolution);
	}

	@Override
	public S getResult() {
		return (S) endResult;
	}

}
