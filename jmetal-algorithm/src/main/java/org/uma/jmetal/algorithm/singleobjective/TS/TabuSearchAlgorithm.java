package org.uma.jmetal.algorithm.singleobjective.TS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.IteratorUtils;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.solution.Solution;


public class TabuSearchAlgorithm<S extends Solution<?>> implements Algorithm<S> {

	private static final long serialVersionUID = 1L;
	private TabuList tabuList;
	private StopCondition stopCondition;
	private BestNeighborSolutionLocator solutionLocator;
	private int numberOfNeighbors = 30;
	PermutationSwapMutation<Integer> mutationOperator;
	PermutationSolution<Integer> endResult;
	PermutationSolution<Integer> initialSolution;
	Problem<S> problem;


	public TabuSearchAlgorithm(TabuList tabuList, StopCondition stopCondition, BestNeighborSolutionLocator solutionLocator,
			PermutationSwapMutation<Integer> mutationOperator, PermutationSolution<Integer> initialSolution, Problem<S> problem) {
		this.tabuList = tabuList;
		this.stopCondition = stopCondition;
		this.solutionLocator = solutionLocator;
		this.mutationOperator = mutationOperator;
		this.initialSolution = initialSolution;
		this.problem = problem;
	}

	public PermutationSolution<Integer> run(PermutationSolution<Integer> initialSolution) {
		PermutationSolution<Integer> bestSolution = initialSolution;
		problem.evaluate((S)bestSolution);
		PermutationSolution<Integer> currentSolution = initialSolution;

		Integer currentIteration = 0;
		while (!stopCondition.mustStop(++currentIteration)) {

			PermutationSolution<Integer> bestNeighborFound = null;
			List<PermutationSolution<Integer>> candidateNeighbors = getNeighbors(currentSolution);
			List<PermutationSolution<Integer>> solutionsInTabu = IteratorUtils.toList(tabuList.iterator());

			Optional<PermutationSolution<Integer>> optionalBestNeighborFound = solutionLocator
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

	public List<PermutationSolution<Integer>> getNeighbors(PermutationSolution<Integer> solution) {
		ArrayList<PermutationSolution<Integer>> newList = new ArrayList<>();
		for (int i = 0; i < numberOfNeighbors; i++) {
				PermutationSolution<Integer> tmpSolution = mutationOperator.execute((PermutationSolution<Integer>) solution.copy());
				problem.evaluate((S)tmpSolution);
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
