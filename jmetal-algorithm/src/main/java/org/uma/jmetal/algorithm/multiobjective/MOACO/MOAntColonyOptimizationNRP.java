package org.uma.jmetal.algorithm.multiobjective.MOACO;
import java.io.IOException;
import java.util.stream.IntStream;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRP;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class MOAntColonyOptimizationNRP<S extends Solution<?>> {
	
	private int size;
	private Problem<S> problem;
	
	public MOAntColonyOptimizationNRP(Problem<S> problem) {
		this.size = ((NRP)problem).getNumberOfBitInVariable(0);
		this.problem = problem;
		initializePheromonLevel();
	}
	
	private double[][] pheromonLevelMatrix = null;

	public double[][] getPheramonLevelMatrix(){ 
		return pheromonLevelMatrix;
	}
	
	public MOAntColonyOptimizationNRP() throws IOException{
		initializePheromonLevel();
	}

	private void initializePheromonLevel() {
		pheromonLevelMatrix = new double[size][size];
		IntStream.range(0,  size).forEach(x -> {
			IntStream.range(0,  size).forEach(y -> pheromonLevelMatrix[x][y] = JMetalRandom.getInstance().nextDouble());
		});
	}
	
	public int getProblemSize() {
		return size;
	}
	
	public S getInitialSolution() {
		return problem.createSolution();
	}
	
	public Problem<S> getProblem() {
		return problem;
	}

}
