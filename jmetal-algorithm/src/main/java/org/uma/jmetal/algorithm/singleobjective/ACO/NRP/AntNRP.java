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
	private S solution = null;
	private double alpha;
	private double beta;
	private double rho;
	private double q;

	public S getSolution() {
		return solution;
	}

	static int invalidIndex = -1;

	private int numberOfCustomers;
	int _x = 0, _y = 0;

	public AntNRP(AntColonyOptimizationNRP<S> aco, int antNumb, double alpha, double beta, double rho, double q) {
		this.aco = aco;
		this.antNumb = antNumb;
		this.numberOfCustomers = aco.getProblemSize();
		this.alpha = alpha;
		this.beta = beta;
		this.rho = rho;
		this.q = q;

	}

	public AntNRP<S> run() {
		int originatingCityIndex = JMetalRandom.getInstance().nextInt(0, numberOfCustomers - 1);
		solution = aco.getInitialSolution();

		HashMap<Integer, Boolean> acceptedCustomers = new HashMap<Integer, Boolean>(numberOfCustomers);
		IntStream.range(0, numberOfCustomers).forEach(x -> acceptedCustomers.put(x, false));
		int currentNumberOfCustomers = 0;
		acceptedCustomers.put(originatingCityIndex, true);
		double customersProfit = 0.0;
		int x = originatingCityIndex;
		int y = invalidIndex;
		S tmpCopy;

		if (currentNumberOfCustomers < numberOfCustomers)
			y = getNextCustomer(x, acceptedCustomers);
		while (y != invalidIndex) {
			currentNumberOfCustomers++;
			tmpCopy = (S) solution.copy();
			((BinarySolution) solution).getVariableValue(0).set(x);
			aco.getProblem().evaluate(solution);

			if (solution.getObjective(0) == -1) {
				solution = tmpCopy;
				aco.getPheramonLevelMatrix()[_x][_y] = 0.01; // last pheromon level update should be set to 0 or very
																// small value, because it caused invalid solution!
				return this;
			}
			customersProfit += getDistance(x, y);
			adjustPheromonLevel(x, y, customersProfit);
			acceptedCustomers.put(y, true);
			x = y;
			if (currentNumberOfCustomers < numberOfCustomers)
				y = getNextCustomer(x, acceptedCustomers);
			else
				y = invalidIndex;
		}
		customersProfit += getDistance(x, originatingCityIndex);
		((BinarySolution) solution).getVariableValue(0).set(x);
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

	private int getNextCustomer(int x, HashMap<Integer, Boolean> visitedCities) {
		int returnY = invalidIndex;
		double random = JMetalRandom.getInstance().nextDouble();
		ArrayList<Double> transitionProbabilities = getTransitionProbabilities(x, visitedCities);
		for (int y = 0; y < numberOfCustomers; y++) {
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
		ArrayList<Double> transitionProbabilities = new ArrayList<Double>(numberOfCustomers);
		IntStream.range(0, numberOfCustomers).forEach(i -> transitionProbabilities.add(0.0));

		double denominator = getTPDenominator(transitionProbabilities, x, visitedCities);
		IntStream.range(0, numberOfCustomers).forEach(y -> {
			transitionProbabilities.set(y, transitionProbabilities.get(y) / denominator);
		});
		return transitionProbabilities;
	}

	private double getTPDenominator(ArrayList<Double> transitionProbabilities, int x,
			HashMap<Integer, Boolean> visitedCities) {
		double denominator = 0.0;
		for (int y = 0; y < numberOfCustomers; y++) {
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
