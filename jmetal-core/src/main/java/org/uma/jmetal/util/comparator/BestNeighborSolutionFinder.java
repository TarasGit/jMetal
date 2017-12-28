package org.uma.jmetal.util.comparator;
 
import java.util.List;
import java.util.Optional;

import org.uma.jmetal.solution.Solution; 
  
public interface BestNeighborSolutionFinder<S extends Solution<?>> { 
 
 Optional<S> findBestNeighbor(List<S> neighborsSolutions, List<S> solutionsInTabu); 
  
}
