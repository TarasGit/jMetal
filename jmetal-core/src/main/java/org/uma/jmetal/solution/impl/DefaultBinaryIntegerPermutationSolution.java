package org.uma.jmetal.solution.impl;

import java.util.HashMap;

import org.uma.jmetal.problem.PermutationProblem;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;

/**
 * Defines an implementation of solution composed of a permuation of integers
 *
 * @author Taras Iks <ikstaras@gmail.com>
 */
@SuppressWarnings("serial")
public class DefaultBinaryIntegerPermutationSolution extends AbstractGenericSolution<Integer, PermutationProblem<?>>
		implements PermutationSolution<Integer> {

	/** Constructor */
	public DefaultBinaryIntegerPermutationSolution(PermutationProblem<?> problem) {
		super(problem);

		boolean empty = DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().isEmpty();
		if (!empty) {
			for (int i = 0; i < getNumberOfVariables(); i++) {
				if (randomGenerator.nextDouble() > 0.9)//TODO XXX: change to 0.5.
					setVariableValue(i, 1);
				else
					setVariableValue(i, 0);
			}
		} else {
			for (int i = 0; i < getNumberOfVariables(); i++) {
				setVariableValue(i, 0);
			}
		}
	}

	/** Copy Constructor */
	public DefaultBinaryIntegerPermutationSolution(DefaultBinaryIntegerPermutationSolution solution) {
		super(solution.problem);
		for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
			setObjective(i, solution.getObjective(i));
		}

		for (int i = 0; i < problem.getNumberOfVariables(); i++) {
			setVariableValue(i, solution.getVariableValue(i));
		}
		attributes = new HashMap<Object, Object>(solution.attributes);
	}

	@Override
	public String getVariableValueString(int index) {
		return getVariableValue(index).toString();
	}

	@Override
	public DefaultBinaryIntegerPermutationSolution copy() {
		return new DefaultBinaryIntegerPermutationSolution(this);
	}
}
