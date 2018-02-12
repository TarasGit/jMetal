package org.uma.jmetal.algorithm.multiobjective.MOTS;


public class MOIterationsStopCondition { 
 
 private final Integer maxIterations; 
  
 public MOIterationsStopCondition(Integer maxIterations) { 
  this.maxIterations = maxIterations; 
 } 
  
 public Boolean mustStop(Integer currentIteration) { 
  return currentIteration >= maxIterations; 
 } 
 
}
