package org.uma.jmetal.solution.util;

public class DefaultBinaryIntegerPermutationSolutionConfiguration {

	private static final DefaultBinaryIntegerPermutationSolutionConfiguration instance = new DefaultBinaryIntegerPermutationSolutionConfiguration();
	
	private double probability;
	
	public DefaultBinaryIntegerPermutationSolutionConfiguration() {
		this.probability = 0.5;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public static DefaultBinaryIntegerPermutationSolutionConfiguration getInstance() {
		return instance;
	}
}
