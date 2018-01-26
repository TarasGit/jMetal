package org.uma.jmetal.algorithm.multiobjective.MOACO;
import java.io.IOException;
import java.util.stream.IntStream;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.NRP;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class MOAntColonyOptimizationNRP<S extends Solution<?>> {
	
	private S initialSolution;
	private int size;
	private Problem<S> problem;
	
	public MOAntColonyOptimizationNRP(Problem<S> problem, S initialSolution ) {
		this.size = ((NRP)problem).getNumberOfBitInVariable(0); //;initialSolution.getNumberOfVariables();
		this.initialSolution = initialSolution;
		this.problem = problem;
		initializePheromonLevel();
	}
	
	private double[][] pheromonLevelMatrix = null;

	public double[][] getPheramonLevelMatrix(){ 
		//System.out.println("PheromonM: " + this.pheromonLevelMatrix[0][0]);
		return pheromonLevelMatrix;
	}
	
	public MOAntColonyOptimizationNRP() throws IOException{
		initializePheromonLevel();
	}

	private void initializePheromonLevel() {
		pheromonLevelMatrix = new double[size][size];//rename size
		IntStream.range(0,  size).forEach(x -> {
			IntStream.range(0,  size).forEach(y -> pheromonLevelMatrix[x][y] = JMetalRandom.getInstance().nextDouble());
		});
		System.out.println("Initialized PM:"  + this.pheromonLevelMatrix[0][0]);
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
