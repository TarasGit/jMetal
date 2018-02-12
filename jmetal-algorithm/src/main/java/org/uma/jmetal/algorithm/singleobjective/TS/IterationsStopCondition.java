package org.uma.jmetal.algorithm.singleobjective.TS;


public class IterationsStopCondition{ 
 
 private final Integer maxIterations; 
  
 public IterationsStopCondition(Integer maxIterations) { 
  this.maxIterations = maxIterations; 
 } 
  
 public Boolean mustStop(Integer currentIteration) { 
  return currentIteration >= maxIterations; 
 } 
 
}
