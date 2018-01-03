package org.uma.jmetal.algorithm.singleobjective.ACO.TSP;
import java.io.IOException;
import java.util.stream.IntStream;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class AntColonyOptimizationTSP<S extends Solution<?>> {
	
	private S initialSolution;
	private int size;
	private Problem<S> problem;
	
	public AntColonyOptimizationTSP(Problem<S> problem, S initialSolution ) {
		this.size = initialSolution.getNumberOfVariables();
		this.initialSolution = initialSolution;
		this.problem = problem;
		initializePheromonLevel();
	}
	
	private Double[][] pheromonLevelMatrix = null;

	public Double[][] getPheramonLevelMatrix(){ 
		return pheromonLevelMatrix;
	}
	
	public AntColonyOptimizationTSP() throws IOException{
		initializePheromonLevel();
	}

	private void initializePheromonLevel() {
		pheromonLevelMatrix = new Double[size][size];//rename size
		IntStream.range(0,  size).forEach(x -> {
			IntStream.range(0,  size).forEach(y -> pheromonLevelMatrix[x][y] = new Double(JMetalRandom.getInstance().nextDouble()));
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
