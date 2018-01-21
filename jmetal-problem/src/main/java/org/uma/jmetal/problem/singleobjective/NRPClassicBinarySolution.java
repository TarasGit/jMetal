package org.uma.jmetal.problem.singleobjective;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.math3.util.Pair;
import org.uma.jmetal.problem.BudgetProblem;
import org.uma.jmetal.problem.impl.MyAbstractBinaryProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.JMetalException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
 
@SuppressWarnings("serial")
public class NRPClassicBinarySolution extends MyAbstractBinaryProblem implements BudgetProblem, NRP {//TODO: change name of My*.

	private int levelOfRequirements = 0;
	private int numberOfRequirementsInLevel[] = null;
	private int costsOfRequirements[][] = null;

	private int numberOfDependencies = 0;
	private Multimap<Integer, Integer> dependencies = ArrayListMultimap.create();;

	private int numberOfCustoments = 0;
	private List<Customer> customers = null;
	private int costs;
	private double costFactor;
	private double distancePrifitMatrix[][];// for ACO: difference of the profit between the neighbors.

	/**
	 * Creates a new TSP problem instance
	 */
	public NRPClassicBinarySolution(String distanceFile, double costFactor) throws IOException {
		readProblem(distanceFile);
		this.costs = computeAllCosts();// tsp1.txt = 857;
		this.costFactor = costFactor;
		System.out.println("All costs: " + this.costs);// TODO: remove.

		setNumberOfVariables(1);
		setNumberOfObjectives(1);
		setName("NRPClassic");

	}

	/** Evaluate() method */

	public void readProblem(String file) throws IOException {

		InputStream in = getClass().getResourceAsStream(file);
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);

		int depA, depB;

		StreamTokenizer token = new StreamTokenizer(br);
		try {

			token.nextToken();// TODO: check if NULL? ex. if ((token.sval != null)

			levelOfRequirements = (int) token.nval;
			costsOfRequirements = new int[levelOfRequirements][];
			numberOfRequirementsInLevel = new int[levelOfRequirements];

			for (int i = 0; i < levelOfRequirements; i++) {
				token.nextToken();
				int k = numberOfRequirementsInLevel[i] = (int) token.nval;
				costsOfRequirements[i] = new int[k];
				for (int j = 0; j < k; j++) {
					token.nextToken();
					costsOfRequirements[i][j] = (int) token.nval;
				}
			}

			token.nextToken();
			numberOfDependencies = (int) token.nval;

			for (int i = 0; i < numberOfDependencies; i++) {
				token.nextToken();
				depA = (int) token.nval;
				token.nextToken();
				depB = (int) token.nval;

				dependencies.put(depA, depB);
			}

			token.nextToken();
			numberOfCustoments = (int) token.nval;
			customers = new ArrayList<>();

			int k = 0;
			for (int i = 1; i <= numberOfCustoments; i++) {
				Customer customer = new Customer();
				token.nextToken();
				customer.setId(i);
				customer.setProfit((int) token.nval);

				token.nextToken();
				k = (int) token.nval;
				customer.setNumberOfRequests(k);

				for (int j = 0; j < k; j++) {
					token.nextToken();
					customer.addToRequirementList((int) token.nval);
				}
				customers.add(customer);
			}

		} catch (Exception e) {
			new JMetalException("NRPClassic.readProblem(): error when reading data file " + e);
		}
	}

	@Override
	public void evaluate(BinarySolution solution) {
		double localCosts = getEvaluatedCosts(solution);
		double tempBudget = this.getBudget();
		if (!violateBudget(localCosts, tempBudget)) {
			solution.setObjective(0, getEvaluatedProfit(solution));
			solution.setAttribute(0, localCosts);
		} else {
			solution.setObjective(0, -1);
			solution.setAttribute(0, -1);
		}
	}

	// TODO: this method will be needed for multiple-objective optimization, to
	// compute the distance of costs for two neighbor customers.
	public void computeProfitMatrix() {// TODO: too complex calculation for profit, this structure should be used to
										// compute costs difference, not profit.
		distancePrifitMatrix = new double[numberOfCustoments][numberOfCustoments];
		BinarySolution initialSolution1 = this.createSolution();
		BinarySolution initialSolution2 = this.createSolution();
		for (int i = 0; i < numberOfCustoments; i++) {
			initialSolution1.getVariableValue(0).set(i);
			for (int j = 0; j < numberOfCustoments; j++) {
				if (i == j) {
					distancePrifitMatrix[i][j] = 0;
				} else {
					initialSolution2.getVariableValue(0).set(i);
					distancePrifitMatrix[i][j] = Math.abs(getEvaluatedProfit(initialSolution2));
					initialSolution2.getVariableValue(0).clear(0);
				}
			}
			initialSolution1.getVariableValue(0).clear(i);
		}
	}

	public double getEvaluatedProfit(BinarySolution solution) {
		double fitness = 0.0;

		for (int i = 0; i < numberOfCustoments; i++) {
			if (solution.getVariableValue(0).get(i)) {// TODO: should it be a binary Solution?
				Customer customer = customers.get(i);
				fitness += customer.getProfit();
			}
		}
		return fitness;
	}

	public double getEvaluatedCosts(BinarySolution solution) {
		Set<Integer> setOfRequirements = new HashSet<>();

		double fitness = 0.0;
		int numberOfRequests;
		int requirement;

		for (int i = 0; i < numberOfCustoments; i++) {
			if (solution.getVariableValue(0).get(i)) {
				Customer customer = customers.get(i);
				numberOfRequests = customer.getNumberOfRequests();
				for (int j = 0; j < numberOfRequests; j++) {
					requirement = customer.getFromRequirementList(j);
					addAllDependenciesToSetRecursively(requirement, setOfRequirements);
				}
			}
		}

		List<Integer> list = new ArrayList<>(setOfRequirements);
		for (int i = 0; i < list.size(); i++) {
			fitness += getCostFromRequirement(list.get(i));
		}
		return fitness;
	}

	public void addToSetIfUnique(int value, Set<Integer> setOfRequirements) {
		setOfRequirements.add(value);
	}

	public void addAllDependenciesToSetRecursively(int requirement, Set<Integer> setOfRequirements) {
		ArrayList<Integer> tmpArrayList = new ArrayList<>();
		int localRequirement;
		addToSetIfUnique(requirement, setOfRequirements);
		List<Integer> localDependencies = getDependencies(requirement);

		/*
		 * To remove a possible cycle in dependencies, check if this dependency is
		 * already in HashSet
		 */

		for (Integer i : localDependencies) { // TODO: do it by some library. if
			if (!setOfRequirements.contains(i))
				tmpArrayList.add(i);
		}

		while (!tmpArrayList.isEmpty()) {
			localRequirement = tmpArrayList.remove(0);
			addAllDependenciesToSetRecursively(localRequirement, setOfRequirements);
		}
	}

	public Double getCostFromRequirement(int localRequirement) {
		double costs = 0.0;
		int k, m;
		Pair<Integer, Integer> positions;
		Optional<Pair<Integer, Integer>> result = getRequirementPosition(localRequirement);

		if (!result.isPresent()) {
			return 0.0;
		}

		positions = result.get();
		k = positions.getFirst();
		m = positions.getSecond();
		costs += costsOfRequirements[k][m];
		return costs;
	}

	public List<Integer> getDependencies(Integer requirement) {
		return new ArrayList<>(dependencies.get(requirement));
	}

	public Optional<Pair<Integer, Integer>> getRequirementPosition(int requirement) {
		requirement = requirement - 1; // requirement starts from 0..n -> subtract 1.
		int counter = 0;
		for (int k = 0; k < levelOfRequirements; k++) {
			for (int m = 0; m < numberOfRequirementsInLevel[k]; m++) {
				if (requirement == counter++) {// TODO: why do I search for the position in this way???
					return Optional.of(new Pair<>(k, m));
				}
			}
		}
		return Optional.empty();
	}

	public int computeAllCosts() {
		int sum = 0;
		for (int i = 0; i < levelOfRequirements; i++) {
			int k = numberOfRequirementsInLevel[i];
			for (int j = 0; j < k; j++) {
				sum += costsOfRequirements[i][j];
			}
		}
		return sum;
	}

	public int getCosts() {
		return this.costs;
	}

	public double getBudget() {
		return this.costs * costFactor;
	}

	public void setCostFactor(double costFactor) {
		this.costFactor = costFactor;
	}

	@Override
	public boolean violateBudget(double costs, double budget) {
		if (costs <= budget)
			return false;
		else
			return true;
	}

	public double getCostsOfRequirement(int x, int y) {
		return costsOfRequirements[x][y];
	}
	
	public double getDistanceProfit(int i, int j) {
		if(distancePrifitMatrix == null)
			computeProfitMatrix();
		
		return distancePrifitMatrix[i][j];
	}


	@Override
	protected int getBitsPerVariable(int index) {
		// TODO Auto-generated method stub
		return this.numberOfCustoments;
	}

	@Override
	public int getNumberOfBitInVariable(int index) {
		// TODO Auto-generated method stub
		return this.getBitsPerVariable(index);
	}

}
