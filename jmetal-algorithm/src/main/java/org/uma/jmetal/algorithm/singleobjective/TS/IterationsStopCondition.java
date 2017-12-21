package org.uma.jmetal.algorithm.singleobjective.TS;


public class IterationsStopCondition implements StopCondition { 
 
 private final Integer maxIterations; 
  
 public IterationsStopCondition(Integer maxIterations) { 
  this.maxIterations = maxIterations; 
 } 
  
 @Override 
 public Boolean mustStop(Integer currentIteration) { 
  return currentIteration >= maxIterations; 
 } 
 
}
