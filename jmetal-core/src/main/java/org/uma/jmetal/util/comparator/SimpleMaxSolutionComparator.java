package org.uma.jmetal.util.comparator;

import org.uma.jmetal.solution.Solution;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author Taras Iks
 *
 */
@SuppressWarnings("serial")
public class SimpleMaxSolutionComparator<S extends Solution<?>> implements Comparator<S>, Serializable {
  /**
   * Compares two solutions.
   *
   * @param solution1 Object representing the first solution
   * @param solution2 Object representing the second solution.
   * @return -1, or 0, or 1 if solution1 is less than, equal, or greater than solution2,
   * respectively.
   */
  @Override
  public int compare(S solution1, S solution2) {
	  if(solution1.getObjective(0) > solution2.getObjective(0))
		  return -1;
	  else
		  return 1;
  }
}
