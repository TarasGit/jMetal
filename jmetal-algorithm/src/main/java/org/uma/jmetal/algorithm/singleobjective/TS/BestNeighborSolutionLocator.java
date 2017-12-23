package org.uma.jmetal.algorithm.singleobjective.TS;
 
import java.util.List;
import java.util.Optional;

import org.uma.jmetal.solution.Solution; 
  
public interface BestNeighborSolutionLocator<S extends Solution<?>> { 
 
 Optional<S> findBestNeighbor(List<S> neighborsSolutions, List<S> solutionsInTabu); 
  
}
