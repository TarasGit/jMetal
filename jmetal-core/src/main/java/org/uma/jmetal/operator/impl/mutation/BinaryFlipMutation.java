package org.uma.jmetal.operator.impl.mutation;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

/**
 * @author Taras Iks <ikstaras@gmail.com>
 * @version 1.0
 *
 *          This class implements a bit flip mutation operator.
 */
@SuppressWarnings("serial")
public class BinaryFlipMutation<S extends Solution> implements MutationOperator<S> {// TODO: do something with raw type
																					// of Solution.
	private double mutationProbability;
	private RandomGenerator<Double> randomGenerator;

	/** Constructor */
	public BinaryFlipMutation(double mutationProbability) {
		this(mutationProbability, () -> JMetalRandom.getInstance().nextDouble());
	}

	/** Constructor */
	public BinaryFlipMutation(double mutationProbability, RandomGenerator<Double> randomGenerator) {
		if (mutationProbability < 0) {
			throw new JMetalException("Mutation probability is negative: " + mutationProbability);
		}
		this.mutationProbability = mutationProbability;
		this.randomGenerator = randomGenerator;
	}

	/* Getter */
	public double getMutationProbability() {
		return mutationProbability;
	}

	/* Setters */
	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	/**
	 * Perform the mutation operation
	 *
	 * @param probability
	 *            Mutation setProbability
	 * @param solution
	 *            The solution to mutate
	 */
	public void doMutation(S solution) {
		int pos = (int) (randomGenerator.getRandomValue() * solution.getNumberOfVariables());// TODO: check
		if (((int) solution.getVariableValue(pos)) == 1)
			solution.setVariableValue(pos, 0);
		else
			solution.setVariableValue(pos, 1);
	}

	@Override
	public S execute(S solution) {
		if (null == solution) {
			throw new JMetalException("Null parameter");
		}

		doMutation(solution);
		return solution;
	}
}
