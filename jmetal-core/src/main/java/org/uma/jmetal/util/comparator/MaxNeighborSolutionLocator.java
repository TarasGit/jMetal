package org.uma.jmetal.util.comparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.uma.jmetal.solution.Solution;

public class MaxNeighborSolutionLocator<S extends Solution<?>> implements BestNeighborSolutionLocator<S> {

	@Override
	public Optional<S> findBestNeighbor(
			List<S> neighborsSolutions,
			final List<S> solutionsInTabu) {
		// remove any neighbor that is in tabu list
		CollectionUtils.filterInverse(neighborsSolutions, new Predicate<S>() {
			@Override
			public boolean evaluate(S neighbor) {
				return solutionsInTabu.contains(neighbor);
			}
		});

		// sort the neighbors
		Collections.sort(neighborsSolutions, new Comparator<S>() {
			@Override
			public int compare(S a, S b) {
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
