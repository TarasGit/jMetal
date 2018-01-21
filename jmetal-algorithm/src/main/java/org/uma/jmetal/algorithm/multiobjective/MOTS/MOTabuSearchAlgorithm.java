package org.uma.jmetal.algorithm.multiobjective.MOTS;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.selection.RankingAndCrowdingSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
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
	private int numberOfNeighbors = 5000;
	private MutationOperator<S> mutationOperator;
	private List<S> endResult;
	private List<S> newEndResult;
	private S initialSolution;
	private Comparator<Double> comparator;
	Problem<S> problem;

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
		this.endResult = new ArrayList<>();
		this.newEndResult = new ArrayList<>();
	}

	public S run(S initialSolution) {
		S bestSolution = initialSolution;
		problem.evaluate(bestSolution);
		System.out.println(bestSolution.getVariableValueString(0));
		System.out.println(
				"Initial fitness: " + bestSolution.getObjective(0) + " / " + "costs: " + bestSolution.getObjective(1));
		System.out.println(bestSolution);

		S currentSolution = initialSolution;

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

			// if (listWithoutViolations.isEmpty()) {
			// Random r = new Random();
			// currentSolution =
			// listWithoutViolations.get(r.nextInt(listWithoutViolations.size()));
			// continue; // TODO: break, if we get invalid solutions-> better computation
			// time, worse
			// // quality of results.
			// }

			List<S> solutionsNotInTabuList = solutionLocator.findBestNeighbor(listWithoutViolations, solutionsInTabu);

			endResult = replacement(solutionsNotInTabuList, endResult);
			
//			newEndResult = new ArrayList<>();
//			for(S s : endResult) {
//				int rank = (int) s.getAttribute(DominanceRanking.class);//Crowding Distance
//					if(rank == 0) {
//						newEndResult.add(s);
//						//System.out.println("Rand0 : " + s);
//					}else {
//						break;
//					}
//					
//			}
//
//			System.out.println(newEndResult.size());
//			//solutionsNotInTabuList = SolutionListUtils.getNondominatedSolutions(solutionsNotInTabuList);
			//endResult.addAll(solutionsNotInTabuList);

//			Collections.sort(solutionsNotInTabuList, new Comparator<S>() {
//
//				@Override
//				public int compare(S o1, S o2) {
//					if (o1.getObjective(0) > o2.getObjective(0))
//						return 1;
//					else if (o1.getObjective(0) < o2.getObjective(0))
//						return -1;
//					else
//						return 0;
//				}
//
//			});
//			
			currentSolution = endResult.get(0);
			System.out.println(currentSolution.getObjective(0));

			for (S s : endResult)
				tabuList.add(s);
		}
		
		return endResult.get(0);//TODO: remove it
	}

	protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
		List<S> jointPopulation = new ArrayList<>();
		jointPopulation.addAll(population);
		jointPopulation.addAll(offspringPopulation);

		RankingAndCrowdingSelection<S> rankingAndCrowdingSelection;
		rankingAndCrowdingSelection = new RankingAndCrowdingSelection<S>(getMaxPopulationSize());

		return rankingAndCrowdingSelection.execute(jointPopulation);
	}

	public int getMaxPopulationSize() {
		return this.numberOfNeighbors;
	}

	public List<S> getNeighbors(S solution) {
		ArrayList<S> newList = new ArrayList<>();
		for (int i = 0; i < numberOfNeighbors; i++) {
			S tmpSolution = mutationOperator.execute((S) solution.copy());// TODO: how to cast properly?
			tmpSolution.setObjective(0, 0);// TODO: reset costs to 0?
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
		run(initialSolution);
	}

	@Override
	public List<S> getResult() {
		return SolutionListUtils.getNondominatedSolutions(endResult);
	}

}
