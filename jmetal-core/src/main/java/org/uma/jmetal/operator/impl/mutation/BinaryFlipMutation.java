package org.uma.jmetal.operator.impl.mutation;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.PermutationSolution;
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
public class BinaryFlipMutation<S extends PermutationSolution<Integer>> implements MutationOperator<S> {
																				
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
	public void doMutation(double probability, S solution) {
		double rand = randomGenerator.getRandomValue();
		//System.out.println(":" + rand);
		int pos1 = 0, pos2 = 0;
		if (rand > 0.5) {
			
			int pos = (int) (randomGenerator.getRandomValue() * solution.getNumberOfVariables());// TODO: check the
																									// distribution 0 <=
																									// x <= 1
			if (((int) solution.getVariableValue(pos)) == 1) {
				solution.setVariableValue(pos, 0);
			} else {
				solution.setVariableValue(pos, 1);
			}
			
			
		} else {
			int permutationLength;
			permutationLength = solution.getNumberOfVariables();

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

					Object temp = (Object) solution.getVariableValue(pos1);
					solution.setVariableValue(pos1, solution.getVariableValue(pos2));
					solution.setVariableValue(pos2, (Integer)temp);
				}
			}
		}
	}

	@Override
	public S execute(S solution) {
		if (null == solution) {
			throw new JMetalException("Null parameter");
		}

		doMutation(mutationProbability, solution);
		return solution;
	}
}
