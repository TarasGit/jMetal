package org.uma.jmetal.algorithm.singleobjective.TS;

import org.uma.jmetal.solution.PermutationSolution;

public interface TabuList extends Iterable<PermutationSolution<Integer>> { 
  
 void add(PermutationSolution<Integer> solution); 
  
 Boolean contains(PermutationSolution<Integer> solution); 
}
