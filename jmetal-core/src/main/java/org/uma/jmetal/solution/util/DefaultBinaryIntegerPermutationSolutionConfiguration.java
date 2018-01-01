package org.uma.jmetal.solution.util;

public class DefaultBinaryIntegerPermutationSolutionConfiguration {

	private static final DefaultBinaryIntegerPermutationSolutionConfiguration instance = new DefaultBinaryIntegerPermutationSolutionConfiguration();
	
	private boolean empty;
	
	public DefaultBinaryIntegerPermutationSolutionConfiguration() {
		this.empty = false;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public static DefaultBinaryIntegerPermutationSolutionConfiguration getInstance() {
		return instance;
	}
}
