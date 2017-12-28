package org.uma.jmetal.algorithm.singleobjective.ACO.NRP;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import org.uma.jmetal.algorithm.singleobjective.ACO.TSP.AntColonyOptimizationTSP;
import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.DefaultIntegerPermutationSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class AntNRP<S extends Solution<?>> {
	
	private AntColonyOptimizationTSP<S> aco;
	private int antNumb;
	private S route = null;
	private double alpha;
	private double beta;
	private double rho;
	private double q;
	
	public S getSolution() { return route; }
	
	static int invalidCityIndex = -1;
	
	private int numbOfCities;
	
	public AntNRP(AntColonyOptimizationTSP<S> aco, int antNumb, double alpha, double beta, double rho, double q) {
		this.aco = aco;
		this.antNumb = antNumb;
		this.numbOfCities = aco.getProblemSize();// 1 < x < n -> [0,..10] -> 11
		this.alpha = alpha;
		this.beta = beta;
		this.rho = rho;
		this.q = q;
		
	}
	

	public AntNRP<S> run() {
		int originatingCityIndex = JMetalRandom.getInstance().nextInt(0, numbOfCities-1); 
		route = aco.getInitialSolution();//TODO: should get an empty solution -> new abstract class?
		IntStream.range(0, numbOfCities).forEach(x -> ((DefaultIntegerPermutationSolution)route).setVariableValue(x, 0));
		route.setObjective(0, 0);//TODO: if solution is empty remove this line.
		
		HashMap<Integer, Boolean> visitedCities = new HashMap<Integer, Boolean>(numbOfCities);
		IntStream.range(0, numbOfCities).forEach(x -> visitedCities.put(x, false)); 
		int numbOfVisitedCities = 0;
		visitedCities.put(originatingCityIndex, true); 
		double routeDistance = 0.0;
		int x = originatingCityIndex;
		int y = invalidCityIndex;
		
		if(numbOfVisitedCities < numbOfCities)
			y = getY(x, visitedCities);
		while(y != invalidCityIndex) {
			
			((DefaultIntegerPermutationSolution)route).setVariableValue(numbOfVisitedCities++, x);
			routeDistance += getDistance(x,y);
			adjustPheromonLevel(x, y, routeDistance);
			visitedCities.put(y, true);
			x = y;
			if(numbOfVisitedCities < numbOfCities)
				y = getY(x, visitedCities);
			else
				y = invalidCityIndex;
		}
		routeDistance += getDistance(x,originatingCityIndex);
		((DefaultIntegerPermutationSolution)route).setVariableValue(numbOfVisitedCities, x);//TODO: how to setVariable in Solution interface, cast to DefaultSolution?
		
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
			double updatedPheromonLevel = (1-rho) * currentPheromonLevel + q / routeDistance;
			
			if(updatedPheromonLevel < 0.00) {
				 aco.getPheramonLevelMatrix()[x][y] = 0.0;
				 flag = false;
			}else {
				aco.getPheramonLevelMatrix()[x][y] = updatedPheromonLevel;
				flag = true;
			}
		}
	}
	
	private int getY(int x, HashMap<Integer, Boolean> visitedCities) {
		
		//System.err.println("-->" + x);
		
		int returnY = invalidCityIndex;
		double random = JMetalRandom.getInstance().nextDouble();
		ArrayList<Double> transitionProbabilities = getTransitionProbabilities(x, visitedCities); //TODO: sum of all transitionProbabities should be 1!!!! not 0.9998!!
		for(int y=0;y<numbOfCities;y++) {
			if(transitionProbabilities.get(y) > random) {
				returnY = y;
				break;
			}else {
				random -= transitionProbabilities.get(y);}
		}
		return returnY;
	}
	

	private ArrayList<Double> getTransitionProbabilities(int x, HashMap<Integer, Boolean> visitedCities) {
		ArrayList<Double> transitionProbabilities = new ArrayList<Double>(numbOfCities);
		IntStream.range(0, numbOfCities).forEach(i -> transitionProbabilities.add(0.0));
		
		double denominator = getTPDenominator(transitionProbabilities, x, visitedCities);
		IntStream.range(0,  numbOfCities).forEach(y -> {
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
		return denominator;
	}
	
	//Zähler
	private double getTPNumerator(int x, int y) {
		double numerator = 0.0;
		
		double pheromonLevel = aco.getPheramonLevelMatrix()[x][y].doubleValue();
		if(pheromonLevel != 0.0)
			numerator = Math.pow(pheromonLevel, alpha) * Math.pow(1 /  ((TSP)aco.getProblem()).getDistanceMatrix()[x][y], beta); //TODO: cast to TSP very bad solution!
		return numerator;
	}

	public int getAntNumb() { return antNumb; }
	
	@Override
	public String toString() {
		return " " + this.getSolution();
	}
}
