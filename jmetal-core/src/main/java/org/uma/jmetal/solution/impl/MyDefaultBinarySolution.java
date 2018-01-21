package org.uma.jmetal.solution.impl;

import java.util.HashMap;
import java.util.Random;

import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.util.DefaultBinaryIntegerPermutationSolutionConfiguration;
import org.uma.jmetal.util.binarySet.BinarySet;

/**
 * Defines an implementation of a binary solution
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class MyDefaultBinarySolution extends AbstractGenericSolution<BinarySet, BinaryProblem> implements BinarySolution {

	/** Constructor */
	public MyDefaultBinarySolution(BinaryProblem problem) {
		super(problem);

		initializeBinaryVariables();
		initializeObjectiveValues();
	}

	/** Copy constructor */
	public MyDefaultBinarySolution(MyDefaultBinarySolution solution) {
		super(solution.problem);

		for (int i = 0; i < problem.getNumberOfVariables(); i++) {
			setVariableValue(i, (BinarySet) solution.getVariableValue(i).clone());
		}

		for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
			setObjective(i, solution.getObjective(i));
		}

		attributes = new HashMap<Object, Object>(solution.attributes);
	}

	private BinarySet createNewBitSet(int numberOfBits) {
		BinarySet bitSet = new BinarySet(numberOfBits);

		for (int i = 0; i < numberOfBits; i++) {
			double rnd = randomGenerator.nextDouble();
			if (rnd < 0.5) {
				bitSet.set(i);
			} else {
				bitSet.clear(i);
			}
		}
		return bitSet;
	}

	@Override
	public int getNumberOfBits(int index) {
		return getVariableValue(index).getBinarySetLength();
	}

	@Override
	public MyDefaultBinarySolution copy() {
		return new MyDefaultBinarySolution(this);
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

	private void initializeBinaryVariables() {//TODO: Taras, Create own class for this type.
//		for (int i = 0; i < problem.getNumberOfVariables(); i++) {
//			setVariableValue(i, createNewBitSet(problem.getNumberOfBits(i)));
//		}
		double probability = DefaultBinaryIntegerPermutationSolutionConfiguration.getInstance().getProbability();
		Random r = new Random();
		BinarySet bitSet = new BinarySet(problem.getNumberOfBits(0));
		for(int i=0;i<problem.getNumberOfBits(0);i++) {
			if(r.nextDouble() > probability)
				bitSet.set(i);
		}
		setVariableValue(0, bitSet);
		//System.out.println("Initial Solution BitSet: " + this.getVariableValue(0).toString());
		
	}
	
//	@Override
//	public String toString() {//TODO: changed by Taras, create it's own class for this kind of solutions.
//		String result = "";
//		for (int i = 0; i < getVariableValue(0).getBinarySetLength(); i++) {
//			if (getVariableValue(0).get(i)) {
//				result += "1";
//			} else {
//				result += "0";
//			}
//		}
//		return "Fitness: " + this.getObjective(0) + " Costs: " + this.getObjective(1) + " CD: " + this.getAttribute(0) + " Rank: " + this.getAttribute(1) + " " +  result + "\n";
//	}
}
