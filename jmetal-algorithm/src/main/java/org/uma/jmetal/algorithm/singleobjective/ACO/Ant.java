package org.uma.jmetal.algorithm.singleobjective.ACO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.DefaultIntegerPermutationSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class Ant<S extends Solution<?>> implements Callable<Ant<S>> {
	
	public static final boolean D = false;//Debug
	
	public static final double ALPHA = 0.01;//importance of pheramon trail, x >= 0
	public static final double BETA = 9.5;//importance between source and destination, x >= 1
	
	public static final double Q = 0.0005;//feramon deposited level, 0<=x<=1
	public static final double RHO = 0.2;//feramon avapouration level, 0<=x<=1

	private AntColonyOptimization<S> aco;
	private int antNumb;
	private DefaultIntegerPermutationSolution route = null;
	
	public DefaultIntegerPermutationSolution getSolution() { return route; }
	
	static int invalidCityIndex = -1;
	
	private int numbOfCities;
	
	public Ant(AntColonyOptimization<S> aco, int antNumb) {
		this.aco = aco;
		this.antNumb = antNumb;
		this.numbOfCities = aco.getProblemSize();
		
	}
	
	@Override
	public Ant<S> call() throws Exception {
		int originatingCityIndex = ThreadLocalRandom.current().nextInt(numbOfCities);
		DefaultIntegerPermutationSolution routeCities = aco.getInitialSolution();//should be null ArrayList solution
		//IntStream.range(0, numbOfCities).forEach(x -> routeCities.setVariableValue(x, null));//TODO XXX: should be set to [null?], because otherwise some values are not valid -> result is
																							//smaller because some cities are not on the route. How is it solved in ACO???
		routeCities.setObjective(0, 0);
		
		HashMap<Integer, Boolean> visitedCities = new HashMap<Integer, Boolean>(numbOfCities);
		IntStream.range(0, numbOfCities).forEach(x -> visitedCities.put(x, false)); //visitedCities.put(Driver.initialRoute.get(x).getName(), false));
		int numbOfVisitedCities = 0;
		visitedCities.put(originatingCityIndex, true);       //initialRoute.get(originatingCityIndex).getName(), true);
		double routeDistance = 0.0;
		int x = originatingCityIndex;
		int y = invalidCityIndex;
		if( numbOfVisitedCities != numbOfCities)
			y = getY(x, visitedCities);
		while(y != invalidCityIndex) {//TODO XXX: if y == invalidCityIdex then no while loop - route is full of null values, and if y == invalidCity Index and end loop, one 
										//or more values are null!!!
			
			//routeCities.add(numbOfVisitedCities++, aco.getInitialSolution().getObjective(x));//.initialRoute.get(x));//? x = original
			routeCities.setVariableValue(numbOfVisitedCities++, x);
			routeDistance += getDistance(x,y);
			adjustPheromonLevel(x, y, routeDistance);
			visitedCities.put(y, true);
			x = y;
			if(numbOfVisitedCities != numbOfCities)
				y = getY(x, visitedCities);
			else
				y = invalidCityIndex;
		}
		routeDistance += getDistance(x,originatingCityIndex);
		routeCities.setVariableValue(numbOfVisitedCities, x);
		route = new DefaultIntegerPermutationSolution(routeCities);
		return this;
	}
	
	private double getDistance(int x, int y) {
		double result =  ((TSP)aco.getProblem()).getDistanceMatrix()[x][y]; //TODO: should the interface get an public method getDistanceMatrix or are there other solutions?
		return result;
	}
	private void adjustPheromonLevel(int x, int y, double routeDistance) {
		boolean flag = false;
		while(!flag) {
			double currentPheromonLevel = aco.getPheramonLevelMatrix()[x][y].doubleValue();
			double updatedPheromonLevel = (1-RHO) * currentPheromonLevel + Q / routeDistance;
			if(D)System.out.println("FeromonLevel(c/u/d): " + currentPheromonLevel + " / " + updatedPheromonLevel + " / " + (currentPheromonLevel - updatedPheromonLevel));
			if(updatedPheromonLevel < 0.00)
				flag = aco.getPheramonLevelMatrix()[x][y].compareAndSet(0);
			else
				flag = aco.getPheramonLevelMatrix()[x][y].compareAndSet(updatedPheromonLevel);
		}
	}
	
	private int getY(int x, HashMap<Integer, Boolean> visitedCities) {
		int returnY = invalidCityIndex;
		double random = JMetalRandom.getInstance().nextDouble();
		ArrayList<Double> transitionProbabilities = getTransitionProbabilities(x, visitedCities);	
		for(int y=0;y<numbOfCities;y++) {
			if(transitionProbabilities.get(y) > random) {
				returnY = y;
				break;
			}else
				random -= transitionProbabilities.get(y);
		}
		return returnY;
	}
	

	private ArrayList<Double> getTransitionProbabilities(int x, HashMap<Integer, Boolean> visitedCities) {
		ArrayList<Double> transitionProbabilities = new ArrayList<Double>(numbOfCities);
		IntStream.range(0, numbOfCities).forEach(i -> transitionProbabilities.add(0.0));
		
		double denominator = getTPDenominator(transitionProbabilities, x, visitedCities);
		IntStream.range(0,  numbOfCities).forEach(y -> {
			if(D)System.out.println("transitionProbabilities: " + transitionProbabilities.get(y));
			transitionProbabilities.set(y, transitionProbabilities.get(y)/denominator); 
		});
		return transitionProbabilities;
	}
	
	//Nenner
	private double getTPDenominator(ArrayList<Double> transitionProbabilities, int x, HashMap<Integer, Boolean> visitedCities) {
		double denominator = 0.0;
		for(int y = 0; y < numbOfCities; y++) {
			if(!visitedCities.get(y)) {
				if(x == y)
					transitionProbabilities.set(y, 0.0);
				else
					transitionProbabilities.set(y, getTPNumerator(x, y));
				denominator += transitionProbabilities.get(y);
			}
		}
		if(D) System.out.println(transitionProbabilities);
		if(D) System.out.println("Denominator: " + denominator);
		return denominator;
	}
	
	//Zähler
	private double getTPNumerator(int x, int y) {
		double numerator = 0.0;
		double pheromonLevel = aco.getPheramonLevelMatrix()[x][y].doubleValue();
		if(pheromonLevel != 0.0)
			numerator = Math.pow(pheromonLevel, ALPHA) * Math.pow(1 /  ((TSP)aco.getProblem()).getDistanceMatrix()[x][y], BETA); //TODO: cast to TSP very bad solution!
		if(D)System.out.println("TPNumerator: " + numerator);
		return numerator;
	}

	public int getAntNumb() { return antNumb; }
}
