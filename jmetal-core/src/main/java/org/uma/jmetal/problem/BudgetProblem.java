package org.uma.jmetal.problem;

import java.io.Serializable;


 
public interface BudgetProblem extends Serializable {
  public boolean violateBudget(double costs, double budget);
}
