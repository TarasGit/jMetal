package org.uma.jmetal.operator.impl.crossover;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.BoundedRandomGenerator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

/**
 * This class implements a single point crossover operator.
 *
 * @author Taras Iks.
 */
@SuppressWarnings("serial")
public class BinarySinglePointCrossover implements CrossoverOperator<PermutationSolution<Integer>> {
  private double crossoverProbability ;
  private RandomGenerator<Double> crossoverRandomGenerator ;
  private BoundedRandomGenerator<Integer> pointRandomGenerator ;

  /** Constructor */
  public BinarySinglePointCrossover(double crossoverProbability) {
	  this(crossoverProbability, () -> JMetalRandom.getInstance().nextDouble(), (a, b) -> JMetalRandom.getInstance().nextInt(a, b));
  }

  /** Constructor */
  public BinarySinglePointCrossover(double crossoverProbability, RandomGenerator<Double> randomGenerator) {
	  this(crossoverProbability, randomGenerator, BoundedRandomGenerator.fromDoubleToInteger(randomGenerator));
  }

  /** Constructor */
  public BinarySinglePointCrossover(double crossoverProbability, RandomGenerator<Double> crossoverRandomGenerator, BoundedRandomGenerator<Integer> pointRandomGenerator) {
    if (crossoverProbability < 0) {
      throw new JMetalException("Crossover probability is negative: " + crossoverProbability) ;
    }
    this.crossoverProbability = crossoverProbability;
    this.crossoverRandomGenerator = crossoverRandomGenerator ;
    this.pointRandomGenerator = pointRandomGenerator ;
  }

  /* Getter */
  public double getCrossoverProbability() {
    return crossoverProbability;
  }

  /* Setter */
  public void setCrossoverProbability(double crossoverProbability) {
    this.crossoverProbability = crossoverProbability;
  }

  @Override
  public List<PermutationSolution<Integer>> execute(List<PermutationSolution<Integer>> solutions) {
    if (solutions == null) {
      throw new JMetalException("Null parameter") ;
    } else if (solutions.size() != 2) {
      throw new JMetalException("There must be two parents instead of " + solutions.size()) ;
    }

    return doCrossover(crossoverProbability, solutions.get(0), solutions.get(1)) ;
  }

  /**
   * Perform the crossover operation.
   *
   * @param probability Crossover setProbability
   * @param parent1     The first parent
   * @param parent2     The second parent
   * @return An array containing the two offspring
   */
  public List<PermutationSolution<Integer>> doCrossover(double probability, PermutationSolution<Integer> parent1, PermutationSolution<Integer> parent2)  {
    List<PermutationSolution<Integer>> offspring = new ArrayList<>(2);
    offspring.add((PermutationSolution<Integer>) parent1.copy()) ;
    offspring.add((PermutationSolution<Integer>) parent2.copy()) ;

    if (crossoverRandomGenerator.getRandomValue() < probability) {
      // 1. Get the total number of bits
      int totalNumberOfBits = parent1.getNumberOfVariables();

      // 2. Calculate the point to make the crossover
      int crossoverPoint = pointRandomGenerator.getRandomValue(0, totalNumberOfBits - 1);


      for (int i = 0; i < totalNumberOfBits; i++) {//TODO: check it.
        if(i < crossoverPoint) {
        	offspring.get(1).setVariableValue(i, parent1.getVariableValue(i));
        }else {
        	offspring.get(0).setVariableValue(i, parent2.getVariableValue(i));
        }
      }
    }
    return offspring ;
  }

  @Override
  public int getNumberOfRequiredParents() {
    return 2 ;
  }

  @Override
  public int getNumberOfGeneratedChildren() {
    return 2;
  }
}
