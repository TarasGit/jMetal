package org.uma.jmetal.problem.singleobjective;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.math3.util.Pair;
import org.uma.jmetal.solution.PermutationSolution;

public interface NRP {
	void readProblem(String file) throws IOException;
	void computeProfitMatrix();
	double getDistanceProfit(int i, int j);
	double getEvaluatedProfit(PermutationSolution<Integer> solution);
	double getEvaluatedCosts(PermutationSolution<Integer> solution);
	void addToSetIfUnique(int value, Set<Integer> setOfRequirements);
	void addAllDependenciesToSetRecursively(int requirement, Set<Integer> setOfRequirements);
	Double getCostFromRequirement(int localRequirement);
	List<Integer> getDependencies(Integer requirement);
	Optional<Pair<Integer, Integer>> getRequirementPosition(int requirement);
	int computeAllCosts();
	int getCosts();
	double getBudget();
	void setCostFactor(double costFactor);
	double getCostsOfRequirement(int x, int y);
}
