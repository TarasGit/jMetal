package org.uma.jmetal.algorithm.singleobjective.TS;

import java.util.Iterator;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.uma.jmetal.solution.PermutationSolution;

public final class StaticTabuList implements TabuList {


	private CircularFifoQueue<PermutationSolution<Integer>> tabuList;

	public StaticTabuList(Integer size) {
		this.tabuList = new CircularFifoQueue<PermutationSolution<Integer>>(size);
	}

	public StaticTabuList() {

	}

	@Override
	public void add(PermutationSolution<Integer> solution) {
		tabuList.add(solution);
	}

	@Override
	public Boolean contains(PermutationSolution<Integer> solution) {
		return tabuList.contains(solution);
	}

	@Override
	public Iterator<PermutationSolution<Integer>> iterator() {
		return tabuList.iterator();
	}
}
