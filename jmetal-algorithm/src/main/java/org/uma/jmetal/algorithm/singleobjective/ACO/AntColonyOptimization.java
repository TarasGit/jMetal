package org.uma.jmetal.algorithm.singleobjective.ACO;
import java.io.IOException;
import java.util.stream.IntStream;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.DefaultIntegerPermutationSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class AntColonyOptimization<S extends Solution<?>> {
	
	private DefaultIntegerPermutationSolution initialSolution;
	private int size;
	private Problem<S> problem;
	
	public AntColonyOptimization(Problem<S> problem, DefaultIntegerPermutationSolution initialSolution ) {
		this.size = initialSolution.getNumberOfVariables();
		this.initialSolution = initialSolution;
		this.problem = problem;
		initializePheromonLevel();
	}
	
	private AtomicDouble[][] pheromonLevelMatrix = null;

	public AtomicDouble[][] getPheramonLevelMatrix(){ 
		return pheromonLevelMatrix;
	}
	
	public AntColonyOptimization() throws IOException{
		initializePheromonLevel();
	}

	private void initializePheromonLevel() {
		pheromonLevelMatrix = new AtomicDouble[size][size];//rename size
		IntStream.range(0,  size).forEach(x -> {
			IntStream.range(0,  size).forEach(y -> pheromonLevelMatrix[x][y] = new AtomicDouble(JMetalRandom.getInstance().nextDouble()));
		});
	}
	
	public int getProblemSize() {
		return size;
	}
	
	public DefaultIntegerPermutationSolution getInitialSolution() {
		return this.initialSolution;
	}
	
	public Problem<S> getProblem() {
		return problem;
	}

}
