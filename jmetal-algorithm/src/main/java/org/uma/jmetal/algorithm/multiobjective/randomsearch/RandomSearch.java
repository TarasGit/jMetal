package org.uma.jmetal.algorithm.multiobjective.randomsearch;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchiveForMinMax;

/**
 * This class implements a simple random search algorithm.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class RandomSearch<S extends Solution<?>> implements Algorithm<List<S>> {
	private Problem<S> problem;
	private int maxEvaluations;
	private double initialSolutionProbablity;
	private MutationOperator<S> mutationOperator;


	NonDominatedSolutionListArchiveForMinMax<S> nonDominatedArchive;

	/** Constructor */
	public RandomSearch(Problem<S> problem, int maxEvaluations, double initialPopulationProbability, MutationOperator<S> mutationOperator) {
		this.problem = problem;
		this.maxEvaluations = maxEvaluations;
		nonDominatedArchive = new NonDominatedSolutionListArchiveForMinMax<S>();
		this.initialSolutionProbablity = initialPopulationProbability;
		this.mutationOperator = mutationOperator;		
	}

	/* Getter */
	public int getMaxEvaluations() {
		return maxEvaluations;
	}

	@Override
	public void run() {
		DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance()
				.setProbability(this.initialSolutionProbablity);
		S newSolution = problem.createSolution();
		for (int i = 0; i < maxEvaluations; i++) {
			newSolution = mutationOperator.execute((S) newSolution.copy());//problem.createSolution();
			problem.evaluate(newSolution);
			System.out.println("R:" + newSolution.getObjective(0));
			if (newSolution.getObjective(0) == -1)
				continue;
			nonDominatedArchive.add(newSolution);
		}
	}

	@Override
	public List<S> getResult() {
		List<S> solutions = nonDominatedArchive.getSolutionList();
		if (problem.getNumberOfObjectives() == 2) {
			for (S s : solutions) {
				s.setObjective(0, s.getObjective(0) * -1);
				s.setObjective(1, s.getObjective(1) * -1);
			}
		}else{
			for (S s : solutions) {
				s.setObjective(0, s.getObjective(0));
			}
		}
		return solutions;
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
