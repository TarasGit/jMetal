package org.uma.jmetal.algorithm.multiobjective.MOTS;

import org.uma.jmetal.solution.Solution;

public interface MOTabuList<S extends Solution<?>> extends Iterable<S> { 
  
 void add(S solution); 
  
 Boolean contains(S solution); 
}
