package org.uma.jmetal.algorithm.multiobjective.MOTS;

import java.util.Iterator;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.uma.jmetal.solution.Solution;

public final class MOStaticTabuList<S extends Solution<?>> implements MOTabuList<S> {


	private CircularFifoQueue<S> tabuList;

	public MOStaticTabuList(Integer size) {
		this.tabuList = new CircularFifoQueue<S>(size);
	}

	public MOStaticTabuList() {

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
