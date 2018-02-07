package org.uma.jmetal.solution.impl;

import java.util.HashMap;

import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.binarySet.BinarySet;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * Defines an implementation of a binary solution
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
@SuppressWarnings("serial")
public class DefaultBinarySolutionWithConfiguration extends AbstractGenericSolution<BinarySet, BinaryProblem>
		implements BinarySolution {

	/** Constructor */
	public DefaultBinarySolutionWithConfiguration(BinaryProblem problem) {
		super(problem);

		initializeBinaryVariables();
		initializeObjectiveValues();
	}

	/** Copy constructor */
	public DefaultBinarySolutionWithConfiguration(DefaultBinarySolutionWithConfiguration solution) {
		super(solution.problem);

		for (int i = 0; i < problem.getNumberOfVariables(); i++) {
			setVariableValue(i, (BinarySet) solution.getVariableValue(i).clone());
		}

		for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
			setObjective(i, solution.getObjective(i));
		}

		attributes = new HashMap<Object, Object>(solution.attributes);
	}

	@Override
	public int getNumberOfBits(int index) {
		return getVariableValue(index).getBinarySetLength();
	}

	@Override
	public DefaultBinarySolutionWithConfiguration copy() {
		return new DefaultBinarySolutionWithConfiguration(this);
	}

	@Override
	public int getTotalNumberOfBits() {
		int sum = 0;
		for (int i = 0; i < getNumberOfVariables(); i++) {
			sum += getVariableValue(i).getBinarySetLength();
		}

		return sum;
	}

	@Override
	public String getVariableValueString(int index) {
		String result = "";
		for (int i = 0; i < getVariableValue(index).getBinarySetLength(); i++) {
			if (getVariableValue(index).get(i)) {
				result += "1";
			} else {
				result += "0";
			}
		}
		return result;
	}

	private void initializeBinaryVariables() {
		double probability = DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().getProbability();
		
		JMetalRandom r = JMetalRandom.getInstance();
		BinarySet bitSet = new BinarySet(problem.getNumberOfBits(0));
		for (int i = 0; i < problem.getNumberOfBits(0); i++) {
			if (r.nextDouble() > probability)
				bitSet.set(i);
		}
		setVariableValue(0, bitSet);

	}
}
