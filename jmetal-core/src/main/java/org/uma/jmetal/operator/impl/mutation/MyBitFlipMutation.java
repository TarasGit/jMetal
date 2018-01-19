package org.uma.jmetal.operator.impl.mutation;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @version 1.0
 *
 *          This class implements a bit flip mutation operator.
 */
@SuppressWarnings("serial")
public class MyBitFlipMutation implements MutationOperator<BinarySolution> {
	private double mutationProbability;
	private RandomGenerator<Double> randomGenerator;

	/** Constructor */
	public MyBitFlipMutation(double mutationProbability) {
		this(mutationProbability, () -> JMetalRandom.getInstance().nextDouble());
	}

	/** Constructor */
	public MyBitFlipMutation(double mutationProbability, RandomGenerator<Double> randomGenerator) {
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

	/** Execute() method */
	@Override
	public BinarySolution execute(BinarySolution solution) {
		if (null == solution) {
			throw new JMetalException("Null parameter");
		}

		doMutation(mutationProbability, solution);
		return solution;
	}

	/**
	 * Perform the mutation operation
	 *
	 * @param probability
	 *            Mutation setProbability
	 * @param solution
	 *            The solution to mutate
	 */
	// public void doMutation(double probability, BinarySolution solution) {
	// for (int i = 0; i < solution.getNumberOfVariables(); i++) {
	// for (int j = 0; j < solution.getVariableValue(i).getBinarySetLength(); j++) {
	// if (randomGenerator.getRandomValue() <= probability) {
	// solution.getVariableValue(i).flip(j);
	// }
	// }
	// }
	// }

	public void doMutation(double probability, BinarySolution solution) {
		double rand = randomGenerator.getRandomValue();

		int pos1 = 0, pos2 = 0;
		if (rand > probability) {
			int pos = (int) (randomGenerator.getRandomValue() * solution.getNumberOfBits(0));// TODO: check the
			solution.getVariableValue(0).flip(pos);
		
		} else {
			int permutationLength;
			permutationLength = solution.getNumberOfBits(0);
			if ((permutationLength != 0) && (permutationLength != 1)) {
				if (randomGenerator.getRandomValue() < mutationProbability) {
					pos1 = (int) (randomGenerator.getRandomValue() * (permutationLength - 1));
					pos2 = (int) (randomGenerator.getRandomValue() * (permutationLength - 1));

					while (pos1 == pos2) {
						if (pos1 == (permutationLength - 1))
							pos2 = (int) (randomGenerator.getRandomValue() * (permutationLength - 2));
						else
							pos2 = (int) (randomGenerator.getRandomValue() * (permutationLength - 1));
					}

					boolean temp = solution.getVariableValue(0).get(pos1);
					if (solution.getVariableValue(0).get(pos2)) {
						solution.getVariableValue(0).set(pos1);
						;
					} else {
						solution.getVariableValue(0).clear(pos1);
					}
					if (temp) {
						solution.getVariableValue(0).set(pos2);
						;
					} else {
						solution.getVariableValue(0).clear(pos2);
					}
				}
			}
		}
	}
}
