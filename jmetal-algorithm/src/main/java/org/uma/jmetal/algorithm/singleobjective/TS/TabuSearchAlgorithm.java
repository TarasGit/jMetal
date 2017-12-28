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
import org.uma.jmetal.util.comparator.BestNeighborSolutionLocator;

public class TabuSearchAlgorithm<S extends Solution<?>> implements Algorithm<S> {

	/*
	 * TODO: 
	 * - check mutation operator -> mutation vs. swap operation; should be flip-mutation at the beginning and swap at the end.
	 * -
	 * */
	private static final long serialVersionUID = 1L;
	private TabuList<S> tabuList;
	private StopCondition stopCondition;
	private BestNeighborSolutionLocator<S> solutionLocator;
	private int numberOfNeighbors = 100;
	private MutationOperator<S> mutationOperator;
	private S endResult;
	private S initialSolution;
	private Comparator<Double> comparator;
	Problem<S> problem;

	public TabuSearchAlgorithm(TabuList<S> tabuList, StopCondition stopCondition,
			BestNeighborSolutionLocator<S> solutionLocator, MutationOperator<S> mutationOperator, S initialSolution, Comparator<Double> comparator,
			Problem<S> problem) {
		this.tabuList = tabuList;
		this.stopCondition = stopCondition;
		this.solutionLocator = solutionLocator;
		this.mutationOperator = mutationOperator;
		this.initialSolution = initialSolution;
		this.comparator = comparator;
		this.problem = problem;
	}

	public S run(S initialSolution) {
		S bestSolution = initialSolution;
		problem.evaluate(bestSolution);
		
		System.out.println("Initial fitness: " + bestSolution.getObjective(0) + " / " + "costs: " + bestSolution.getAttribute(0));
		System.out.println(bestSolution);
		
		S currentSolution = initialSolution;
		S bestNeighborFound = null;

		Integer currentIteration = 0;
		while (!stopCondition.mustStop(++currentIteration)) {

			List<S> candidateNeighbors = getNeighbors(currentSolution);			
			List<S> solutionsInTabu = IteratorUtils.toList(tabuList.iterator());
			List<S> listWithoutViolations = new ArrayList<>();
			
			
			for(S solution : candidateNeighbors) {
				Double tmpAttr =(Double) solution.getObjective(0);
				if(tmpAttr != -1.0)
					listWithoutViolations.add(solution);
			}
			
			if(listWithoutViolations.isEmpty()) {
				continue;												//TODO: break, if we get invalid solutions-> better computation time, worse quality of results.
			}

			Optional<S> optionalBestNeighborFound = solutionLocator.findBestNeighbor(candidateNeighbors,
					solutionsInTabu);

			if (!optionalBestNeighborFound.isPresent())// Taras added.
				break;
			else
				bestNeighborFound = optionalBestNeighborFound.get();
			
			System.out.println("fitness: " + bestNeighborFound.getObjective(0) + " / " + "costs: " + bestNeighborFound.getAttribute(0));
			//System.out.println(bestNeighborFound);

			
			/* minComparator == 1 if a < b, maxComparator == 1, if a > b */
			if (comparator.compare(bestNeighborFound.getObjective(0), bestSolution.getObjective(0)) == 1) {
				bestSolution = bestNeighborFound;
				System.out.println("fitness: " + bestSolution.getObjective(0) + " / " + "costs: " + bestSolution.getAttribute(0));
			}

			tabuList.add(currentSolution);
			currentSolution = bestNeighborFound;
		}

		
		return bestSolution;
	}

	public List<S> getNeighbors(S solution) {
		ArrayList<S> newList = new ArrayList<>();
		for (int i = 0; i < numberOfNeighbors; i++) {
			S tmpSolution = mutationOperator.execute((S) solution.copy());// TODO: how to cast properly?
			tmpSolution.setObjective(0, 0);//TODO: reset costs to 0?
			tmpSolution.setAttribute(0, 0);
			problem.evaluate(tmpSolution);
			//System.out.println("fitness: " + tmpSolution.getObjective(0) + " / " + "costs: " + tmpSolution.getAttribute(0));
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
		endResult = run(initialSolution);
		System.out.println("EndResult" + endResult);
	}

	@Override
	public S getResult() {
		return (S) endResult;
	}

}
