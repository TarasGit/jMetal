package org.uma.jmetal.problem.singleobjective;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.impl.AbstractBinaryIntegerPermutationProblem;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.JMetalException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Class representing a single-objective TSP (Traveling Salesman Problem)
 * problem. It accepts data files from TSPLIB:
 * http://www.iwr.uni-heidelberg.de/groups/comopt/software/TSPLIB95/tsp/
 */
@SuppressWarnings("serial")
public class NRPClassic extends AbstractBinaryIntegerPermutationProblem{

//	public static void main(String[] args) {
//
//		NRPClassic nrp = null;
//		try {
//			nrp = new NRPClassic("/nrpClassicInstances/nrp2.txt");
//			System.out.println("All Costs: " + nrp.computeAllCosts());
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public static final boolean COSTSORPROFIT = true; // false = profit, true = costs;
	
	private int levelOfRequirements = 0;
	private int numberOfRequirementsInLevel[] = null;
	private int costsOfRequirements[][] = null;

	private int numberOfDependencies = 0;
	private Multimap<Integer, Integer> dependencies = ArrayListMultimap.create();;

	private int numberOfCustoments = 0;
	private List<Customer> customers = null;
	private int costs;

	/**
	 * Creates a new TSP problem instance
	 */

	public NRPClassic(String distanceFile) throws IOException {
		readProblem(distanceFile);
		this.costs = computeAllCosts();//tsp1.txt = 857;

		setNumberOfVariables(numberOfCustoments);
		setNumberOfObjectives(1);
		setName("NRPClassic");
	}

	/** Evaluate() method */

	private void readProblem(String file) throws IOException {

		InputStream in = getClass().getResourceAsStream(file);
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);

		int depA, depB;

		StreamTokenizer token = new StreamTokenizer(br);
		try {

			token.nextToken();// TODO: check if NULL? ex. // if ((token.sval != null) &&
								// ((token.sval.compareTo("DIMENSION") == 0)))

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
	public int getPermutationLength() {
		return this.numberOfCustoments;
	}

	@Override
	public void evaluate(PermutationSolution<Integer> solution) {
		if(COSTSORPROFIT) {
			solution.setObjective(0, getEvaluatedCosts(solution));
		}else {
			solution.setObjective(0, getEvaluatedProfit(solution));
		}
	}
	
	
	private double getEvaluatedProfit(PermutationSolution<Integer> solution) {
		double fitness = 0.0;
		
		for (int i = 0; i < numberOfCustoments; i++) {
			if (solution.getVariableValue(i) == 0) { 
				Customer customer = customers.get(i);
				fitness += customer.getProfit();	
			}//end - if
		}
		
		return fitness;
		
	}

	private double getEvaluatedCosts(PermutationSolution<Integer> solution) {
		int counter = 0;
		double fitness = 0.0;
		boolean found = false;
		
		//look for costs for all the requirements desired by the customers in solution - start
		for (int i = 0; i < numberOfCustoments; i++) {
			if (solution.getVariableValue(i) == 0) { //TODO: should be binary solution with 1 or 0.
				Customer customer = customers.get(i);
				int numberOfRequests = customer.getNumberOfRequests();
				for (int j = 0; j < numberOfRequests; j++) {
					int requirement = customer.getFromRequirementList(j);
					for (int k = 0; k < levelOfRequirements; k++) {
						for (int m = 0; m < numberOfRequirementsInLevel[k]; m++) {
							if (requirement == counter++) {
								fitness += costsOfRequirements[k][m];
								counter = 0;
								found = true;
								break;
							}
						}
						if (found) {
							found = false;
							break;
						}
					}
				}
			}//end - if
		}
		//costs for all customers - end
		
		return fitness;
	}

	private int computeAllCosts() {
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



	// public void evaluate(PermutationSolution<Integer> solution) {
	// double fitness1;
	//
	// fitness1 = 0.0;
	//
	// for (int i = 0; i < (numberOfCities - 1); i++) {
	// int x;
	// int y;
	//
	// x = solution.getVariableValue(i);
	// y = solution.getVariableValue(i + 1);
	//
	// fitness1 += distanceMatrix[x][y];
	// }
	// int firstCity;
	// int lastCity;
	//
	// firstCity = solution.getVariableValue(0);
	// lastCity = solution.getVariableValue(numberOfCities - 1);
	//
	// fitness1 += distanceMatrix[firstCity][lastCity];
	//
	// solution.setObjective(0, fitness1);
	// }
}
