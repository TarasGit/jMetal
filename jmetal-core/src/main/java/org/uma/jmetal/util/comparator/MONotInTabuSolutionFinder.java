package org.uma.jmetal.util.comparator;
 
import java.util.List;
import java.util.Optional;

import org.uma.jmetal.solution.Solution; 
  
public interface MONotInTabuSolutionFinder<S extends Solution<?>> { 
 
 List<S> findBestNeighbor(List<S> neighborsSolutions, List<S> solutionsInTabu); 
  
}
