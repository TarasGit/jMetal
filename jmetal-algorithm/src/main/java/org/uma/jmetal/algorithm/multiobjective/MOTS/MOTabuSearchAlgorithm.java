package org.uma.jmetal.algorithm.multiobjective.MOTS;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchiveForMinMax;
import org.uma.jmetal.util.comparator.MONotInTabuListSolutionFinder;

public class MOTabuSearchAlgorithm<S extends Solution<?>> implements Algorithm<List<S>> {

	/*
	 * TODO: - check mutation operator -> mutation vs. swap operation; should be
	 * flip-mutation at the beginning and swap at the end. - move numberOfNeighbors
	 * to TabuSearchRunner.
	 */
	private static final long serialVersionUID = 1L;
	private MOTabuList<S> tabuList;
	private MOStopCondition stopCondition;
	private MONotInTabuListSolutionFinder<S> solutionLocator;
	private int numberOfNeighbors = 100;
	private MutationOperator<S> mutationOperator;
	private S initialSolution;
	private Comparator<Double> comparator;
	Problem<S> problem;
	NonDominatedSolutionListArchiveForMinMax<S> nonDominatedArchive;

	

	public MOTabuSearchAlgorithm(MOTabuList<S> tabuList, MOStopCondition stopCondition,
			MONotInTabuListSolutionFinder<S> solutionLocator, MutationOperator<S> mutationOperator, S initialSolution,
			Comparator<Double> comparator, Problem<S> problem) {
		this.tabuList = tabuList;
		this.stopCondition = stopCondition;
		this.solutionLocator = solutionLocator;
		this.mutationOperator = mutationOperator;
		this.initialSolution = initialSolution;
		this.comparator = comparator;
		this.problem = problem;
		this.nonDominatedArchive = new NonDominatedSolutionListArchiveForMinMax<S>();

	}

	public S run(S initialSolution) {
		S bestSolution = initialSolution;
		problem.evaluate(bestSolution);
		System.out.println(bestSolution.getVariableValueString(0));
		System.out.println(
				"Initial fitness: " + bestSolution.getObjective(0) + " / " + "costs: " + bestSolution.getObjective(1));
		System.out.println(bestSolution);

		S currentSolution = initialSolution;
		List<S> listWithoutViolations = new ArrayList<>();
		List<S> candidateNeighbors;


		Integer currentIteration = 0;
		while (!stopCondition.mustStop(++currentIteration)) {

			candidateNeighbors = getNeighbors(currentSolution);
			listWithoutViolations.clear();
			for (S solution : candidateNeighbors) {
				Double tmpAttr = (Double) solution.getObjective(0);
				if (tmpAttr != -1.0)
					listWithoutViolations.add(solution);
			}

			List<S> solutionsNotInTabuList = solutionLocator.findBestNeighbor(listWithoutViolations, IteratorUtils.toList(tabuList.iterator()));

				
			for (S s : solutionsNotInTabuList) {				
				this.nonDominatedArchive.add(s);
				tabuList.add(s);
			}
			currentSolution = nonDominatedArchive.getSolutionList().get(nonDominatedArchive.getSolutionList().size()-1);
		}
		
		return null;
	}


	public int getMaxPopulationSize() {
		return this.numberOfNeighbors;
	}

	public List<S> getNeighbors(S solution) {
		ArrayList<S> newList = new ArrayList<>();
		for (int i = 0; i < numberOfNeighbors; i++) {
			S tmpSolution = mutationOperator.execute((S) solution.copy());// TODO: how to cast properly?
//			tmpSolution.setObjective(0, 0);// TODO: reset costs to 0?
//			tmpSolution.setAttribute(0, 0);
			problem.evaluate(tmpSolution);
			newList.add(tmpSolution);
		}
		return newList;
	}

	@Override
	public String getName() {
		return "TS";
	}

	@Override
	public String getDescription() {
		return "Tabu Search Algorithm";
	}

	@Override
	public void run() {
		run(initialSolution);
	}

	@Override
	public List<S> getResult() {
		return nonDominatedArchive.getSolutionList();
	}

}
