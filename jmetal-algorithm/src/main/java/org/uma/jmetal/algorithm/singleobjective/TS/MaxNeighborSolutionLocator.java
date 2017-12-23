package org.uma.jmetal.algorithm.singleobjective.TS;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.uma.jmetal.solution.PermutationSolution;

public class MaxNeighborSolutionLocator implements BestNeighborSolutionLocator {

	@Override
	public Optional<PermutationSolution<Integer>> findBestNeighbor(
			List<PermutationSolution<Integer>> neighborsSolutions,
			final List<PermutationSolution<Integer>> solutionsInTabu) {
		// remove any neighbor that is in tabu list
		CollectionUtils.filterInverse(neighborsSolutions, new Predicate<PermutationSolution<Integer>>() {
			@Override
			public boolean evaluate(PermutationSolution<Integer> neighbor) {
				return solutionsInTabu.contains(neighbor);
			}
		});

		// sort the neighbors
		Collections.sort(neighborsSolutions, new Comparator<PermutationSolution<Integer>>() {
			@Override
			public int compare(PermutationSolution<Integer> a, PermutationSolution<Integer> b) {
				if (a.getObjective(0) == b.getObjective(0))
					return 0;
				else if (a.getObjective(0) > b.getObjective(0))
					return -1;
				else
					return 1;
			}
		});

		if (neighborsSolutions.isEmpty())
			return Optional.empty();
		// get the neighbor with lowest value
		return Optional.of(neighborsSolutions.get(0));
	}

}
