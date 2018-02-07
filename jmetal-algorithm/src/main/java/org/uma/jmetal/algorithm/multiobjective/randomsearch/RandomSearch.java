package org.uma.jmetal.algorithm.multiobjective.randomsearch;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchiveForMinMax;

/**
 * This class implements a simple random search algorithm.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class RandomSearch<S extends Solution<?>> implements Algorithm<List<S>> {
  private Problem<S> problem ;
  private int maxEvaluations ;
  NonDominatedSolutionListArchiveForMinMax<S> nonDominatedArchive ;

  /** Constructor */
  public RandomSearch(Problem<S> problem, int maxEvaluations) {
    this.problem = problem ;
    this.maxEvaluations = maxEvaluations ;
    nonDominatedArchive = new NonDominatedSolutionListArchiveForMinMax<S>();
  }

  /* Getter */
  public int getMaxEvaluations() {
    return maxEvaluations;
  }

  @Override public void run() {
    S newSolution;
    for (int i = 0; i < maxEvaluations; i++) {
      newSolution = problem.createSolution() ;
      problem.evaluate(newSolution);
      System.out.println("R:"  + newSolution.getObjective(0));
      if(newSolution.getObjective(0) == -1)
    	  continue;
      nonDominatedArchive.add(newSolution);
    }
  }

  @Override public List<S> getResult() {
	    return nonDominatedArchive.getSolutionList();
  }

  @Override public String getName() {
    return "RS" ;
  }

  @Override public String getDescription() {
    return "Multi-objective random search algorithm" ;
  }
} 
