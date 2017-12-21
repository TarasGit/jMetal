package org.uma.jmetal.algorithm.singleobjective.TS;
 
import java.util.List;
import java.util.Optional;

import org.uma.jmetal.solution.PermutationSolution; 
  
public interface BestNeighborSolutionLocator { 
 
 Optional<PermutationSolution<Integer>> findBestNeighbor(List<PermutationSolution<Integer>> neighborsSolutions, List<PermutationSolution<Integer>> solutionsInTabu); 
  
}
