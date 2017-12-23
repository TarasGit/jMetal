package org.uma.jmetal.algorithm.singleobjective.TS;

import java.util.Iterator;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.uma.jmetal.solution.Solution;

public final class StaticTabuList<S extends Solution<?>> implements TabuList<S> {


	private CircularFifoQueue<S> tabuList;

	public StaticTabuList(Integer size) {
		this.tabuList = new CircularFifoQueue<S>(size);
	}

	public StaticTabuList() {

	}

	@Override
	public void add(S solution) {
		tabuList.add(solution);
	}

	@Override
	public Boolean contains(S solution) {
		return tabuList.contains(solution);
	}

	@Override
	public Iterator<S> iterator() {
		return tabuList.iterator();
	}
}
