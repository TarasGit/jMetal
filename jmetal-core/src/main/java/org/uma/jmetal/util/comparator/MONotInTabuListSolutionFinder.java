package org.uma.jmetal.util.comparator;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.uma.jmetal.solution.Solution;

public class MONotInTabuListSolutionFinder<S extends Solution<?>> implements MONotInTabuSolutionFinder<S> {

	@Override
	public List<S> findBestNeighbor(
			List<S> neighborsSolutions,
			final List<S> solutionsInTabu) {
		// remove any neighbor that is in tabu list
		CollectionUtils.filterInverse(neighborsSolutions, new Predicate<S>() {
			@Override
			public boolean evaluate(S neighbor) {
				return solutionsInTabu.contains(neighbor);
			}
		});
		return neighborsSolutions;
	}

}
