package org.uma.jmetal.algorithm.singleobjective.ACO.NRP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import org.uma.jmetal.problem.singleobjective.NRP;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class AntNRP<S extends Solution<?>> {

	private AntColonyOptimizationNRP<S> aco;
	private int antNumb;
	private S route = null;
	private double alpha;
	private double beta;
	private double rho;
	private double q;

	public S getSolution() {
		return route;
	}

	static int invalidIndex = -1;

	private int numbOfCities;
	int _x = 0, _y = 0;

	public AntNRP(AntColonyOptimizationNRP<S> aco, int antNumb, double alpha, double beta, double rho, double q) {
		this.aco = aco;
		this.antNumb = antNumb;
		this.numbOfCities = aco.getProblemSize();// 1 < x < n -> [0,..10] -> 11
		this.alpha = alpha;
		this.beta = beta;
		this.rho = rho;
		this.q = q;

	}

	public AntNRP<S> run() {
		int originatingCityIndex = JMetalRandom.getInstance().nextInt(0, numbOfCities - 1);
		route = aco.getInitialSolution();

		HashMap<Integer, Boolean> visitedCities = new HashMap<Integer, Boolean>(numbOfCities);
		IntStream.range(0, numbOfCities).forEach(x -> visitedCities.put(x, false));
		int numbOfVisitedCities = 0;
		visitedCities.put(originatingCityIndex, true);
		double routeDistance = 0.0;
		int x = originatingCityIndex;
		int y = invalidIndex;
		S tmpCopy;

		if (numbOfVisitedCities < numbOfCities)
			y = getY(x, visitedCities);
		while (y != invalidIndex) {
			numbOfVisitedCities++;
			tmpCopy = (S) route.copy();
			((BinarySolution) route).getVariableValue(0).set(x);
			aco.getProblem().evaluate(route);

			if (route.getObjective(0) == -1) {
				route = tmpCopy;
				aco.getPheramonLevelMatrix()[_x][_y] = 0.01; // last pheromon level update should be set to 0 or very
																// small value, because it caused invalid solution!
				return this;
			}
			routeDistance += getDistance(x, y);
			adjustPheromonLevel(x, y, routeDistance);
			visitedCities.put(y, true);
			x = y;
			if (numbOfVisitedCities < numbOfCities)
				y = getY(x, visitedCities);
			else
				y = invalidIndex;
		}
		routeDistance += getDistance(x, originatingCityIndex);
		((BinarySolution) route).getVariableValue(0).set(x);
		return this;
	}

	private double getDistance(int x, int y) {
		return ((NRP) aco.getProblem()).getDistanceProfit(x, y);
	}

	public static int count = 0;

	private void adjustPheromonLevel(int x, int y, double routeDistance) {
		boolean flag = false;
		while (!flag) {
			double currentPheromonLevel = aco.getPheramonLevelMatrix()[x][y];
			double updatedPheromonLevel = (1 - rho) * currentPheromonLevel + count++ / routeDistance;

			if (updatedPheromonLevel < 0.00) {
				aco.getPheramonLevelMatrix()[x][y] = 0.0;
				flag = false;
			} else {
				aco.getPheramonLevelMatrix()[x][y] = updatedPheromonLevel;
				_x = x;
				_y = y;

				flag = true;
			}
		}
	}

	private int getY(int x, HashMap<Integer, Boolean> visitedCities) {
		int returnY = invalidIndex;
		double random = JMetalRandom.getInstance().nextDouble();
		ArrayList<Double> transitionProbabilities = getTransitionProbabilities(x, visitedCities);
		for (int y = 0; y < numbOfCities; y++) {
			if (transitionProbabilities.get(y) > random) {
				returnY = y;
				break;
			} else {
				random -= transitionProbabilities.get(y);
			}
		}
		return returnY;
	}

	private ArrayList<Double> getTransitionProbabilities(int x, HashMap<Integer, Boolean> visitedCities) {
		ArrayList<Double> transitionProbabilities = new ArrayList<Double>(numbOfCities);
		IntStream.range(0, numbOfCities).forEach(i -> transitionProbabilities.add(0.0));

		double denominator = getTPDenominator(transitionProbabilities, x, visitedCities);
		IntStream.range(0, numbOfCities).forEach(y -> {
			transitionProbabilities.set(y, transitionProbabilities.get(y) / denominator);
		});
		return transitionProbabilities;
	}

	private double getTPDenominator(ArrayList<Double> transitionProbabilities, int x,
			HashMap<Integer, Boolean> visitedCities) {
		double denominator = 0.0;
		for (int y = 0; y < numbOfCities; y++) {
			if (!visitedCities.get(y)) {
				if (x == y)
					transitionProbabilities.set(y, 0.0);
				else
					transitionProbabilities.set(y, getTPNumerator(x, y));
				denominator += transitionProbabilities.get(y);
			}
		}
		return denominator;
	}

	private double getTPNumerator(int x, int y) {
		double numerator = 0.0;

		double pheromonLevel = aco.getPheramonLevelMatrix()[x][y];
		double distanceLevel = 1;
		((NRP) aco.getProblem()).getDistanceProfit(x, y);
		if (pheromonLevel != 0.0) {
			numerator = Math.pow(pheromonLevel, alpha) * Math.pow(1 / distanceLevel, beta);
		}
		return numerator;
	}

	public int getAntNumb() {
		return antNumb;
	}

	@Override
	public String toString() {
		return " " + this.getSolution();
	}
}
