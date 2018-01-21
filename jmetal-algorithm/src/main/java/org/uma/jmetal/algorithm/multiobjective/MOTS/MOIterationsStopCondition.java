package org.uma.jmetal.algorithm.multiobjective.MOTS;


public class MOIterationsStopCondition implements MOStopCondition { 
 
 private final Integer maxIterations; 
  
 public MOIterationsStopCondition(Integer maxIterations) { 
  this.maxIterations = maxIterations; 
 } 
  
 @Override 
 public Boolean mustStop(Integer currentIteration) { 
  return currentIteration >= maxIterations; 
 } 
 
}
