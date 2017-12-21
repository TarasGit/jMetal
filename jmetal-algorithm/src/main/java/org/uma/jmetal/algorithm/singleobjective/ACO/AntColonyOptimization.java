package org.uma.jmetal.algorithm.singleobjective.ACO;
import java.io.IOException;
import java.util.Random;
import java.util.stream.IntStream;

import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.solution.impl.DefaultIntegerPermutationSolution;
import org.uma.jmetal.solution.impl.DefaultIntegerPermutationSolution;

public class AntColonyOptimization {
	
	private DefaultIntegerPermutationSolution initialSolution;
	private int size;
	private TSP problem;
	
	public AntColonyOptimization(TSP problem) {
		this.size = problem.getNumberOfVariables();
		this.initialSolution = (DefaultIntegerPermutationSolution) problem.createSolution();
		this.problem = problem;
		initializePheromonLevel();
	}
	
	

	private AtomicDouble[][] pheromonLevelMatrix = null;
//	private double[][] distanceMatrix = null;
	
	public AtomicDouble[][] getPheramonLevelMatrix(){ 
		return pheromonLevelMatrix;
	}
	
	
	public AntColonyOptimization() throws IOException{
		initializePheromonLevel();
	}


	
	private void initializePheromonLevel() {
		pheromonLevelMatrix = new AtomicDouble[size][size];//rename size
		Random random = new Random();
		IntStream.range(0,  size).forEach(x -> {
			IntStream.range(0,  size).forEach(y -> pheromonLevelMatrix[x][y] = new AtomicDouble(random.nextDouble()));
		});
	}
	
	public int getProblemSize() {
		return size;
	}
	
	public DefaultIntegerPermutationSolution getInitialSolution() {
		return this.initialSolution;
	}
	
	public TSP getProblem() {
		return problem;
	}

}
