package org.uma.jmetal.algorithm.singleobjective.TS;

import org.uma.jmetal.solution.Solution;

public interface TabuList<S extends Solution<?>> extends Iterable<S> { 
  
 void add(S solution); 
  
 Boolean contains(S solution); 
}
